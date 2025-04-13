package com.kynsoft.finamer.payment.infrastructure.repository.query.payments;

import com.kynsoft.finamer.payment.domain.dtoEnum.EAttachment;
import com.kynsoft.finamer.payment.domain.dtoEnum.ImportType;
import com.kynsoft.finamer.payment.domain.dtoEnum.Status;
import com.kynsoft.finamer.payment.infrastructure.identity.Booking;
import com.kynsoft.finamer.payment.infrastructure.identity.ManagePaymentTransactionType;
import com.kynsoft.finamer.payment.infrastructure.identity.Payment;
import com.kynsoft.finamer.payment.infrastructure.identity.PaymentDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PaymentDetailCustomRepositoryImpl implements PaymentDetailCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PaymentDetail> findAllByPaymentId(UUID id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<PaymentDetail> root = query.from(PaymentDetail.class);

        Join<PaymentDetail, Payment> paymentJoin = root.join("payment", JoinType.LEFT);
        Join<PaymentDetail, ManagePaymentTransactionType> managePaymentTransactionTypeJoin = root.join("transactionType", JoinType.LEFT);
        Join<PaymentDetail, Booking> bookingJoin = root.join("manageBooking", JoinType.LEFT);


        query.where(cb.equal(paymentJoin.get("id"), id));

        List<Selection<?>> selections = new ArrayList<>();

        selections.add(root.get("id"));
        selections.add(root.get("status"));
        selections.add(root.get("paymentDetailId"));
        selections.add(root.get("parentId"));

        //Payment
        selections.add(paymentJoin.get("id"));
        selections.add(paymentJoin.get("paymentId"));
        selections.add(paymentJoin.get("status"));
        selections.add(paymentJoin.get("eAttachment"));
        selections.add(paymentJoin.get("reference"));
        selections.add(paymentJoin.get("transactionDate"));
        selections.add(paymentJoin.get("dateTime"));
        selections.add(paymentJoin.get("paymentAmount"));
        selections.add(paymentJoin.get("paymentBalance"));
        selections.add(paymentJoin.get("depositAmount"));
        selections.add(paymentJoin.get("depositBalance"));
        selections.add(paymentJoin.get("otherDeductions"));
        selections.add(paymentJoin.get("identified"));
        selections.add(paymentJoin.get("notIdentified"));
        selections.add(paymentJoin.get("notApplied"));
        selections.add(paymentJoin.get("applied"));
        selections.add(paymentJoin.get("remark"));
        selections.add(paymentJoin.get("applyPayment"));
        selections.add(paymentJoin.get("hasAttachment"));
        selections.add(paymentJoin.get("hasDetailTypeDeposit"));
        selections.add(paymentJoin.get("paymentSupport"));
        selections.add(paymentJoin.get("createByCredit"));
        selections.add(paymentJoin.get("createdAt"));
        selections.add(paymentJoin.get("updatedAt"));
        selections.add(paymentJoin.get("importType"));

        //Manage Payment Transaction Type
        selections.add(managePaymentTransactionTypeJoin.get("id"));
        selections.add(managePaymentTransactionTypeJoin.get("code"));
        selections.add(managePaymentTransactionTypeJoin.get("name"));
        selections.add(managePaymentTransactionTypeJoin.get("status"));
        selections.add(managePaymentTransactionTypeJoin.get("cash"));
        selections.add(managePaymentTransactionTypeJoin.get("deposit"));
        selections.add(managePaymentTransactionTypeJoin.get("applyDeposit"));
        selections.add(managePaymentTransactionTypeJoin.get("remarkRequired"));
        selections.add(managePaymentTransactionTypeJoin.get("minNumberOfCharacter"));
        selections.add(managePaymentTransactionTypeJoin.get("defaultRemark"));
        selections.add(managePaymentTransactionTypeJoin.get("defaults"));
        selections.add(managePaymentTransactionTypeJoin.get("paymentInvoice"));
        selections.add(managePaymentTransactionTypeJoin.get("debit"));
        selections.add(managePaymentTransactionTypeJoin.get("expenseToBooking"));
        selections.add(managePaymentTransactionTypeJoin.get("negative"));

        //Booking
        selections.add(bookingJoin.get("id"));
        selections.add(bookingJoin.get("bookingId"));
        selections.add(bookingJoin.get("reservationNumber"));
        selections.add(bookingJoin.get("checkIn"));
        selections.add(bookingJoin.get("checkOut"));
        selections.add(bookingJoin.get("fullName"));
        selections.add(bookingJoin.get("firstName"));
        selections.add(bookingJoin.get("lastName"));
        selections.add(bookingJoin.get("invoiceAmount"));
        selections.add(bookingJoin.get("amountBalance"));
        selections.add(bookingJoin.get("couponNumber"));
        selections.add(bookingJoin.get("adults"));
        selections.add(bookingJoin.get("children"));
        selections.add(bookingJoin.get("bookingDate"));

        selections.add(root.get("reverseFrom"));
        selections.add(root.get("reverseFromParentId"));
        selections.add(root.get("amount"));
        selections.add(root.get("applyDepositValue"));
        selections.add(root.get("remark"));
        selections.add(root.get("reverseTransaction"));
        selections.add(root.get("canceledTransaction"));
        selections.add(root.get("createByCredit"));
        selections.add(root.get("bookingId"));
        selections.add(root.get("invoiceId"));
        selections.add(root.get("transactionDate"));
        selections.add(root.get("firstName"));
        selections.add(root.get("lastName"));
        selections.add(root.get("reservation"));
        selections.add(root.get("couponNo"));
        selections.add(root.get("adults"));
        selections.add(root.get("children"));
        selections.add(root.get("createdAt"));
        selections.add(root.get("updatedAt"));
        selections.add(root.get("applyPayment"));
        selections.add(root.get("appliedAt"));
        selections.add(root.get("effectiveDate"));

        query.multiselect(selections.toArray(new Selection[0]));

        List<Tuple> tuples = entityManager.createQuery(query).getResultList();

        List<PaymentDetail> details = tuples.stream()
                .map(tuple -> {
                    return new PaymentDetail(
                            tuple.get(0, UUID.class),
                            tuple.get(1, Status.class),
                            tuple.get(2, Long.class),
                            tuple.get(3, Long.class),
                            new Payment(
                                    tuple.get(4, UUID.class),
                                    tuple.get(5, Long.class),
                                    tuple.get(6, Status.class),
                                    tuple.get(7, EAttachment.class),
                                    null,
                                    tuple.get(8, String.class),
                                    tuple.get(9, LocalDate.class),
                                    tuple.get(10, LocalTime.class),
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    Collections.emptyList(),
                                    Collections.emptyList(),
                                    tuple.get(11, Double.class),
                                    tuple.get(12, Double.class),
                                    tuple.get(13, Double.class),
                                    tuple.get(14, Double.class),
                                    tuple.get(15, Double.class),
                                    tuple.get(16, Double.class),
                                    tuple.get(17, Double.class),
                                    tuple.get(18, Double.class),
                                    tuple.get(19, Double.class),
                                    tuple.get(20, String.class),
                                    tuple.get(21, Boolean.class),
                                    tuple.get(22, Boolean.class),
                                    tuple.get(23, Boolean.class),
                                    tuple.get(24, Boolean.class),
                                    tuple.get(25, Boolean.class),
                                    tuple.get(26, OffsetDateTime.class),
                                    tuple.get(27, OffsetDateTime.class),
                                    tuple.get(28, ImportType.class)
                            ),
                            new ManagePaymentTransactionType(
                                    tuple.get(29, UUID.class),
                                    tuple.get(30, String.class),
                                    tuple.get(31, String.class),
                                    tuple.get(32, Status.class),
                                    tuple.get(33, Boolean.class),
                                    tuple.get(34, Boolean.class),
                                    tuple.get(35, Boolean.class),
                                    tuple.get(36, Boolean.class),
                                    tuple.get(37, Integer.class),
                                    tuple.get(38, String.class),
                                    tuple.get(39, Boolean.class),
                                    tuple.get(40, Boolean.class),
                                    tuple.get(41, Boolean.class),
                                    tuple.get(42, Boolean.class),
                                    tuple.get(43, Boolean.class)
                            ),
                            new Booking(
                                    tuple.get(44, UUID.class),
                                    tuple.get(45, Long.class),
                                    tuple.get(46, String.class),
                                    tuple.get(47, LocalDateTime.class),
                                    tuple.get(48, LocalDateTime.class),
                                    tuple.get(49, String.class),
                                    tuple.get(50, String.class),
                                    tuple.get(51, String.class),
                                    tuple.get(52, Double.class),
                                    tuple.get(53, Double.class),
                                    tuple.get(54, String.class),
                                    tuple.get(55, Integer.class),
                                    tuple.get(56, Integer.class),
                                    null,
                                    null,
                                    tuple.get(57, LocalDateTime.class)
                            ),
                            tuple.get(58, Long.class),
                            tuple.get(59, Long.class),
                            tuple.get(60, Double.class),
                            tuple.get(61, Double.class),
                            tuple.get(62, String.class),
                            tuple.get(63, Boolean.class),
                            tuple.get(64, Boolean.class),
                            tuple.get(65, Boolean.class),
                            tuple.get(66, Double.class),
                            tuple.get(67, String.class),
                            tuple.get(68, OffsetDateTime.class),
                            tuple.get(69, String.class),
                            tuple.get(70, String.class),
                            tuple.get(71, String.class),
                            tuple.get(72, String.class),
                            tuple.get(73, Integer.class),
                            tuple.get(74, Integer.class),
                            Collections.emptyList(),
                            tuple.get(75, OffsetDateTime.class),
                            tuple.get(76, OffsetDateTime.class),
                            tuple.get(77, Boolean.class),
                            tuple.get(78, OffsetDateTime.class),
                            tuple.get(79, OffsetDateTime.class)
                    );
                }).toList();

        return details;
    }

    @Override
    /*public Page<PaymentDetail> findAllCustom(Specification<PaymentDetail> specification, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PaymentDetail> query = cb.createQuery(PaymentDetail.class);

        Root<PaymentDetail> root = query.from(PaymentDetail.class);

        root.fetch("payment", JoinType.LEFT);
        root.fetch("transactionType", JoinType.LEFT);
        root.fetch("manageBooking", JoinType.LEFT);
        root.fetch("paymentDetails", JoinType.LEFT);

        query.distinct(true);

        if (specification != null) {
            Predicate predicate = specification.toPredicate(root, query, cb);
            query.where(predicate);
        }

        query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));

        TypedQuery<PaymentDetail> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<PaymentDetail> results = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<PaymentDetail> countRoot = countQuery.from(PaymentDetail.class);
        countQuery.select(cb.count(countRoot));

        if (specification != null) {
            Predicate countPredicate = specification.toPredicate(countRoot, countQuery, cb);
            countQuery.where(countPredicate);
        }
        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }*/
    public Page<PaymentDetail> findAllCustom(Specification<PaymentDetail> specification, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<PaymentDetail> root = query.from(PaymentDetail.class);

        Join<PaymentDetail, Payment> paymentJoin = root.join("payment", JoinType.LEFT);
        Join<PaymentDetail, ManagePaymentTransactionType> managePaymentTransactionTypeJoin = root.join("transactionType", JoinType.LEFT);
        Join<PaymentDetail, Booking> bookingJoin = root.join("manageBooking", JoinType.LEFT);

        List<Selection<?>> selections = new ArrayList<>();

        selections.add(root.get("id"));
        selections.add(root.get("status"));
        selections.add(root.get("paymentDetailId"));
        selections.add(root.get("parentId"));

        //Payment
        selections.add(paymentJoin.get("id"));
        selections.add(paymentJoin.get("paymentId"));
        selections.add(paymentJoin.get("status"));
        selections.add(paymentJoin.get("eAttachment"));
        selections.add(paymentJoin.get("reference"));
        selections.add(paymentJoin.get("transactionDate"));
        selections.add(paymentJoin.get("dateTime"));
        selections.add(paymentJoin.get("paymentAmount"));
        selections.add(paymentJoin.get("paymentBalance"));
        selections.add(paymentJoin.get("depositAmount"));
        selections.add(paymentJoin.get("depositBalance"));
        selections.add(paymentJoin.get("otherDeductions"));
        selections.add(paymentJoin.get("identified"));
        selections.add(paymentJoin.get("notIdentified"));
        selections.add(paymentJoin.get("notApplied"));
        selections.add(paymentJoin.get("applied"));
        selections.add(paymentJoin.get("remark"));
        selections.add(paymentJoin.get("applyPayment"));
        selections.add(paymentJoin.get("hasAttachment"));
        selections.add(paymentJoin.get("hasDetailTypeDeposit"));
        selections.add(paymentJoin.get("paymentSupport"));
        selections.add(paymentJoin.get("createByCredit"));
        selections.add(paymentJoin.get("createdAt"));
        selections.add(paymentJoin.get("updatedAt"));
        selections.add(paymentJoin.get("importType"));

        //Manage Payment Transaction Type
        selections.add(managePaymentTransactionTypeJoin.get("id"));
        selections.add(managePaymentTransactionTypeJoin.get("code"));
        selections.add(managePaymentTransactionTypeJoin.get("name"));
        selections.add(managePaymentTransactionTypeJoin.get("status"));
        selections.add(managePaymentTransactionTypeJoin.get("cash"));
        selections.add(managePaymentTransactionTypeJoin.get("deposit"));
        selections.add(managePaymentTransactionTypeJoin.get("applyDeposit"));
        selections.add(managePaymentTransactionTypeJoin.get("remarkRequired"));
        selections.add(managePaymentTransactionTypeJoin.get("minNumberOfCharacter"));
        selections.add(managePaymentTransactionTypeJoin.get("defaultRemark"));
        selections.add(managePaymentTransactionTypeJoin.get("defaults"));
        selections.add(managePaymentTransactionTypeJoin.get("paymentInvoice"));
        selections.add(managePaymentTransactionTypeJoin.get("debit"));
        selections.add(managePaymentTransactionTypeJoin.get("expenseToBooking"));
        selections.add(managePaymentTransactionTypeJoin.get("negative"));

        //Booking
        selections.add(bookingJoin.get("id"));
        selections.add(bookingJoin.get("bookingId"));
        selections.add(bookingJoin.get("reservationNumber"));
        selections.add(bookingJoin.get("checkIn"));
        selections.add(bookingJoin.get("checkOut"));
        selections.add(bookingJoin.get("fullName"));
        selections.add(bookingJoin.get("firstName"));
        selections.add(bookingJoin.get("lastName"));
        selections.add(bookingJoin.get("invoiceAmount"));
        selections.add(bookingJoin.get("amountBalance"));
        selections.add(bookingJoin.get("couponNumber"));
        selections.add(bookingJoin.get("adults"));
        selections.add(bookingJoin.get("children"));
        selections.add(bookingJoin.get("bookingDate"));

        selections.add(root.get("reverseFrom"));
        selections.add(root.get("reverseFromParentId"));
        selections.add(root.get("amount"));
        selections.add(root.get("applyDepositValue"));
        selections.add(root.get("remark"));
        selections.add(root.get("reverseTransaction"));
        selections.add(root.get("canceledTransaction"));
        selections.add(root.get("createByCredit"));
        selections.add(root.get("bookingId"));
        selections.add(root.get("invoiceId"));
        selections.add(root.get("transactionDate"));
        selections.add(root.get("firstName"));
        selections.add(root.get("lastName"));
        selections.add(root.get("reservation"));
        selections.add(root.get("couponNo"));
        selections.add(root.get("adults"));
        selections.add(root.get("children"));
        selections.add(root.get("createdAt"));
        selections.add(root.get("updatedAt"));
        selections.add(root.get("applyPayment"));
        selections.add(root.get("appliedAt"));
        selections.add(root.get("effectiveDate"));

        query.multiselect(selections.toArray(new Selection[0]));

        if (specification != null) {
            Predicate predicate = specification.toPredicate(root, query, cb);
            query.where(predicate);
        }

        query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Tuple> tuples = typedQuery.getResultList();

        List<PaymentDetail> details = tuples.stream()
                .map(tuple -> {
                    return new PaymentDetail(
                            tuple.get(0, UUID.class),
                            tuple.get(1, Status.class),
                            tuple.get(2, Long.class),
                            tuple.get(3, Long.class),
                            new Payment(
                                    tuple.get(4, UUID.class),
                                    tuple.get(5, Long.class),
                                    tuple.get(6, Status.class),
                                    tuple.get(7, EAttachment.class),
                                    null,
                                    tuple.get(8, String.class),
                                    tuple.get(9, LocalDate.class),
                                    tuple.get(10, LocalTime.class),
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    Collections.emptyList(),
                                    Collections.emptyList(),
                                    tuple.get(11, Double.class),
                                    tuple.get(12, Double.class),
                                    tuple.get(13, Double.class),
                                    tuple.get(14, Double.class),
                                    tuple.get(15, Double.class),
                                    tuple.get(16, Double.class),
                                    tuple.get(17, Double.class),
                                    tuple.get(18, Double.class),
                                    tuple.get(19, Double.class),
                                    tuple.get(20, String.class),
                                    tuple.get(21, Boolean.class),
                                    tuple.get(22, Boolean.class),
                                    tuple.get(23, Boolean.class),
                                    tuple.get(24, Boolean.class),
                                    tuple.get(25, Boolean.class),
                                    tuple.get(26, OffsetDateTime.class),
                                    tuple.get(27, OffsetDateTime.class),
                                    tuple.get(28, ImportType.class)
                            ),
                            new ManagePaymentTransactionType(
                                    tuple.get(29, UUID.class),
                                    tuple.get(30, String.class),
                                    tuple.get(31, String.class),
                                    tuple.get(32, Status.class),
                                    tuple.get(33, Boolean.class),
                                    tuple.get(34, Boolean.class),
                                    tuple.get(35, Boolean.class),
                                    tuple.get(36, Boolean.class),
                                    tuple.get(37, Integer.class),
                                    tuple.get(38, String.class),
                                    tuple.get(39, Boolean.class),
                                    tuple.get(40, Boolean.class),
                                    tuple.get(41, Boolean.class),
                                    tuple.get(42, Boolean.class),
                                    tuple.get(43, Boolean.class)
                            ),
                            new Booking(
                                    tuple.get(44, UUID.class),
                                    tuple.get(45, Long.class),
                                    tuple.get(46, String.class),
                                    tuple.get(47, LocalDateTime.class),
                                    tuple.get(48, LocalDateTime.class),
                                    tuple.get(49, String.class),
                                    tuple.get(50, String.class),
                                    tuple.get(51, String.class),
                                    tuple.get(52, Double.class),
                                    tuple.get(53, Double.class),
                                    tuple.get(54, String.class),
                                    tuple.get(55, Integer.class),
                                    tuple.get(56, Integer.class),
                                    null,
                                    null,
                                    tuple.get(57, LocalDateTime.class)
                            ),
                            tuple.get(58, Long.class),
                            tuple.get(59, Long.class),
                            tuple.get(60, Double.class),
                            tuple.get(61, Double.class),
                            tuple.get(62, String.class),
                            tuple.get(63, Boolean.class),
                            tuple.get(64, Boolean.class),
                            tuple.get(65, Boolean.class),
                            tuple.get(66, Double.class),
                            tuple.get(67, String.class),
                            tuple.get(68, OffsetDateTime.class),
                            tuple.get(69, String.class),
                            tuple.get(70, String.class),
                            tuple.get(71, String.class),
                            tuple.get(72, String.class),
                            tuple.get(73, Integer.class),
                            tuple.get(74, Integer.class),
                            Collections.emptyList(),
                            tuple.get(75, OffsetDateTime.class),
                            tuple.get(76, OffsetDateTime.class),
                            tuple.get(77, Boolean.class),
                            tuple.get(78, OffsetDateTime.class),
                            tuple.get(79, OffsetDateTime.class)
                    );
                }).toList();





        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<PaymentDetail> countRoot = countQuery.from(PaymentDetail.class);
        countQuery.select(cb.count(countRoot));

        if (specification != null) {
            Predicate countPredicate = specification.toPredicate(countRoot, countQuery, cb);
            countQuery.where(countPredicate);
        }
        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(details, pageable, total);
    }

}
