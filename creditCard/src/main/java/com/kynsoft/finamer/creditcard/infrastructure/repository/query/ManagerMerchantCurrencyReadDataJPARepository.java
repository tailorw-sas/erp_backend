package com.kynsoft.finamer.creditcard.infrastructure.repository.query;

import com.kynsoft.finamer.creditcard.infrastructure.identity.ManagerMerchantCurrency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ManagerMerchantCurrencyReadDataJPARepository extends JpaRepository<ManagerMerchantCurrency, UUID>,
        JpaSpecificationExecutor<ManagerMerchantCurrency> {

    Page<ManagerMerchantCurrency> findAll(Specification specification, Pageable pageable);

    @Query("SELECT COUNT(b) FROM ManagerMerchantCurrency b WHERE b.managerMerchant.id = :managerMerchant AND b.managerCurrency.id = :managerCurrency")
    Long countByManagerMerchantAndManagerCurrency(@Param("managerMerchant") UUID managerMerchant, @Param("managerCurrency") UUID managerCurrency);

    @Query("SELECT COUNT(b) FROM ManagerMerchantCurrency b WHERE b.managerMerchant.id = :managerMerchant AND b.managerCurrency.id = :managerCurrency AND b.id <> :id")
    Long countByManagerMerchantAndManagerCurrencyNotId(@Param("id") UUID id, @Param("managerMerchant") UUID managerMerchant, @Param("managerCurrency") UUID managerCurrency);

}
