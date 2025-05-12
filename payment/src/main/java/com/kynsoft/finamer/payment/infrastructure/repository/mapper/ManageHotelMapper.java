package com.kynsoft.finamer.payment.infrastructure.repository.mapper;

import com.kynsof.share.core.infrastructure.repository.IndexRef;
import com.kynsof.share.core.infrastructure.repository.TupleMapper;
import com.kynsof.share.core.infrastructure.repository.MapperUtils;
import com.kynsoft.finamer.payment.infrastructure.identity.ManageHotel;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ManageHotelMapper implements TupleMapper<ManageHotel> {

    private final int FIELDS_COUNT = 11;

   @Override
    public ManageHotel map(Tuple tuple, IndexRef index) {
       return (tuple.get(index.get(), UUID.class) != null) ? new ManageHotel(
               tuple.get(index.next(), UUID.class),
               tuple.get(index.next(), String.class),
               tuple.get(index.next(), Boolean.class),
               tuple.get(index.next(), String.class),
               tuple.get(index.next(), String.class),
               tuple.get(index.next(), Boolean.class),
               tuple.get(index.next(), UUID.class),
               tuple.get(index.next(), Boolean.class),
               tuple.get(index.next(), LocalDateTime.class),
               tuple.get(index.next(), LocalDateTime.class),
               tuple.get(index.next(), LocalDateTime.class)
       ) : MapperUtils.skip(index, FIELDS_COUNT);
    }

    @Override
    public int totalFields() {
        return FIELDS_COUNT;
    }
}
