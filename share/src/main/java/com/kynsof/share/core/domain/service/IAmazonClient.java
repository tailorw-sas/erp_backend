//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.kynsof.share.core.domain.service;

import com.kynsof.share.core.domain.request.FileRequest;
import com.kynsof.share.core.domain.response.FileDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface IAmazonClient {

    String getBucketName();
    String save(FileRequest fileRequest, String bucketName) throws IOException;

    String save(FilePart filePart, String bucketName) throws IOException;

    List<FileDto> saveAll(List<FileRequest> files, String bucketName);

    void delete(String url);

    byte[] downloadFile(String filePath, String bucketName) throws IOException;
}
