package com.kynsoft.notification.application.command.file.multiSaveFileS3;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.response.FileDto;
import com.kynsof.share.core.domain.response.ResponseStatus;
import com.kynsof.share.core.domain.service.IAmazonClient;
import com.kynsoft.notification.domain.service.IAFileService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MultiSaveFileS3CommandHandler implements ICommandHandler<MultiSaveFileS3Command> {

    private final IAmazonClient amazonClient;
    private final IAFileService fileService;

    public MultiSaveFileS3CommandHandler(IAmazonClient amazonClient,
                                         IAFileService fileService){
        this.amazonClient = amazonClient;
        this.fileService = fileService;
    }

    @Override
    public void handle(MultiSaveFileS3Command command) {
        if(Objects.isNull(command.getFileRequests()) || command.getFileRequests().isEmpty()){
            throw new IllegalArgumentException("The file request list must not be null or empty");
        }

        List<FileDto> files = this.amazonClient.saveAll(command.getFileRequests(), this.amazonClient.getBucketName());

        this.saveUploadedFiles(files);
        command.setFiles(files);
    }

    private void saveUploadedFiles(List<FileDto> files){
        List<FileDto> uploadedFiles = files.stream()
                .filter(fileDto -> ResponseStatus.SUCCESS_RESPONSE.equals(fileDto.getUploadFileResponse().getStatus()))
                .collect(Collectors.toList());
        if(!files.isEmpty()){
            fileService.createAll(uploadedFiles);
        }
    }
}
