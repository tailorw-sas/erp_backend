package com.kynsoft.notification.application.command.file.multiSaveFileS3;

import com.kynsof.share.core.domain.bus.command.ICommand;
import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsof.share.core.domain.request.FileRequest;
import com.kynsof.share.core.domain.response.FileDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultiSaveFileS3Command implements ICommand {

    private List<FileRequest> fileRequests;
    private List<FileDto> files;

    public MultiSaveFileS3Command(List<FileRequest> fileRequests){
        this.fileRequests = fileRequests;
    }

    @Override
    public ICommandMessage getMessage() {
        return new MultiSaveFileS3Message(files);
    }
}
