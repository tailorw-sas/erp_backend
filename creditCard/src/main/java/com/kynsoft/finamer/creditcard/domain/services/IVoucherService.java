package com.kynsoft.finamer.creditcard.domain.services;

import com.kynsoft.finamer.creditcard.domain.dto.ManagerMerchantConfigDto;
import com.kynsoft.finamer.creditcard.domain.dto.TransactionDto;

public interface IVoucherService {

    byte[] createAndUploadAndAttachTransactionVoucher(TransactionDto transactionDto, ManagerMerchantConfigDto merchantConfigDto, String employee);
}
