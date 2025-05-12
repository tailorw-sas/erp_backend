package com.kynsoft.finamer.payment.infrastructure.repository.mapper;

import com.kynsof.share.core.infrastructure.repository.IndexRef;
import com.kynsof.share.core.infrastructure.repository.MapperUtils;
import com.kynsof.share.core.infrastructure.repository.TupleMapper;
import com.kynsoft.finamer.payment.infrastructure.identity.ManageCountry;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ManageCountryMapper implements TupleMapper<ManageCountry> {

    private final ManageLanguageMapper languageMapper;

    private final int FIELDS_COUNT;

    public ManageCountryMapper(ManageLanguageMapper languageMapper){
        this.languageMapper = languageMapper;
        this.FIELDS_COUNT = 10 + languageMapper.totalFields();
    }

    @Override
    public ManageCountry map(Tuple tuple, IndexRef index) {
        return (tuple.get(index.get(), UUID.class) != null) ? new ManageCountry(
                tuple.get(index.next(), UUID.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), Boolean.class),
                tuple.get(index.next(), String.class),
                languageMapper.map(tuple, index),
                tuple.get(index.next(), LocalDateTime.class),
                tuple.get(index.next(), LocalDateTime.class),
                tuple.get(index.next(), LocalDateTime.class),
                tuple.get(index.next(), String.class)
        ) : MapperUtils.skip(index, this.totalFields());
    }

    @Override
    public int totalFields() {
        return FIELDS_COUNT;
    }
}
