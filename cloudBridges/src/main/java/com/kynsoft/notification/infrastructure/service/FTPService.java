package com.kynsoft.notification.infrastructure.service;

import com.kynsof.share.core.application.ftp.FtpService;
import com.kynsof.share.core.domain.response.FileDto;
import com.kynsof.share.core.domain.response.ResponseStatus;
import com.kynsof.share.core.domain.response.UploadFileResponse;
import com.kynsoft.notification.domain.service.IFTPService;
import com.kynsoft.notification.infrastructure.config.FTPConfig;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class FTPService implements IFTPService {

    private static final Logger log = LoggerFactory.getLogger(FtpService.class);
    private final FTPConfig ftpConfig;
    private final Random random = new Random();

    public FTPService(FTPConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    public void uploadFile(String remotePath, byte[] fileBytes, String fileName, String server, String user,
                           String password, int port) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            if (!ftpClient.login(user, password)) {
                log.error("❌ Authentication failed for server: {}", server);
                ftpClient.logout(); // Cerrar sesión antes de lanzar excepción
                throw new RuntimeException("Authentication failed with the FTP server.");
            }

            configureFTPClient(ftpClient);
            setRemotePath(ftpClient, remotePath);

            if (!uploadFileWithRetry(ftpClient, fileName, fileBytes, 4, null)) {
                log.error("❌ FTP upload failed for file: '{}'", fileName);
                throw new RuntimeException("FTP upload failed for file: " + fileName);
            }
        } catch (IOException e) {
            log.error("❌ FTP connection error while uploading '{}': {}", fileName, e.getMessage(), e);
            throw new RuntimeException("FTP connection error while uploading file: " + fileName, e);
        } finally {
            disconnectFTP(ftpClient);
        }
    }

    public Mono<Void> uploadFilesBatch(String remotePath, List<FileDto> files, String server, String user, String password) {
        return Mono.fromRunnable(() -> {
            FTPClient ftpClient = new FTPClient();
            try {
                ftpClient.connect(server, 21);
                if (!ftpClient.login(user, password)) {
                    ftpClient.logout();
                    log.error("❌ Authentication failed for server: {}", server);
                    throw new RuntimeException("Authentication failed with the FTP server.");
                }

                configureFTPClient(ftpClient);
                setRemotePath(ftpClient, remotePath);

                files.parallelStream().forEach(file -> {
                    try {
                        uploadFileWithRetry(ftpClient, file.getName(), file.getFileContent(), 4, file);
                    } catch (Exception e) {
                        log.error("❌ Error uploading file '{}': {}", file.getName(), e.getMessage());
                        file.setUploadFileResponse(new UploadFileResponse(ResponseStatus.ERROR_RESPONSE, e.getMessage()));
                    }
                });

            } catch (IOException e) {
                log.error("❌ FTP connection error: {}", e.getMessage(), e);
                throw new RuntimeException("FTP connection error while uploading files", e);
            } finally {
                disconnectFTP(ftpClient);
            }
        }).subscribeOn(Schedulers.boundedElastic())
        .then();  // Ensures it returns Mono<Void>
    }

    private void configureFTPClient(FTPClient ftpClient) throws IOException {
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setBufferSize(ftpConfig.getBufferSize());
        ftpClient.setConnectTimeout(ftpConfig.getConnectTimeout());
        ftpClient.setSoTimeout(ftpConfig.getSoTimeout());
    }

    private void setRemotePath(FTPClient ftpClient, String remotePath) throws IOException {
        if (remotePath == null || remotePath.trim().isEmpty() || "/".equals(remotePath) || "//".equals(remotePath)) {
            return;
        }

        String currentDir = ftpClient.printWorkingDirectory();
        if (!remotePath.equals(currentDir)) {
            if (!ftpClient.changeWorkingDirectory(remotePath)) {
                if (!ftpClient.makeDirectory(remotePath) || !ftpClient.changeWorkingDirectory(remotePath)) {
                    throw new RuntimeException("❌ Failed to create/access the directory.");
                }
            }
        }
    }

    private boolean uploadFileWithRetry(FTPClient ftpClient, String fileName, byte[] fileBytes, int maxRetries, FileDto fileDto) {
        int attempts = 0;
        int baseDelay = 1000; // 1 second in ms
        int maxDelay = 30000; // 30 seconds in ms

        while (attempts < maxRetries) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
                if (ftpClient.storeFile(fileName, inputStream)) {
                    log.info("✅ File '{}' uploaded successfully on attempt {}", fileName, attempts + 1);
                    if (fileDto != null) {
                        fileDto.setUploadFileResponse(new UploadFileResponse(ResponseStatus.SUCCESS_RESPONSE, "File uploaded successfully"));
                    }
                    return true;
                }
            } catch (IOException e) {
                log.warn("⚠️ Attempt {} failed for '{}': {}", attempts + 1, fileName, e.getMessage());
                if (fileDto != null) {
                    fileDto.setUploadFileResponse(new UploadFileResponse(ResponseStatus.ERROR_RESPONSE, "Upload failed: " + e.getMessage()));
                }
            }

            attempts++;
            if (attempts < maxRetries) {
                int delay = Math.min(baseDelay * (1 << (attempts - 1)), maxDelay);
                delay += random.nextInt(500); // Jitter to avoid collisions
                log.info("⏳ Waiting {} ms before next retry...", delay);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    log.error("⚠️ Retry wait interrupted", ie);
                    if (fileDto != null) {
                        fileDto.setUploadFileResponse(new UploadFileResponse(ResponseStatus.ERROR_RESPONSE, "Upload interrupted"));
                    }
                    return false;
                }
            }
        }

        log.error("❌ Failed to upload the file '{}' after {} attempts", fileName, maxRetries);
        if (fileDto != null) {
            fileDto.setUploadFileResponse(new UploadFileResponse(ResponseStatus.ERROR_RESPONSE, "Failed after " + maxRetries + " attempts"));
        }
        return false;
    }

    private void disconnectFTP(FTPClient ftpClient) {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException ex) {
            log.error("⚠️ Error while closing FTP connection: {}", ex.getMessage(), ex);
        }
    }
}