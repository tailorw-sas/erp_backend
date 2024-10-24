package com.kynsoft.finamer.creditcard.infrastructure.services;

import com.kynsof.share.core.domain.exception.BusinessException;
import com.kynsof.share.core.domain.exception.DomainErrorMessage;
import com.kynsoft.finamer.creditcard.application.query.objectResponse.CardNetSessionResponse;
import com.kynsoft.finamer.creditcard.domain.dto.*;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.Method;
import com.kynsoft.finamer.creditcard.domain.services.ICardNetJobService;
import com.kynsoft.finamer.creditcard.domain.services.IFormPaymentService;
import com.kynsoft.finamer.creditcard.domain.services.IMerchantLanguageCodeService;
import com.kynsoft.finamer.creditcard.infrastructure.identity.Transaction;
import com.kynsoft.finamer.creditcard.infrastructure.identity.TransactionPaymentLogs;
import com.kynsoft.finamer.creditcard.infrastructure.repository.command.ManageTransactionsRedirectLogsWriteDataJPARepository;
import com.kynsoft.finamer.creditcard.infrastructure.repository.query.TransactionPaymentLogsReadDataJPARepository;
import com.kynsoft.finamer.creditcard.infrastructure.repository.query.TransactionReadDataJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class FormPaymentServiceImpl implements IFormPaymentService {
    @Value("${redirect.private.key}")
    private String privateKey;

    @Autowired
    private final ManageTransactionsRedirectLogsWriteDataJPARepository repositoryCommand;
    private final TransactionPaymentLogsReadDataJPARepository repositoryQuery;

    private final ICardNetJobService cardNetJobService;

    private final IMerchantLanguageCodeService merchantLanguageCodeService;

    @Autowired
    private final TransactionReadDataJPARepository transactionRepositoryQuery;

    public FormPaymentServiceImpl(ManageTransactionsRedirectLogsWriteDataJPARepository repositoryCommand,
                                  TransactionPaymentLogsReadDataJPARepository repositoryQuery,
                                  ICardNetJobService cardNetJobService, IMerchantLanguageCodeService merchantLanguageCodeService,
                                  TransactionReadDataJPARepository transactionRepositoryQuery){
        this.repositoryCommand = repositoryCommand;
        this.repositoryQuery = repositoryQuery;
        this.cardNetJobService = cardNetJobService;
        this.merchantLanguageCodeService = merchantLanguageCodeService;
        this.transactionRepositoryQuery = transactionRepositoryQuery;
    }

    public ResponseEntity<String> redirectToMerchant(TransactionDto transactionDto, ManagerMerchantConfigDto merchantConfigDto) {

        if (compareDates(transactionDto.getTransactionUuid())) {
            try {
                if (merchantConfigDto.getMethod().equals(Method.AZUL.toString())) {
                    return redirectToAzul(transactionDto, merchantConfigDto);
                } else {
                    return redirectToCardNet(transactionDto, merchantConfigDto);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payment.");
            }
        }
        else throw new BusinessException(DomainErrorMessage.VCC_EXPIRED_PAYMENT_LINK, DomainErrorMessage.VCC_EXPIRED_PAYMENT_LINK.getReasonPhrase());
//        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing transaction date.");
    }

    private ResponseEntity<String> redirectToAzul(TransactionDto transactionDto, ManagerMerchantConfigDto merchantConfigDto) {

        if (compareDates(transactionDto.getTransactionUuid())) {
            try {
                // Extraer los parámetros del objeto PaymentRequest
                //TODO: aquí la idea es que la info del merchant se tome de merchant, b2bparter y merchantConfig
                String merchantId = merchantConfigDto.getMerchantNumber(); //Campo merchantNumber de Merchant Config
                String merchantName = merchantConfigDto.getName(); //Campo name de Merchant Config
                String merchantType = merchantConfigDto.getMerchantType(); //Campo merchantType de Merchant Config
                String currencyCode = transactionDto.getMerchantCurrency().getValue(); //Valor $ por ahora
                String orderNumber = transactionDto.getId().toString(); //Viene en el request

                double amountValue = transactionDto.getAmount() * 100;
                int intValue = (int) Math.round(amountValue);
                String amount = Integer.toString(intValue);

                String itbis = "000"; //Valor 000 por defecto
                String approvedUrl = merchantConfigDto.getSuccessUrl(); //Campo successUrl de Merchant Config
                String declinedUrl = merchantConfigDto.getDeclinedUrl();//Campo declinedUrl de Merchant Config
                String cancelUrl = merchantConfigDto.getErrorUrl();//Campo errorUrl de Merchant Config
                String useCustomField1 = "0";  //Se mantiene asi por defecto
                String customField1Label = "";//Se mantiene asi por defecto
                String customField1Value = "";//Se mantiene asi por defecto
                String useCustomField2 = "0";//Se mantiene asi por defecto
                String customField2Label = "";//Se mantiene asi por defecto
                String customField2Value = "";//Se mantiene asi por defecto

                // Construir el hash de autenticación
                String data = String.join("", merchantId, merchantName, merchantType, currencyCode, orderNumber, amount, itbis, approvedUrl, declinedUrl, cancelUrl,
                        useCustomField1, customField1Label, customField1Value, useCustomField2, customField2Label, customField2Value, privateKey);
                String authHash = createAuthHash(data);

                //obtener el language
                String locale = this.merchantLanguageCodeService.findMerchantLanguageByMerchantIdAndLanguageId(
                        transactionDto.getMerchant().getId(),
                        transactionDto.getLanguage().getId()
                );

                // Generar el formulario HTML
                String htmlForm = "<html lang=\"en\">" +
                        "<head></head>" +
                        "<body>" +
                        "<form action=\"" + merchantConfigDto.getUrl() + "\" method=\"post\" id=\"paymentForm\">" +
                        "<input type=\"hidden\" name=\"MerchantId\" value=\"" + merchantConfigDto.getMerchantNumber() + "\">" +
                        "<input type=\"hidden\" name=\"MerchantName\" value=\"" + merchantConfigDto.getName() + "\">" +
                        "<input type=\"hidden\" name=\"MerchantType\" value=\"" + merchantConfigDto.getMerchantType() + "\">" +
                        "<input type=\"hidden\" name=\"CurrencyCode\" value=\"" + currencyCode + "\">" +
                        "<input type=\"hidden\" name=\"OrderNumber\" value=\"" + orderNumber + "\">" +
                        "<input type=\"hidden\" name=\"Locale\" value=\"" + (locale.isBlank() ? "EN" : locale) + "\">" +
                        "<input type=\"hidden\" name=\"Amount\" value=\"" + amount + "\">" +
                        "<input type=\"hidden\" name=\"ITBIS\" value=\"" + itbis + "\">" +
                        "<input type=\"hidden\" name=\"ApprovedUrl\" value=\"" + approvedUrl + "\">" +
                        "<input type=\"hidden\" name=\"DeclinedUrl\" value=\"" + declinedUrl + "\">" +
                        "<input type=\"hidden\" name=\"CancelUrl\" value=\"" + cancelUrl + "\">" +
                        "<input type=\"hidden\" name=\"UseCustomField1\" value=\"" + useCustomField1 + "\">" +
                        "<input type=\"hidden\" name=\"CustomField1Label\" value=\"" + customField1Label + "\">" +
                        "<input type=\"hidden\" name=\"CustomField1Value\" value=\"" + customField1Value + "\">" +
                        "<input type=\"hidden\" name=\"UseCustomField2\" value=\"" + useCustomField2 + "\">" +
                        "<input type=\"hidden\" name=\"CustomField2Label\" value=\"" + customField2Label + "\">" +
                        "<input type=\"hidden\" name=\"CustomField2Value\" value=\"" + customField2Value + "\">" +
                        "<input type=\"hidden\" name=\"AuthHash\" value=\"" + authHash + "\">" +

                        "</form>" +
                        "<script>document.getElementById('paymentForm').submit();</script>" +
                        "</body>" +
                        "</html>";

                // Devolver el formulario HTML como respuesta
                String concatenatedBody = htmlForm + "{elemento}";
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(concatenatedBody);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payment.");
            }
        }else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing transaction date.");

    }
    // Método para crear el AuthHash
    private String createAuthHash(String data) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
            javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(privateKey.getBytes(), "HmacSHA512");
            mac.init(secretKey);
            byte[] hashBytes = mac.doFinal(data.getBytes());
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating AuthHash", e);
        }
    }

    private ResponseEntity<String> redirectToCardNet(TransactionDto transactionDto, ManagerMerchantConfigDto merchantConfigDto) {
        try {
            // Paso 1: Enviar los datos para generar la sesión
            //TODO: aquí la idea es que la info del merchant se tome de merchant, b2bparter y merchantConfig

            String successUrl = merchantConfigDto.getSuccessUrl();
            String cancelUrl = merchantConfigDto.getErrorUrl();
            CardnetJobDto cardnetJobDto = cardNetJobService.findByTransactionId(transactionDto.getTransactionUuid());
            String cardNetSession = "";
            String cardNetSessionKey = "";

            Map<String, String> requestData = new HashMap<>();
            String amountString = BigDecimal.valueOf(transactionDto.getAmount()).multiply(new BigDecimal(100)).stripTrailingZeros()
                    .toPlainString();
            //obtener el language
            String pageLanguaje = this.merchantLanguageCodeService.findMerchantLanguageByMerchantIdAndLanguageId(
                    transactionDto.getMerchant().getId(),
                    transactionDto.getLanguage().getId()
            );
            String currencyCode = transactionDto.getMerchantCurrency().getValue();

            requestData.put("TransactionType", "0200"); // dejar 0200 por defecto por ahora
            requestData.put("CurrencyCode", currencyCode); // dejar 214 por ahora que es el peso dominicano. El usd es 840
            requestData.put("Tax", "0"); // 0 por defecto
            requestData.put("AcquiringInstitutionCode", merchantConfigDto.getInstitutionCode()); //Campo institutionCode de Merchant Config
            requestData.put("MerchantType", merchantConfigDto.getMerchantType()); //Campo merchantType de Merchant Config
            requestData.put("MerchantNumber", merchantConfigDto.getMerchantNumber()); //Campo merchantNumber de Merchant Config
            requestData.put("MerchantTerminal", merchantConfigDto.getMerchantTerminal()); //Campo merchantTerminal de Merchant Config
//            requestData.put("MerchantTerminal_amex", paymentRequest.getMerchantTerminalAmex()); //No enviar por ahora
            requestData.put("ReturnUrl", successUrl); //Campo successUrl de Merchant Config
            requestData.put("CancelUrl", cancelUrl); //Campo errorUrl de Merchant Config
            requestData.put("PageLanguaje", pageLanguaje.isBlank() ? "ING" : pageLanguaje); //Se envia por ahora ENG
            requestData.put("TransactionId", String.valueOf(transactionDto.getId())); //Viene en el request
            requestData.put("OrdenId", transactionDto.getId().toString()); //Viene en el request
            requestData.put("MerchantName", merchantConfigDto.getName()); //Campo name de Merchant Config
            requestData.put("IpClient", ""); // Campo ip del b2b partner del merchant
            requestData.put("Amount", amountString); //Viene en el request

            // Solo invocar al servicio de obtener sesion si no se ha hecho previamente.
            if (cardnetJobDto == null) {
                CardNetSessionResponse sessionResponse = getCardNetSession(requestData, merchantConfigDto.getAltUrl());
                if (sessionResponse == null || sessionResponse.getSession() == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al generar la sesión.");
                }
                cardNetSession = sessionResponse.getSession();
                cardNetSessionKey = sessionResponse.getSessionKey();
                // Insertar nueva referencia de session.
                cardnetJobDto = new CardnetJobDto(UUID.randomUUID(), transactionDto.getTransactionUuid(), cardNetSession, cardNetSessionKey, Boolean.FALSE, 0);
                cardNetJobService.create(cardnetJobDto);
            } else {
                cardNetSession = cardnetJobDto.getSession();
            }

            // Paso 2: Generar Formulario
            String htmlForm = "<html lang=\"en\">" +
                    "<head></head>" +
                    "<body>" +
                    "<form action=\"" + merchantConfigDto.getUrl() + "\" method=\"post\" id=\"paymentForm\">" +
                    "<input type=\"hidden\" name=\"SESSION\" value=\"" + cardNetSession + "\"/>" +
                    "<input type=\"hidden\" name=\"ReturnUrl\" value=\"" + successUrl + "\"/>" +
                    "<input type=\"hidden\" name=\"CancelUrl\" value=\"" + cancelUrl + "\"/>" +
                    "</form>" +
                    "<script>document.getElementById('paymentForm').submit();</script>" +
                    "</body>" +
                    "</html>";

            String concatenatedBody = htmlForm + requestData;
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(concatenatedBody);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing Cardnet payment.");
        }
    }

    private Boolean compareDates(UUID id) {
        Optional<Transaction> transaction = transactionRepositoryQuery.findByTransactionUuid(id);
        if(transaction.isPresent()) {
            LocalDateTime date1 = transaction.get().getCreatedAt();
            LocalDateTime currentDate = LocalDateTime.now();
            // Calcular la diferencia en minutos
            Duration difernce = Duration.between(date1, currentDate);
            //Comprobar que la diferncia sea menor que una semana
            return difernce.toDays() <= 7;
        } else return false;
    }

    private CardNetSessionResponse getCardNetSession(Map<String, String> requestData, String url) {
        // Enviar la solicitud POST y obtener la respuesta
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestData, headers);

        ResponseEntity<CardNetSessionResponse> sessionResponse = restTemplate.exchange(url, HttpMethod.POST, entity, CardNetSessionResponse.class);

        // Convertir la respuesta en objeto
        return sessionResponse.getBody();
    }

    public UUID create(TransactionPaymentLogsDto dto) {
        TransactionPaymentLogs entity = new TransactionPaymentLogs(dto);
        return this.repositoryCommand.save(entity).getId();
    }

    public void update(TransactionPaymentLogsDto dto) {
        TransactionPaymentLogs entity = new TransactionPaymentLogs(dto);
        entity.setUpdatedAt(LocalDateTime.now());
        repositoryCommand.save(entity);
    }

    public TransactionPaymentLogsDto findByTransactionId(UUID id) {
        Optional<TransactionPaymentLogs> optional = this.repositoryQuery.findByTransactionId(id);
        if(optional.isPresent()){
            return optional.get().toAggregate();
        }else return null;

    }

}