package com.kynsoft.notification.application.command.sendByFtp;


import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsof.share.core.domain.response.FileDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SendByFtpCommand implements ICommand {
    private String server;
    private String userName;
    private String password;
    private String url;
    private List<FileDto> fileDtos;

    public SendByFtpCommand(String server, String userName, String password, String url, List<FileDto> fileDtos) {
        this.server = server;
        this.userName = userName;
        this.password = password;
        this.url = url;
        this.fileDtos = fileDtos;
    }

    public static SendByFtpCommand fromRequest(SendByFtpRequest request) {
        return new SendByFtpCommand(request.getServer(), request.getUserName(), request.getPassword(), request.getUrl(),
                request.getFileDtos());
    }

    @Override
    public ICommandMessage getMessage() {
        return new SendByFtpMessage();//TODO APF return the real values
    }
}
