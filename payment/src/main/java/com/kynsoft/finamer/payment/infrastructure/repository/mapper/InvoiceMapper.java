package com.kynsoft.finamer.payment.infrastructure.repository.mapper;

import com.kynsof.share.core.infrastructure.repository.IndexRef;
import com.kynsof.share.core.infrastructure.repository.MapperUtils;
import com.kynsof.share.core.infrastructure.repository.TupleMapper;
import com.kynsoft.finamer.payment.domain.dtoEnum.EInvoiceType;
import com.kynsoft.finamer.payment.infrastructure.identity.Invoice;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class InvoiceMapper implements TupleMapper<Invoice> {

    private final ManageHotelMapper manageHotelMapper;
    private final ManageAgencyMapper manageAgencyMapper;
    private final ManageInvoiceStatusMapper manageInvoiceStatusMapper;
    private static final int BASE_FIELDS_COUNT = 10;

    public InvoiceMapper(ManageHotelMapper manageHotelMapper,
                         ManageAgencyMapper manageAgencyMapper,
                         ManageInvoiceStatusMapper manageInvoiceStatusMapper){
        this.manageHotelMapper = manageHotelMapper;
        this.manageAgencyMapper = manageAgencyMapper;
        this.manageInvoiceStatusMapper = manageInvoiceStatusMapper;
    }

    @Override
    public Invoice map(Tuple tuple, IndexRef index) {
        return (tuple.get(index.get(), UUID.class) != null) ? new Invoice(
                tuple.get(index.next(), UUID.class),
                tuple.get(index.next(), Long.class),
                tuple.get(index.next(), Long.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), Double.class),
                tuple.get(index.next(), Double.class),
                tuple.get(index.next(), LocalDateTime.class),
                tuple.get(index.next(), Boolean.class),
                null,//parent
                tuple.get(index.next(), EInvoiceType.class),
                null,
                this.manageHotelMapper.map(tuple, index),
                this.manageAgencyMapper.map(tuple, index),
                tuple.get(index.next(), Boolean.class),
                this.manageInvoiceStatusMapper.map(tuple, index)
        ) : MapperUtils.skip(index, totalFields(false));
    }

    @Override
    public Invoice map(Tuple tuple, IndexRef index, boolean includeParent) {
        return (tuple.get(index.get(), UUID.class) != null) ? new Invoice(
                tuple.get(index.next(), UUID.class),
                tuple.get(index.next(), Long.class),
                tuple.get(index.next(), Long.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), Double.class),
                tuple.get(index.next(), Double.class),
                tuple.get(index.next(), LocalDateTime.class),
                tuple.get(index.next(), Boolean.class),
                includeParent ? this.map(tuple, index) : null,//parent
                tuple.get(index.next(), EInvoiceType.class),
                null,
                this.manageHotelMapper.map(tuple, index),
                this.manageAgencyMapper.map(tuple, index),
                tuple.get(index.next(), Boolean.class),
                this.manageInvoiceStatusMapper.map(tuple, index)
        ) : MapperUtils.skip(index, totalFields());
    }

    @Override
    public int totalFields() {
        return totalFields(true);
    }

    public int totalFields(boolean includeParent) {
        int baseCount = BASE_FIELDS_COUNT
                + manageHotelMapper.totalFields()
                + manageAgencyMapper.totalFields()
                + manageInvoiceStatusMapper.totalFields();

        return includeParent ? baseCount + baseCount : baseCount;
    }
}
