package com.kynsoft.notification.application.query.file.getbyid;

import com.kynsof.share.core.domain.bus.query.IQueryHandler;
import com.kynsof.share.core.domain.response.FileDto;
import com.kynsoft.notification.application.query.file.search.FileResponse;
import com.kynsoft.notification.domain.service.IAFileService;
import org.springframework.stereotype.Component;

@Component
public class FindByIdAFileQueryHandler implements IQueryHandler<FindByIdAFileQuery, FileResponse>  {

    private final IAFileService serviceImpl;

    public FindByIdAFileQueryHandler(IAFileService serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    @Override
    public FileResponse handle(FindByIdAFileQuery query) {
        FileDto userSystemDto = serviceImpl.findById(query.getId());

        return new FileResponse(userSystemDto);
    }
}
