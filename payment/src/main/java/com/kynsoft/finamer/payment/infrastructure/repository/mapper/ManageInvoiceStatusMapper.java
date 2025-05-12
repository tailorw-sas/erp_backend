package com.kynsoft.finamer.payment.infrastructure.repository.mapper;

import com.kynsof.share.core.infrastructure.repository.IndexRef;
import com.kynsof.share.core.infrastructure.repository.MapperUtils;
import com.kynsof.share.core.infrastructure.repository.TupleMapper;
import com.kynsoft.finamer.payment.infrastructure.identity.ManageInvoiceStatus;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ManageInvoiceStatusMapper implements TupleMapper<ManageInvoiceStatus> {

    private final int FIELDS_COUNT;

    public ManageInvoiceStatusMapper(){
        this.FIELDS_COUNT = 3;
    }

    @Override
    public ManageInvoiceStatus map(Tuple tuple, IndexRef index) {
        return (tuple.get(index.get(), UUID.class) != null) ? new ManageInvoiceStatus(
                tuple.get(index.next(), UUID.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class)
        ) : MapperUtils.skip(index, totalFields());
    }

    @Override
    public int totalFields() {
        return this.FIELDS_COUNT;
    }
}
