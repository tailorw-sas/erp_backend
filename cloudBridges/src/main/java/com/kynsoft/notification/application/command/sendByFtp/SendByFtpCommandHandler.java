package com.kynsoft.notification.application.command.sendByFtp;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.response.FileDto;
import com.kynsof.share.core.domain.response.ResponseStatus;
import com.kynsof.share.core.domain.response.UploadFileResponse;
import com.kynsof.share.core.domain.service.IAmazonClient;
import com.kynsoft.notification.domain.service.IFTPService;
import com.kynsoft.notification.infrastructure.service.kafka.producer.ProducerSendByFtpResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
@Transactional
public class SendByFtpCommandHandler implements ICommandHandler<SendByFtpCommand> {

    private static final Logger logger = Logger.getLogger(SendByFtpCommandHandler.class.getName());
    private final IAmazonClient amazonClient;
    private final IFTPService ftpService;
    private final ProducerSendByFtpResponse response;

    public SendByFtpCommandHandler(IAmazonClient amazonClient, IFTPService ftpService,
                                   ProducerSendByFtpResponse response) {
        this.amazonClient = amazonClient;
        this.ftpService = ftpService;
        this.response = response;
    }

    @Override
    public void handle(SendByFtpCommand command) {
        if (command.getFileDtos() == null || command.getFileDtos().isEmpty()) {
            logger.warning("No files to process in SendByFtpCommand.");
            return;
        }

        List<FileDto> successfulDownloads = new ArrayList<>();
        List<FileDto> failedDownloads = new ArrayList<>();

        for (FileDto fileDto : command.getFileDtos()) {
            byte[] fileContent = amazonClient.downloadFile(fileDto.getUrl());

            if (fileContent != null && fileContent.length > 0) {
                fileDto.setFileContent(fileContent);
                successfulDownloads.add(fileDto);
            } else {
                fileDto.setUploadFileResponse(new UploadFileResponse(ResponseStatus.ERROR_RESPONSE,
                        "Failed downloading file from common storage"));
                failedDownloads.add(fileDto);
            }
        }

        if (!successfulDownloads.isEmpty()) {
            ftpService.uploadFilesBatch(command.getUrl(), successfulDownloads, command.getServer(), command.getUserName(),
                    command.getPassword())
                    .doOnSuccess( unused ->  logger.info("✅ FTP batch upload completed."))
                    .doOnError(error -> logger.severe("❌ FTP batch upload failed: " + error.getMessage()))
                    .subscribe();
        } else {
            logger.warning("No successful downloads available for FTP upload.");
        }

        List<FileDto> allDownloads = new ArrayList<>();
        allDownloads.addAll(successfulDownloads);
        allDownloads.addAll(failedDownloads);
        this.response.create(allDownloads);
    }
}
