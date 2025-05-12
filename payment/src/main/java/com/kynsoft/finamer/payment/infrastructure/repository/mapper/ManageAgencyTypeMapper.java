package com.kynsoft.finamer.payment.infrastructure.repository.mapper;

import com.kynsof.share.core.infrastructure.repository.IndexRef;
import com.kynsof.share.core.infrastructure.repository.MapperUtils;
import com.kynsof.share.core.infrastructure.repository.TupleMapper;
import com.kynsoft.finamer.payment.infrastructure.identity.ManageAgencyType;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ManageAgencyTypeMapper implements TupleMapper<ManageAgencyType> {

    private final int FIELDS_COUNT;

    public ManageAgencyTypeMapper(){
        this.FIELDS_COUNT = 4;
    }
    @Override
    public ManageAgencyType map(Tuple tuple, IndexRef index) {
        return (tuple.get(index.get(), UUID.class) != null) ? new ManageAgencyType(
                tuple.get(index.next(), UUID.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class)
        ) : MapperUtils.skip(index, totalFields());
    }

    @Override
    public int totalFields() {
        return this.FIELDS_COUNT;
    }
}
