package com.kynsoft.notification.infrastructure.service.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.kynsof.share.core.domain.response.FileDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProducerSendByFtpResponse {
    private final KafkaTemplate<String, Object> producer;
    private static final Logger logger = Logger.getLogger(ProducerSendByFtpResponse.class.getName());

    public ProducerSendByFtpResponse(KafkaTemplate<String, Object> producer) {
        this.producer = producer;
    }

    @Async
    public void create(List<FileDto> fileDtos) {
        if (fileDtos == null || fileDtos.isEmpty()) {
            logger.log(Level.WARNING, "No data to send to Kafka");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            int systemLoad = Runtime.getRuntime().availableProcessors();
            int batchSize = Math.max(10, systemLoad * 5);

            List<List<FileDto>> batches = Lists.partition(fileDtos, batchSize);

            List<CompletableFuture<Void>> batchFutures = batches.stream()
                    .map(batch -> CompletableFuture.runAsync(() -> {
                        try {
                            List<Map<String, Object>> transformedBatch = batch.stream()
                                    .map(fileDto -> Map.of(
                                            "id", fileDto.getId(),
                                            "response", fileDto.getUploadFileResponse()
                                    ))
                                    .toList();

                            String jsonMessage = objectMapper.writeValueAsString(Map.of("files", transformedBatch));
                            producer.send("finamer-send-by-ftp-response-topic", jsonMessage);

                        } catch (JsonProcessingException e) {
                            logger.log(Level.SEVERE, "Error serializing batch message: {0}", e.getMessage());
                        }
                    }))
                    .toList();

            CompletableFuture.allOf(batchFutures.toArray(new CompletableFuture[0])).join();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error in Kafka batch processing", ex);
        }
    }
}
