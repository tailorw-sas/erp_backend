package com.kynsoft.notification.application.command.file.multiSaveFileS3;

import com.kynsof.share.core.domain.bus.command.ICommandMessage;
import com.kynsof.share.core.domain.request.FileRequest;
import com.kynsof.share.core.domain.response.FileDto;
import lombok.Getter;

import java.util.List;

@Getter
public class MultiSaveFileS3Message implements ICommandMessage {

    private final List<FileDto> files;

    public MultiSaveFileS3Message(List<FileDto> files){
        this.files = files;
    }
}
