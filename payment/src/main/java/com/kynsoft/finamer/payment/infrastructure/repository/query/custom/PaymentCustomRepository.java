package com.kynsoft.finamer.payment.infrastructure.repository.query.custom;

import com.kynsoft.finamer.payment.infrastructure.identity.Payment;
import com.kynsoft.finamer.payment.infrastructure.identity.projection.PaymentSearchProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentCustomRepository {

    Optional<Payment> findByIdCustom(UUID id);

    List<Payment> findAllByPaymentIdCustom(List<Long> ids);

    /**
     * Método para realizar una consulta con proyección de Payment,
     * aplicando Specification (filtros) y paginación.
     *
     * @param specification filtros dinámicos
     * @param pageable paginación y orden
     * @return página de PaymentSearchProjection
     */
    Page<PaymentSearchProjection> findAllProjected(Specification<Payment> specification, Pageable pageable);
}
