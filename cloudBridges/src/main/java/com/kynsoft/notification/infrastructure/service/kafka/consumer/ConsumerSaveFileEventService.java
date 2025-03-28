package com.kynsoft.notification.infrastructure.service.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kynsof.share.core.domain.kafka.entity.FileKafka;
import com.kynsof.share.core.domain.kafka.event.EventType;
import com.kynsof.share.core.domain.response.FileDto;
import com.kynsof.share.core.domain.service.IAmazonClient;
import com.kynsoft.notification.domain.service.IAFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerSaveFileEventService {

    @Autowired
    private IAmazonClient amazonClient;

    @Autowired
    private IAFileService fileService;

    // Ejemplo de un método listener
    @KafkaListener(topics = "finamer-file", groupId = "file")
    public void listen(String event) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(event);

            FileKafka eventRead = objectMapper.treeToValue(rootNode.get("data"), FileKafka.class);
            EventType eventType = objectMapper.treeToValue(rootNode.get("type"), EventType.class);

            if (eventType.equals(EventType.CREATED)) {
                try {
                    String fileUrl = amazonClient.save(eventRead.getFile(), eventRead.getFileName(),null);
                    this.fileService.create(new FileDto(eventRead.getId(), eventRead.getFileName(),
                            eventRead.getMicroServiceName(), fileUrl, false, null, null));
                } catch (IOException ex) {
                    Logger.getLogger(ConsumerSaveFileEventService.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            if (eventType.equals(EventType.DELETED)) {
                    FileDto deleteFile = this.fileService.findById(eventRead.getId());
                    amazonClient.delete(deleteFile.getUrl());
                    this.fileService.delete(deleteFile);

            }

        } catch (JsonProcessingException ex) {
            Logger.getLogger(ConsumerSaveFileEventService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
