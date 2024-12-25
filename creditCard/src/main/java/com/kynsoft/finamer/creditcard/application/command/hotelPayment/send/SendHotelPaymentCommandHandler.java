package com.kynsoft.finamer.creditcard.application.command.hotelPayment.send;

import com.kynsof.share.core.domain.bus.command.ICommandHandler;
import com.kynsof.share.core.domain.kafka.entity.hotelPayment.ReplicateHotelPaymentKafka;
import com.kynsoft.finamer.creditcard.domain.dto.HotelPaymentDto;
import com.kynsoft.finamer.creditcard.domain.services.IHotelPaymentService;
import com.kynsoft.finamer.creditcard.infrastructure.services.kafka.producer.hotelPayment.ProducerHotelPaymentService;
import org.springframework.stereotype.Component;

@Component
public class SendHotelPaymentCommandHandler implements ICommandHandler<SendHotelPaymentCommand> {

    private final IHotelPaymentService hotelPaymentService;
    private final ProducerHotelPaymentService producerHotelPaymentService;

    public SendHotelPaymentCommandHandler(IHotelPaymentService hotelPaymentService, ProducerHotelPaymentService producerHotelPaymentService) {
        this.hotelPaymentService = hotelPaymentService;
        this.producerHotelPaymentService = producerHotelPaymentService;
    }

    @Override
    public void handle(SendHotelPaymentCommand command) {
        HotelPaymentDto hotelPaymentDto = this.hotelPaymentService.findById(command.getId());
        this.producerHotelPaymentService.producer(new ReplicateHotelPaymentKafka(
                hotelPaymentDto.getId(),
                hotelPaymentDto.getHotelPaymentId(),
                hotelPaymentDto.getTransactionDate(),
                hotelPaymentDto.getManageHotel().getId(),
                hotelPaymentDto.getManageBankAccount().getId(),
                hotelPaymentDto.getAmount(),
                hotelPaymentDto.getCommission(),
                hotelPaymentDto.getNetAmount(),
                hotelPaymentDto.getStatus().getId(),
                hotelPaymentDto.getRemark()
        ));
    }
}
