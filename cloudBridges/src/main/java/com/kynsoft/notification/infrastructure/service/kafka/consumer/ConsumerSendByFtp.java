package com.kynsoft.notification.infrastructure.service.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kynsof.share.core.domain.response.FileDto;
import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.notification.application.command.sendByFtp.SendByFtpCommand;
import com.kynsoft.notification.application.command.sendByFtp.SendByFtpRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerSendByFtp {
    private static final Logger logger = Logger.getLogger(ConsumerSendByFtp.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final IMediator mediator;

    public ConsumerSendByFtp(IMediator mediator) {
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

            SendByFtpRequest sendByFtpRequest = new SendByFtpRequest();
            sendByFtpRequest.setServer(b2bPartnerData.get("ip"));
            sendByFtpRequest.setUserName(b2bPartnerData.get("userName"));
            sendByFtpRequest.setPassword(b2bPartnerData.get("password"));
            sendByFtpRequest.setUrl(b2bPartnerData.get("url"));
            sendByFtpRequest.setFileDtos(fileDtos);

            SendByFtpCommand command = SendByFtpCommand.fromRequest(sendByFtpRequest);
            mediator.send(command);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Error processing Kafka message", e);
        }
    }
}
