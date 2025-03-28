package com.kynsof.share.core.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private UUID id;
    private String name;
    private String microServiceName;
    private String url;
    private boolean isConfirmed;

    @JsonIgnore
    protected UploadFileResponse uploadFileResponse;
    @JsonIgnore
    protected byte[] fileContent;


    public void setUploadStatus(boolean success, String message) {
        this.uploadFileResponse = success
                ? new UploadFileResponse(ResponseStatus.SUCCESS_RESPONSE)
                : new UploadFileResponse(ResponseStatus.ERROR_RESPONSE, message);
    }
}
