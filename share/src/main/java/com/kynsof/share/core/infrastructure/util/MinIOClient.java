package com.kynsof.share.core.infrastructure.util;

import com.kynsof.share.core.domain.request.FileRequest;
import com.kynsof.share.core.domain.response.FileDto;
import com.kynsof.share.core.domain.response.ResponseStatus;
import com.kynsof.share.core.domain.response.UploadFileResponse;
import com.kynsof.share.core.domain.service.IAmazonClient;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.Channels;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service("minio")
public class MinIOClient implements IAmazonClient {

    private static final Logger logger = LoggerFactory.getLogger(MinIOClient.class);
    private MinioClient minioClient;

    @Value("${minio.endpoint.url}")
    private String endpointUrl;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.bucket.private}")
    private Boolean isPrivateBucket;

    @PostConstruct
    private void initializeMinIO() {
        logger.info("🔗 Initializing MinIO connection...");
        minioClient = MinioClient.builder()
                .endpoint(endpointUrl)
                .credentials(accessKey, secretKey)
                .build();
        logger.info("✅ MinIO connection initialized successfully.");
    }

    @Override
    public void uploadFile(InputStream streamToUpload, Long size, String contentType, String objectKey) throws IOException {
        logger.info("📤 Uploading file '{}' with size {} bytes to MinIO...", objectKey, size);
        try {
            PutObjectArgs putObject = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectKey)
                    .stream(streamToUpload, size, -1)
                    .contentType(contentType)
                    .build();
            minioClient.putObject(putObject);
            logger.info("✅ File '{}' uploaded successfully.", objectKey);
        } catch (Exception e) {
            logger.error("❌ Error uploading file to MinIO: {}", objectKey, e);
            throw new IOException(e);
        }
    }

    @Override
    public String save(FileRequest fileRequest) throws IOException {
        return save(fileRequest.getFile(), fileRequest.getFileName(), fileRequest.getContentType());
    }

    public String save(byte[] bytes, String fileName, String contentType) throws IOException {
        String objectKey = getName(fileName);
        try (InputStream fileStream = new ByteArrayInputStream(bytes)) {
            uploadFile(fileStream, (long) bytes.length, contentType, objectKey);
            if (isPrivateBucket) {
                return getPublicUrl(objectKey);
            }

            return endpointUrl + bucketName + "/" + objectKey;
        } catch (Exception e) {
            throw new IOException("Error saving file: " + e.getMessage(), e);
        }
    }

    @Override
    public String save(FilePart filePart) throws IOException {
        return getBytesFromFilePart(filePart)
                .<String>handle((bytes, sink) -> {
                    String contentType = Objects.requireNonNull(filePart.headers().getContentType()).toString();
                    try {
                        String fileUrl = this.save(bytes, filePart.filename(), contentType);
                        sink.next(fileUrl);
                    } catch (IOException e) {
                        sink.error(new RuntimeException(e));
                    }
                })
                .block();
    }

    @Override
    public List<FileDto> saveAll(List<FileRequest> files) {
        logger.info("📦 Processing batch upload of {} files...", files.size());
        List<CompletableFuture<FileDto>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    FileDto fileDto = new FileDto();
                    fileDto.setId(file.getObjectId());
                    fileDto.setName(file.getFileName());
                    fileDto.setUploadFileResponse(new UploadFileResponse(ResponseStatus.SUCCESS_RESPONSE));
                    try {
                        String fileUrl = save(file);
                        fileDto.setUrl(fileUrl);
                    } catch (IOException e) {
                        fileDto.setUploadFileResponse(new UploadFileResponse(ResponseStatus.ERROR_RESPONSE,
                                "UPLOAD_FAILED: " + e.getMessage()));
                    }
                    return fileDto;
                }))
                .toList();

        List<FileDto> uploadedFiles = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        logger.info("📦 Batch upload completed. {} files processed.", uploadedFiles.size());
        return uploadedFiles;
    }

    @Override
    public void delete(String url) {
        logger.info("🗑️ Deleting file: {}", url);
        if (url.isEmpty()) {
            logger.warn("⚠️ Skipping deletion: empty URL.");
            return;
        }
        if (url.contains(this.endpointUrl)) {
            url = url.replace(this.endpointUrl, "");
        }

        try {
            removeObject(url);
            logger.info("✅ File '{}' deleted successfully.", url);
        } catch (Exception e) {
            logger.error("❌ Error deleting file: {}", url, e);
        }
    }

    @Override
    public byte[] downloadFile(String filePath) {
        logger.info("📥 Downloading file from MinIO: {}", filePath);
        try {
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filePath)
                            .build()
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            inputStream.transferTo(outputStream);
            inputStream.close();

            logger.info("✅ File downloaded successfully: {}", filePath);
            return outputStream.toByteArray();
        } catch (MinioException e) {
            logger.error("❌ MinIO error while downloading file: {}", filePath, e);
        } catch (Exception e) {
            logger.error("❌ Unexpected error while downloading file: {}", filePath, e);
        }
        return null;
    }

    private String getPublicUrl(String objectName) throws IOException {
        try {
            logger.info("🔗 Generating public URL for file: {}", objectName);
            String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(604800)
                    .build());
            logger.info("✅ Public URL generated: {}", url);
            return url;
        } catch (Exception e) {
            logger.error("❌ Error generating public URL for: {}", objectName, e);
            throw new IOException(e);
        }
    }

    private void removeObject(String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        RemoveObjectArgs req = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        minioClient.removeObject(req);
    }

    public static Mono<byte[]> getBytesFromFilePart(FilePart filePart) {
        return filePart.content()
                .flatMap(dataBuffer -> {
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                        Channels.newChannel(outputStream).write(dataBuffer.asByteBuffer());
                        DataBufferUtils.release(dataBuffer); // Release the buffer to avoid memory leaks
                        return Mono.just(outputStream.toByteArray());
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException("Error reading file", e));
                    }
                })
                .reduce((bytes1, bytes2) -> {
                    byte[] combined = new byte[bytes1.length + bytes2.length];
                    System.arraycopy(bytes1, 0, combined, 0, bytes1.length);
                    System.arraycopy(bytes2, 0, combined, bytes1.length, bytes2.length);
                    return combined;
                });
    }

    public static String getName(String originalFilename) {
        if (originalFilename == null) {
            originalFilename = UUID.randomUUID().toString();
        }
        String sanitizedFilename = originalFilename.replace(" ", "_");
        String fileExtension = StringUtils.getFilenameExtension(sanitizedFilename);
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        return StringUtils.stripFilenameExtension(sanitizedFilename) + "_" + timestamp + "." + fileExtension;
    }
}