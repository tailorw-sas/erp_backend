package com.kynsof.share.core.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    private UUID id;
    private String name;
    private String microServiceName;
    private String url;
    private boolean isConfirmed;
    protected UploadFileResponse uploadFileResponse;
}
