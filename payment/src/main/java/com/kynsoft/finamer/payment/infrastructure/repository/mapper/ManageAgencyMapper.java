package com.kynsoft.finamer.payment.infrastructure.repository.mapper;

import com.kynsof.share.core.infrastructure.repository.IndexRef;
import com.kynsof.share.core.infrastructure.repository.MapperUtils;
import com.kynsof.share.core.infrastructure.repository.TupleMapper;
import com.kynsoft.finamer.payment.infrastructure.identity.*;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ManageAgencyMapper implements TupleMapper<ManageAgency> {

    private final ManageAgencyTypeMapper manageAgencyTypeMapper;
    private final ManageClientMapper manageClientMapper;
    private final ManageCountryMapper manageCountryMapper;
    private final int FIELDS_COUNT;

    public ManageAgencyMapper(ManageAgencyTypeMapper manageAgencyTypeMapper,
                              ManageClientMapper manageClientMapper,
                              ManageCountryMapper manageCountryMapper){
        this.manageAgencyTypeMapper = manageAgencyTypeMapper;
        this.manageClientMapper = manageClientMapper;
        this.manageCountryMapper = manageCountryMapper;
        this.FIELDS_COUNT =  6 + manageAgencyTypeMapper.totalFields()
                            + manageClientMapper.totalFields()
                            + manageCountryMapper.totalFields();
    }
    @Override
    public ManageAgency map(Tuple tuple, IndexRef index) {
        return (tuple.get(index.get(), UUID.class) != null) ? new ManageAgency(
                tuple.get(index.next(), UUID.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class),
                tuple.get(index.next(), String.class),
                manageAgencyTypeMapper.map(tuple, index),
                manageClientMapper.map(tuple, index),
                manageCountryMapper.map(tuple, index),
                tuple.get(index.next(), LocalDateTime.class),
                tuple.get(index.next(), LocalDateTime.class)
        ) : MapperUtils.skip(index, totalFields());
    }

    @Override
    public int totalFields() {
        return this.FIELDS_COUNT;
    }
}
