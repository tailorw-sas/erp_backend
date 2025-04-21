package com.kynsoft.finamer.invoicing.infrastructure.repository.query.invoice;

import com.kynsoft.finamer.invoicing.infrastructure.identity.*;
import com.kynsoft.finamer.invoicing.infrastructure.interfacesEntity.*;
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
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ManageInvoiceCustomRepositoryImpl implements ManageInvoiceCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Invoice> findByIdCustom(UUID id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class);
        Root<Invoice> root = query.from(Invoice.class);

        Join<Invoice, ManageInvoiceType> invoiceTypeJoin = root.join("manageInvoiceType", JoinType.LEFT);
        Join<Invoice, ManageInvoiceStatus> invoiceStatusJoin = root.join("manageInvoiceStatus", JoinType.LEFT);
        Join<Invoice, ManageHotel> hotelJoin = root.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> agencyJoin = root.join("agency", JoinType.LEFT);

        query.where(cb.equal(root.get("id"), id));

        List<Selection<?>> selections = new ArrayList<>();
        selections.add(root.get("id"));
        selections.add(root.get("invoiceId"));
        selections.add(root.get("invoiceNo"));
        selections.add(root.get("invoiceNumber"));
        selections.add(root.get("invoiceNumberPrefix"));
        selections.add(root.get("invoiceDate"));
        selections.add(root.get("dueDate"));
        selections.add(root.get("isManual"));
        selections.add(root.get("autoRec"));
        selections.add(root.get("reSend"));
        selections.add(root.get("isCloned"));
        selections.add(root.get("reSendDate"));

        //Manage Invoice Type
        selections.add(invoiceTypeJoin.get("id"));
        selections.add(invoiceTypeJoin.get("code"));
        selections.add(invoiceTypeJoin.get("deleted"));
        selections.add(invoiceTypeJoin.get("name"));
        selections.add(invoiceTypeJoin.get("invoice"));
        selections.add(invoiceTypeJoin.get("credit"));
        selections.add(invoiceTypeJoin.get("income"));
        selections.add(invoiceTypeJoin.get("status"));
        selections.add(invoiceTypeJoin.get("enabledToPolicy"));
        selections.add(invoiceTypeJoin.get("createdAt"));
        selections.add(invoiceTypeJoin.get("updatedAt"));
        selections.add(invoiceTypeJoin.get("deletedAt"));

        //Manage Invoice Status
        selections.add(invoiceStatusJoin.get("id"));
        selections.add(invoiceStatusJoin.get("code"));
        selections.add(invoiceStatusJoin.get("status"));
        selections.add(invoiceStatusJoin.get("description"));
        selections.add(invoiceStatusJoin.get("name"));
        selections.add(invoiceStatusJoin.get("createdAt"));
        selections.add(invoiceStatusJoin.get("updatedAt"));
        selections.add(invoiceStatusJoin.get("enabledToPrint"));
        selections.add(invoiceStatusJoin.get("enabledToPropagate"));
        selections.add(invoiceStatusJoin.get("enabledToApply"));
        selections.add(invoiceStatusJoin.get("enabledToPolicy"));
        selections.add(invoiceStatusJoin.get("processStatus"));
        selections.add(invoiceStatusJoin.get("sentStatus"));
        selections.add(invoiceStatusJoin.get("reconciledStatus"));
        selections.add(invoiceStatusJoin.get("canceledStatus"));
        selections.add(invoiceStatusJoin.get("showClone"));

        selections.add(root.get("originalAmount"));
        selections.add(root.get("invoiceAmount"));
        selections.add(root.get("dueAmount"));

        //Manage Hotel
        selections.add(hotelJoin.get("id"));
        selections.add(hotelJoin.get("code"));
        selections.add(hotelJoin.get("deleted"));
        selections.add(hotelJoin.get("autogen_code"));

        //Manage Trading Company

        //Manage Hotel - resto
        selections.add(hotelJoin.get("name"));
        selections.add(hotelJoin.get("createdAt"));
        selections.add(hotelJoin.get("updatedAt"));
        selections.add(hotelJoin.get("deletedAt"));
        selections.add(hotelJoin.get("isVirtual"));
        selections.add(hotelJoin.get("status"));
        selections.add(hotelJoin.get("requiresFlatRate"));
        selections.add(hotelJoin.get("autoApplyCredit"));
        selections.add(hotelJoin.get("babelCode"));
        selections.add(hotelJoin.get("city"));

        //Manage Country


        //Manage City State

        //Manage Hotel - resto
        selections.add(hotelJoin.get("address"));

        //Invoice Close Operation

        //Manage Currency

        selections.add(hotelJoin.get("applyByTradingCompany"));
        selections.add(hotelJoin.get("prefixToInvoice"));

        query.multiselect(selections.toArray(new Selection[0]));

        Tuple tuple = entityManager.createQuery(query).getSingleResult();

        Invoice invoice = new Invoice(

        );

        return Optional.of(invoice);
    }

    @Override
    public Page<ManageInvoiceSearchProjection> findAllProjected(Specification<Invoice> specification, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ManageInvoiceSearchProjection> query = cb.createQuery(ManageInvoiceSearchProjection.class);
        Root<Invoice> root = query.from(Invoice.class);

        // Joins optimizados
        Join<Invoice, ManageHotel> hotelJoin = root.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> agencyJoin = root.join("agency", JoinType.LEFT);
        Join<Invoice, ManageInvoiceStatus> statusJoin = root.join("manageInvoiceStatus", JoinType.LEFT);
        Join<Invoice, ManageInvoiceType> typeJoin = root.join("manageInvoiceType", JoinType.LEFT);

        // Construcción de proyección
        query.select(cb.construct(
                ManageInvoiceSearchProjection.class,
                root.get("id"),
                root.get("invoiceId"),
                root.get("isManual"),
                root.get("invoiceNo"),
                root.get("invoiceAmount"),
                root.get("dueAmount"),
                root.get("invoiceDate"),
                cb.construct(ManageInvoiceHotelProjection.class,
                        hotelJoin.get("id"),
                        hotelJoin.get("code"),
                        hotelJoin.get("name"),
                        hotelJoin.get("isVirtual")
                ),
                cb.construct(ManageInvoiceAgencyProjection.class,
                        agencyJoin.get("id"),
                        agencyJoin.get("code"),
                        agencyJoin.get("name")
                ),

        cb.construct(ManageInvoiceStatusProjection.class,
                        statusJoin.get("id"),
                        statusJoin.get("name"),
                        statusJoin.get("code"),
                        statusJoin.get("showClone"),
                        statusJoin.get("enabledToApply"),
                        statusJoin.get("processStatus"),
                        statusJoin.get("sentStatus"),
                        statusJoin.get("reconciledStatus"),
                        statusJoin.get("canceledStatus")
                ),
                root.get("hasAttachments"),
                root.get("invoiceType"),
                root.get("invoiceStatus"),
                root.get("invoiceNumber"),
                cb.construct(ManageInvoiceTypeProjection.class,
                        typeJoin.get("id"),
                        typeJoin.get("name"),
                        typeJoin.get("code")
                ),
                root.get("sendStatusError"),
                root.get("parent").get("id"),
                root.get("autoRec"),
                root.get("originalAmount"),
                root.get("importType"),
                root.get("cloneParent"),
                root.get("aging"),
                cb.selectCase()
                        .when(cb.and(
                                cb.isNotNull(hotelJoin.get("closeOperation")),
                                cb.greaterThanOrEqualTo(root.get("invoiceDate"), hotelJoin.get("closeOperation").get("beginDate")),
                                cb.lessThanOrEqualTo(root.get("invoiceDate"), hotelJoin.get("closeOperation").get("endDate"))
                        ), true)
                        .otherwise(false),
                root.get("dueDate")
        ));

        // Aplicar especificaciones
        if (specification != null) {
            Predicate predicate = specification.toPredicate(root, query, cb);
            query.where(predicate);
        }

        // Orden y paginación
        query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));
        TypedQuery<ManageInvoiceSearchProjection> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        // Contar el total de resultados
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Invoice> countRoot = countQuery.from(Invoice.class);
        countQuery.select(cb.count(countRoot));

        if (specification != null) {
            Predicate countPredicate = specification.toPredicate(countRoot, countQuery, cb);
            countQuery.where(countPredicate);
        }

        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(typedQuery.getResultList(), pageable, total);
    }
}