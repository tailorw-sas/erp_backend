package com.kynsoft.notification.infrastructure.service.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kynsof.share.core.domain.response.FileDto;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.notification.application.command.sendByFtp.SendByFtpCommand;
import com.kynsoft.notification.application.command.sendByFtp.SendByFtpRequest;
import com.kynsoft.notification.application.command.sendMailjetEmail.SendMailJetEMailCommand;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerSendInvoice {
    private static final Logger logger = Logger.getLogger(ConsumerSendInvoice.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final IMediator mediator;

    public ConsumerSendInvoice(IMediator mediator) {
        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-send-by-ftp-topic", groupId = "invoice-processing-group")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            String message = record.value();
            logger.info("📥 Received Kafka message: " + message);

            Map<String, Object> data = objectMapper.readValue(message, Map.class);

            List<FileDto> fileDtos = objectMapper.convertValue(data.get("files"), List.class);
            Map<String, String> b2bPartnerData = objectMapper.convertValue(data.get("b2bPartner"), Map.class);

            // Extraer datos de b2BPartner
            String server = b2bPartnerData.get("ip");
            String userName = b2bPartnerData.get("userName");
            String password = b2bPartnerData.get("password");
            String url = b2bPartnerData.get("url");

            SendByFtpCommand command = new SendByFtpCommand(server, userName, password, url, fileDtos);
            mediator.send(command);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Error processing Kafka message", e);
        }
    }
}
