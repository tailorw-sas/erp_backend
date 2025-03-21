package com.kynsoft.finamer.creditcard.domain.services;

import com.kynsof.share.core.domain.request.FilterCriteria;
import com.kynsof.share.core.domain.response.PaginatedResponse;
import com.kynsoft.finamer.creditcard.application.query.transaction.search.TransactionTotalResume;
import com.kynsoft.finamer.creditcard.domain.dto.ManagerMerchantConfigDto;
import com.kynsoft.finamer.creditcard.domain.dto.TransactionDto;
import com.kynsoft.finamer.creditcard.domain.dtoEnum.ETransactionStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ITransactionService {

    TransactionDto create(TransactionDto dto);

    void update(TransactionDto dto);

    TransactionDto findByUuid(UUID uuid);

    void delete(TransactionDto dto);

    TransactionDto findById(Long id);

    PaginatedResponse search(Pageable pageable, List<FilterCriteria> filterCriteria, UUID employeeId);

    /*PaginatedResponse searchData(Pageable pageable, List<FilterCriteria> filterCriteria);*/

    Long countByReservationNumberAndManageHotelIdAndNotId(String reservationNumber, UUID hotel);

    boolean compareParentTransactionAmount(Long parentId, Double amount);

    Double findSumOfAmountByParentId(Long parentId);

    void sendTransactionConfirmationVoucherEmail(TransactionDto transactionDto, ManagerMerchantConfigDto merchantConfigDto, String responseCodeMessage, byte[] attachment);

    void sendTransactionPaymentLinkEmail(TransactionDto transactionDto, String paymentLink);

    TransactionTotalResume searchTotal(List<FilterCriteria> filterCriteria);

    Set<TransactionDto> changeAllTransactionStatus(Set<Long> transactionIds, ETransactionStatus status, UUID employeeId);
}
