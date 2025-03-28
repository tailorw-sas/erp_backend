package com.kynsof.share.core.domain.service;

import com.kynsof.share.core.domain.request.FileRequest;
import com.kynsof.share.core.domain.response.FileDto;
import org.springframework.http.codec.multipart.FilePart;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IAmazonClient {

    void uploadFile(InputStream streamToUpload, Long size, String contentType, String objectKey) throws IOException;

    String save(FileRequest fileRequest) throws IOException;
    String save(FilePart file) throws IOException;
    String save(byte[] bytes, String fileName, String contentType) throws IOException;

    List<FileDto> saveAll(List<FileRequest> files);

    void delete(String url);

    byte[] downloadFile(String filePath);
}
