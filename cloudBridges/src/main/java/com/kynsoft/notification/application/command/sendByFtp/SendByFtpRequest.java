package com.kynsoft.notification.application.command.sendByFtp;

import com.kynsof.share.core.domain.response.FileDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SendByFtpRequest {
    private String server;
    private String userName;
    private String password;
    private String url;
    private List<FileDto> fileDtos;
}
