package com.kynsoft.finamer.payment.infrastructure.services.http;

import com.kynsof.share.core.domain.exception.BusinessNotFoundException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsof.share.core.domain.exception.GlobalBusinessException;
import com.kynsof.share.core.domain.http.entity.InvoiceHttp;
import com.kynsof.share.core.domain.response.ErrorField;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class InvoiceHttpUUIDService {

    private final RestTemplate restTemplate;

//    @Value("${booking.invoice.service:http://localhost:9199}")
    @Value("${booking.invoice.service:http://invoicing.finamer.svc.cluster.local:9909}")
    private String serviceUrl;

    public InvoiceHttpUUIDService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public InvoiceHttp sendGetBookingHttpRequest(UUID id) {
        try {
            String url = serviceUrl + "/api/manage-invoice/uuid-id/" + id;

            // Crear cabeceras para la solicitud
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Crear la entidad de la solicitud con el cuerpo (request) y las cabeceras
            HttpEntity<UUID> entity = new HttpEntity<>(id, headers);

            // Enviar la solicitud POST al endpoint del controlador
            ResponseEntity<InvoiceHttp> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, InvoiceHttp.class);

            if (!HttpStatus.OK.equals(response.getStatusCode())) {
                throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.INVOICE_NOT_FOUND_, new ErrorField("id", DomainErrorMessage.INVOICE_NOT_FOUND_.getReasonPhrase())));
            }
            return response.getBody();
        } catch (RestClientException e) {
            throw new BusinessNotFoundException(new GlobalBusinessException(DomainErrorMessage.INVOICE_NOT_FOUND_, new ErrorField("id", DomainErrorMessage.INVOICE_NOT_FOUND_.getReasonPhrase())));
        }
    }
}
