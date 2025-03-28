package com.kynsoft.notification.controller;

import com.kynsof.share.core.domain.response.ApiError;
import com.kynsof.share.core.domain.response.ApiResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.notification.application.command.file.confirm.ConfirmFileCommand;
import com.kynsoft.notification.application.command.file.confirm.ConfirmFileMessage;
import com.kynsoft.notification.application.command.file.confirm.ConfirmFileRequest;
import com.kynsoft.notification.application.command.file.saveFileS3.SaveFileS3Command;
import com.kynsoft.notification.application.command.file.saveFileS3.SaveFileS3Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final IMediator mediator;
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    public FileController(IMediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping(value = "")
    public Mono<ResponseEntity<ApiResponse<?>>> upload(@RequestPart("file") FilePart filePart) {
        try {
            Object response = mediator.send(new SaveFileS3Command(filePart));
            return Mono.just(ResponseEntity.ok(ApiResponse.success(response)));
        } catch (Exception e) {
            log.error("❌ Error uploading file: {}", e.getMessage(), e);
            return Mono.just(ResponseEntity.internalServerError()
                    .body(ApiResponse.fail(new ApiError("Failed to upload file: " + e.getMessage()))));
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> loadFile(@RequestBody ConfirmFileRequest request) {
        ConfirmFileCommand command = ConfirmFileCommand.fromRequest(request);
        ConfirmFileMessage response = mediator.send(command);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
