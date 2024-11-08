package com.kynsoft.report.controller;

import com.kynsoft.report.applications.command.generateTemplate.GenerateTemplateCommand;
import com.kynsoft.report.applications.command.generateTemplate.GenerateTemplateMessage;
import com.kynsoft.report.applications.command.generateTemplate.GenerateTemplateRequest;
import com.kynsoft.report.applications.query.reportTemplate.GetReportParameterByCodeQuery;
import com.kynsoft.report.applications.query.reportTemplate.GetReportParameterByCodeResponse;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.report.domain.dto.JasperReportTemplateDto;
import com.kynsoft.report.domain.services.IJasperReportTemplateService;
import com.kynsoft.report.infrastructure.services.ReportService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final IMediator mediator;
    private final IJasperReportTemplateService reportService;

    public ReportController(IMediator mediator, IJasperReportTemplateService reportService) {
        this.mediator = mediator;
        this.reportService = reportService;
    }

    @PostMapping(value = "/generate", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePdfReport(@RequestBody ReportRequest reportRequest) {
        try {
            JasperReportTemplateDto reportTemplateDto = reportService.findByTemplateCode(reportRequest.getReportCode());
            // Crear un DataSource personalizado con los datos de conexión
            DriverManagerDataSource customDataSource = new DriverManagerDataSource();
            customDataSource.setDriverClassName("org.postgresql.Driver"); // Cambia el driver según tu base de datos
            customDataSource.setUrl(reportTemplateDto.getDbConectionDto().getUrl()); // URL de tu base de datos
            //customDataSource.setUrl("jdbc:postgresql://172.20.41.100:5432/finamer-payments"); // URL de tu base de datos
          //  customDataSource.setUrl("jdbc:postgresql://postgres-erp-rw.postgres.svc.cluster.local:5432/finamer-payments");
            customDataSource.setUsername("finamer_rw"); // Usuario
            customDataSource.setPassword("5G30y1cXz89cA1yc0gCE3OhhBLQkvUTV2icqz5qNRQGq4cbM5F0bc"); // Contraseña

            // Cargar el archivo JRXML desde la URL proporcionada
            JasperReport jasperReport = loadJasperReportFromUrl(reportTemplateDto.getFile());

            // Llenar el reporte con la fuente de datos y parámetros
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportRequest.getParameters(), customDataSource.getConnection());

            // Exportar a PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            // Preparar la respuesta HTTP
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            throw new RuntimeException("Error generating report", e);
        }
    }

    private JasperReport loadJasperReportFromUrl(String templateUrl) throws JRException {
        try (InputStream inputStream = new URL(templateUrl).openStream()) {
            return JasperCompileManager.compileReport(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error loading JRXML template from URL: " + templateUrl, e);
        }
    }

    @PostMapping(value = "/generate-template", produces = {MediaType.APPLICATION_PDF_VALUE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"})
    public ResponseEntity<?> getTemplate(@RequestBody GenerateTemplateRequest request) {
        try {
            GenerateTemplateCommand command = new GenerateTemplateCommand(request.getParameters(),
                    request.getJasperReportCode(), request.getReportFormatType());
            GenerateTemplateMessage response = mediator.send(command);

            // Verificar si el resultado es nulo o vacío
            if (response == null || response.getResult() == null || response.getResult().length == 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al generar el reporte.");
            }

            String contentType;
            String fileName;
            if ("XLS".equalsIgnoreCase(request.getReportFormatType())) {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileName = "report.xlsx";
            } else {
                contentType = MediaType.APPLICATION_PDF_VALUE;
                fileName = "report.pdf";
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(response.getResult());
        } catch (Exception e) {
            // Manejar errores y responder adecuadamente
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la solicitud.");
        }
    }

    @GetMapping("/parameters/{reportCode}")
    public ResponseEntity<?> getReportParameters(@PathVariable String reportCode) {
        GetReportParameterByCodeQuery query = new GetReportParameterByCodeQuery(reportCode);
        GetReportParameterByCodeResponse response = mediator.send(query);
        return ResponseEntity.ok(response);
    }
}
