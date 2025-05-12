package com.kynsoft.finamer.payment.infrastructure.repository.mapper;

import com.kynsof.share.core.infrastructure.repository.IndexRef;
import com.kynsof.share.core.infrastructure.repository.MapperUtils;
import com.kynsof.share.core.infrastructure.repository.TupleMapper;
import com.kynsoft.finamer.payment.infrastructure.identity.Booking;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class BookingMapper implements TupleMapper<Booking> {

    private final InvoiceMapper invoiceMapper;
    private static final int BASE_FIELDS_COUNT = 14;

    public BookingMapper(InvoiceMapper invoiceMapper){
        this.invoiceMapper = invoiceMapper;
    }


    @Override
    public Booking map(Tuple tuple, IndexRef index) {
        return (tuple.get(index.get(), UUID.class) != null) ? new Booking(
                tuple.get(index.next(), UUID.class),
                tuple.get(index.next(), Long.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), LocalDateTime.class),
                tuple.get(index.next(), LocalDateTime.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), Double.class),
                tuple.get(index.next(), Double.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), Integer.class),
                tuple.get(index.next(), Integer.class),
                null,
                null,
                tuple.get(index.next(), LocalDateTime.class)
        ) : MapperUtils.skip(index, totalFields(false));
    }

    @Override
    public Booking map(Tuple tuple, IndexRef index, boolean includeParent) {
        return (tuple.get(index.get(), UUID.class) != null) ? new Booking(
                tuple.get(index.next(), UUID.class),
                tuple.get(index.next(), Long.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), LocalDateTime.class),
                tuple.get(index.next(), LocalDateTime.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), Double.class),
                tuple.get(index.next(), Double.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), Integer.class),
                tuple.get(index.next(), Integer.class),
                this.invoiceMapper.map(tuple, index, true),
                includeParent ? this.map(tuple, index) : null,
                tuple.get(index.next(), LocalDateTime.class)
        ) : MapperUtils.skip(index, totalFields()) ;
    }

    @Override
    public int totalFields() {
        return totalFields(true);
    }

    public int totalFields(boolean includeParent) {
        int baseCount = BASE_FIELDS_COUNT;

        return includeParent ? baseCount + + this.invoiceMapper.totalFields(false) : baseCount;
    }
}
