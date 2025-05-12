package com.kynsoft.finamer.payment.infrastructure.repository.mapper;

import com.kynsof.share.core.infrastructure.repository.IndexRef;
import com.kynsof.share.core.infrastructure.repository.MapperUtils;
import com.kynsof.share.core.infrastructure.repository.TupleMapper;
import com.kynsoft.finamer.payment.infrastructure.identity.ManageClient;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ManageClientMapper implements TupleMapper<ManageClient> {

    private final int FIELDS_COUNT;

    public ManageClientMapper(){
        this.FIELDS_COUNT = 4;
    }

    @Override
    public ManageClient map(Tuple tuple, IndexRef index) {
        return (tuple.get(index.get(), UUID.class) != null) ? new ManageClient(
                tuple.get(index.next(), UUID.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class)
        ) : MapperUtils.skip(index, totalFields());
    }

    @Override
    public int totalFields() {
        return FIELDS_COUNT;
    }
}
