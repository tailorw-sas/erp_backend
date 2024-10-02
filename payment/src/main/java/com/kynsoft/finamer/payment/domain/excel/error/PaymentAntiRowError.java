package com.kynsoft.finamer.payment.domain.excel.error;

import com.kynsof.share.core.domain.response.ErrorField;
import com.kynsoft.finamer.payment.domain.excel.bean.detail.AntiToIncomeRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@RedisHash(value = "paymentantierror",timeToLive = 3600)
public class PaymentAntiRowError implements Serializable {

    @Id
    private String id;
    private int rowNumber;
    @Indexed
    private String importProcessId;
    private List<ErrorField> errorFields;
    private AntiToIncomeRow row;
}
