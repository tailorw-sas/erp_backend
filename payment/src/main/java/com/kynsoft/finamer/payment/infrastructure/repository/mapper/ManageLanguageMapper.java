package com.kynsoft.finamer.payment.infrastructure.repository.mapper;

import com.kynsof.share.core.infrastructure.repository.IndexRef;
import com.kynsof.share.core.infrastructure.repository.MapperUtils;
import com.kynsof.share.core.infrastructure.repository.TupleMapper;
import com.kynsoft.finamer.payment.infrastructure.identity.ManageLanguage;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ManageLanguageMapper implements TupleMapper<ManageLanguage> {

    private final int FIELDS_COUNT;

    public ManageLanguageMapper() {
        FIELDS_COUNT = 7;
    }

    @Override
    public ManageLanguage map(Tuple tuple, IndexRef index) {
        return (tuple.get(index.get(), UUID.class) != null) ? new ManageLanguage(
                tuple.get(index.next(), UUID.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), Boolean.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), LocalDateTime.class),
                tuple.get(index.next(), LocalDateTime.class)
        ) : MapperUtils.skip(index, this.totalFields());
    }

    @Override
    public int totalFields() {
        return FIELDS_COUNT;
    }
}
