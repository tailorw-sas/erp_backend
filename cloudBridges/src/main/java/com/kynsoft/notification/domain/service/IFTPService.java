package com.kynsoft.notification.domain.service;

import com.kynsof.share.core.domain.response.FileDto;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface IFTPService {
    void uploadFile(String remotePath, byte[] fileBytes, String fileName, String server, String user, String password,
                    int port) ;
    Mono<Void> uploadFilesBatch(String remotePath, List<FileDto> files, String server, String user, String password);
}