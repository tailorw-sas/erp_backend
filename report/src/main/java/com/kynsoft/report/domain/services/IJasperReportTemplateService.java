package com.kynsoft.report.domain.services;

import com.kynsoft.report.domain.dto.JasperReportTemplateDto;
import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IJasperReportTemplateService {
    UUID create(JasperReportTemplateDto object);
    void update(JasperReportTemplateDto object);
    void delete(JasperReportTemplateDto object);
    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria);
    JasperReportTemplateDto findById(UUID id);
    JasperReportTemplateDto findByTemplateCode(String templateCode);

    Long countByCodeAndNotId(String templateCode, UUID id);
}
