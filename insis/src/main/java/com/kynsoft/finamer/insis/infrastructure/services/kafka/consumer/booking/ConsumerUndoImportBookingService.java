package com.kynsoft.finamer.insis.infrastructure.services.kafka.consumer.booking;

import com.kynsof.share.core.infrastructure.bus.IMediator;
import com.kynsoft.finamer.insis.application.command.booking.undoImportBooking.UndoImportBookingCommand;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumerUndoImportBookingService {
    private final IMediator mediator;

    public ConsumerUndoImportBookingService(IMediator mediator){
        this.mediator = mediator;
    }

    @KafkaListener(topics = "finamer-undo-import-innsist-response", groupId = "finamer-innsist-replica")
    public void listen(UUID invoice){
        try{
            System.out.println("************El ID del booking es: "+invoice);
            UndoImportBookingCommand command = new UndoImportBookingCommand(invoice);
            mediator.send(command);
        }catch (Exception ex){
            Logger.getLogger(ConsumerUndoImportBookingService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
