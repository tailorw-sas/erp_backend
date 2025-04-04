package com.kynsoft.finamer.creditcard.infrastructure.repository.query;

import com.kynsoft.finamer.creditcard.infrastructure.identity.ManageLanguage;
import com.kynsoft.finamer.creditcard.infrastructure.identity.MerchantLanguageCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MerchantLanguageCodeReadDataJPARepository extends JpaRepository<MerchantLanguageCode, UUID>,
        JpaSpecificationExecutor<MerchantLanguageCode> {

    Page<MerchantLanguageCode> findAll(Specification specification, Pageable pageable);

    @Query("SELECT COUNT(m) FROM MerchantLanguageCode m WHERE m.manageMerchant.id = :manageMerchant AND m.manageLanguage.id = :manageLanguage AND m.id <> :id")
    Long countByManageMerchantAndMerchantLanguageAndNotId(@Param("manageMerchant") UUID manageMerchant, @Param("manageLanguage") UUID manageLanguage, @Param("id") UUID id);

    @Query("SELECT m.manageLanguage FROM MerchantLanguageCode m WHERE m.manageMerchant.id = :merchantId")
    List<ManageLanguage> findManageLanguageByMerchantId(@Param("merchantId") UUID merchantId);

    @Query("SELECT m.merchantLanguage FROM MerchantLanguageCode m WHERE m.manageMerchant.id = :merchantId AND m.manageLanguage.id = :languageId")
    Optional<String> findMerchantLanguageByMerchantIdAndLanguageId(@Param("merchantId") UUID merchantId, @Param("languageId") UUID languageId);
}
