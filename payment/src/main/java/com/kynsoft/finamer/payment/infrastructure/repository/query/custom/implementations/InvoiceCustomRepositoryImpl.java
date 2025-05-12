package com.kynsoft.finamer.payment.infrastructure.repository.query.custom.implementations;

import com.kynsof.share.core.infrastructure.repository.IndexRef;
import com.kynsoft.finamer.payment.infrastructure.identity.*;
import com.kynsoft.finamer.payment.infrastructure.repository.mapper.BookingMapper;
import com.kynsoft.finamer.payment.infrastructure.repository.mapper.InvoiceMapper;
import com.kynsoft.finamer.payment.infrastructure.repository.query.custom.InvoiceCustomRepository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InvoiceCustomRepositoryImpl implements InvoiceCustomRepository {

    private final InvoiceMapper invoiceMapper;
    private final BookingMapper bookingMapper;

    public InvoiceCustomRepositoryImpl(BookingMapper bookingMapper,
                                       InvoiceMapper invoiceMapper){
        this.bookingMapper = bookingMapper;
        this.invoiceMapper = invoiceMapper;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Invoice> findAllCustom(Specification<Invoice> specification, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<Invoice> root = query.from(Invoice.class);
        Join<Invoice, Invoice> invoiceJoin = root.join("parent", JoinType.LEFT);

        Join<Invoice, ManageHotel> invoiceHotelJoin = invoiceJoin.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> invoiceAgencyJoin = invoiceJoin.join("agency", JoinType.LEFT);
        Join<ManageAgency, ManageAgencyType> invoiceAgencyTypeJoin = invoiceAgencyJoin.join("agencyType", JoinType.LEFT);
        Join<ManageAgency, ManageClient> invoiceClientJoin = invoiceAgencyJoin.join("client", JoinType.LEFT);
        Join<ManageAgency, ManageCountry> invoiceCountryJoin = invoiceAgencyJoin.join("country", JoinType.LEFT);
        Join<ManageCountry, ManageLanguage> invoiceCountryManageLanguageJoin = invoiceCountryJoin.join("managerLanguage", JoinType.LEFT);
        Join<Invoice, ManageInvoiceStatus> invoiceStatusJoin = root.join("status", JoinType.LEFT);

        Join<Invoice, ManageHotel> hotelJoin = root.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> agencyJoin = root.join("agency", JoinType.LEFT);
        Join<ManageAgency, ManageAgencyType> agencyTypeJoin = agencyJoin.join("agencyType", JoinType.LEFT);
        Join<ManageAgency, ManageClient> clientJoin = agencyJoin.join("client", JoinType.LEFT);
        Join<ManageAgency, ManageCountry> countryJoin = agencyJoin.join("country", JoinType.LEFT);
        Join<ManageCountry, ManageLanguage> countryManageLanguageJoin = countryJoin.join("managerLanguage", JoinType.LEFT);
        Join<Invoice, ManageInvoiceStatus> statusJoin = root.join("status", JoinType.LEFT);

        List<Selection<?>> selections = this.getInvoiceSelections(root,
                invoiceJoin,
                invoiceHotelJoin,
                invoiceAgencyJoin,
                invoiceAgencyTypeJoin,
                invoiceClientJoin,
                invoiceCountryJoin,
                invoiceCountryManageLanguageJoin,
                invoiceStatusJoin,
                hotelJoin,
                agencyJoin,
                agencyTypeJoin,
                clientJoin,
                countryJoin,
                countryManageLanguageJoin,
                statusJoin);

        query.multiselect(selections.toArray(new Selection[0]));

        if(specification != null){
            Predicate predicate = specification.toPredicate(root, query, cb);
            query.where(predicate);
        }
        query.orderBy(QueryUtils.toOrders(pageable.getSort(), root, cb));

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int)pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Tuple> tuples = typedQuery.getResultList();

        List<UUID> invoiceIds = tuples.stream()
                .map(tuple -> tuple.get(0, UUID.class))
                .collect(Collectors.toList());

        Map<UUID, List<Booking>> bookingsMap = this.getBookingsByInvoiceIdsMap(invoiceIds);

        List<Invoice> results = tuples.stream()
                .map(tuple -> this.convertTupleToInvoice(tuple, 0, bookingsMap.get(tuple.get(0, UUID.class))))
                .collect(Collectors.toList());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Invoice> countRoot = countQuery.from(Invoice.class);
        countQuery.select(cb.count(countRoot));

        if(specification != null){
            Predicate countPredicate = specification.toPredicate(countRoot, countQuery, cb);
            countQuery.where(countPredicate);
        }
        long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Optional<Invoice> findByIdCustom(UUID id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<Invoice> root = query.from(Invoice.class);
        Join<Invoice, Invoice> invoiceJoin = root.join("parent", JoinType.LEFT);

        Join<Invoice, ManageHotel> invoiceHotelJoin = invoiceJoin.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> invoiceAgencyJoin = invoiceJoin.join("agency", JoinType.LEFT);
        Join<ManageAgency, ManageAgencyType> invoiceAgencyTypeJoin = invoiceAgencyJoin.join("agencyType", JoinType.LEFT);
        Join<ManageAgency, ManageClient> invoiceClientJoin = invoiceAgencyJoin.join("client", JoinType.LEFT);
        Join<ManageAgency, ManageCountry> invoiceCountryJoin = invoiceAgencyJoin.join("country", JoinType.LEFT);
        Join<ManageCountry, ManageLanguage> invoiceCountryManageLanguageJoin = invoiceCountryJoin.join("managerLanguage", JoinType.LEFT);
        Join<Invoice, ManageInvoiceStatus> invoiceStatusJoin = root.join("status", JoinType.LEFT);

        Join<Invoice, ManageHotel> hotelJoin = root.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> agencyJoin = root.join("agency", JoinType.LEFT);
        Join<ManageAgency, ManageAgencyType> agencyTypeJoin = agencyJoin.join("agencyType", JoinType.LEFT);
        Join<ManageAgency, ManageClient> clientJoin = agencyJoin.join("client", JoinType.LEFT);
        Join<ManageAgency, ManageCountry> countryJoin = agencyJoin.join("country", JoinType.LEFT);
        Join<ManageCountry, ManageLanguage> countryManageLanguageJoin = countryJoin.join("managerLanguage", JoinType.LEFT);

        Join<Invoice, ManageInvoiceStatus> statusJoin = root.join("status", JoinType.LEFT);

        List<Selection<?>> selections = this.getInvoiceSelections(root,
                invoiceJoin,
                invoiceHotelJoin,
                invoiceAgencyJoin,
                invoiceAgencyTypeJoin,
                invoiceClientJoin,
                invoiceCountryJoin,
                invoiceCountryManageLanguageJoin,
                invoiceStatusJoin,
                hotelJoin,
                agencyJoin,
                agencyTypeJoin,
                clientJoin,
                countryJoin,
                countryManageLanguageJoin,
                statusJoin);

        query.multiselect(selections.toArray(new Selection[0]));

        query.where(cb.equal(root.get("id"), id));

        try{
            Tuple tuple = entityManager.createQuery(query).getSingleResult();
            List<Booking> bookings = this.getBookingsByInvoiceId(id);

            Invoice result = this.convertTupleToInvoice(tuple, 0, bookings);
            return Optional.of(result);
        }catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Invoice> findByInvoiceIdCustom(Long invoiceId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<Invoice> root = query.from(Invoice.class);
        Join<Invoice, Invoice> invoiceJoin = root.join("parent", JoinType.LEFT);

        Join<Invoice, ManageHotel> invoiceHotelJoin = invoiceJoin.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> invoiceAgencyJoin = invoiceJoin.join("agency", JoinType.LEFT);
        Join<ManageAgency, ManageAgencyType> invoiceAgencyTypeJoin = invoiceAgencyJoin.join("agencyType", JoinType.LEFT);
        Join<ManageAgency, ManageClient> invoiceClientJoin = invoiceAgencyJoin.join("client", JoinType.LEFT);
        Join<ManageAgency, ManageCountry> invoiceCountryJoin = invoiceAgencyJoin.join("country", JoinType.LEFT);
        Join<ManageCountry, ManageLanguage> invoiceCountryManageLanguageJoin = invoiceCountryJoin.join("managerLanguage", JoinType.LEFT);
        Join<Invoice, ManageInvoiceStatus> invoiceStatusJoin = root.join("status", JoinType.LEFT);

        Join<Invoice, ManageHotel> hotelJoin = root.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> agencyJoin = root.join("agency", JoinType.LEFT);
        Join<ManageAgency, ManageAgencyType> agencyTypeJoin = agencyJoin.join("agencyType", JoinType.LEFT);
        Join<ManageAgency, ManageClient> clientJoin = agencyJoin.join("client", JoinType.LEFT);
        Join<ManageAgency, ManageCountry> countryJoin = agencyJoin.join("country", JoinType.LEFT);
        Join<ManageCountry, ManageLanguage> countryManageLanguageJoin = countryJoin.join("managerLanguage", JoinType.LEFT);

        Join<Invoice, ManageInvoiceStatus> statusJoin = root.join("status", JoinType.LEFT);

        List<Selection<?>> selections = this.getInvoiceSelections(root,
                invoiceJoin,
                invoiceHotelJoin,
                invoiceAgencyJoin,
                invoiceAgencyTypeJoin,
                invoiceClientJoin,
                invoiceCountryJoin,
                invoiceCountryManageLanguageJoin,
                invoiceStatusJoin,
                hotelJoin,
                agencyJoin,
                agencyTypeJoin,
                clientJoin,
                countryJoin,
                countryManageLanguageJoin,
                statusJoin);

        query.multiselect(selections.toArray(new Selection[0]));

        query.where(cb.equal(root.get("invoiceId"), invoiceId));

        try{
            Tuple tuple = entityManager.createQuery(query).getSingleResult();
            List<Booking> bookings = this.getBookingsByInvoiceId(tuple.get(0, UUID.class));

            Invoice result = this.convertTupleToInvoice(tuple, 0, bookings);
            return Optional.of(result);
        }catch (NoResultException e){
            return Optional.empty();
        }

    }

    @Override
    public List<Invoice> findAllByIdInCustom(List<UUID> ids) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<Invoice> root = query.from(Invoice.class);
        Join<Invoice, Invoice> invoiceJoin = root.join("parent", JoinType.LEFT);

        Join<Invoice, ManageHotel> invoiceHotelJoin = invoiceJoin.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> invoiceAgencyJoin = invoiceJoin.join("agency", JoinType.LEFT);
        Join<ManageAgency, ManageAgencyType> invoiceAgencyTypeJoin = invoiceAgencyJoin.join("agencyType", JoinType.LEFT);
        Join<ManageAgency, ManageClient> invoiceClientJoin = invoiceAgencyJoin.join("client", JoinType.LEFT);
        Join<ManageAgency, ManageCountry> invoiceCountryJoin = invoiceAgencyJoin.join("country", JoinType.LEFT);
        Join<ManageCountry, ManageLanguage> invoiceCountryManageLanguageJoin = invoiceCountryJoin.join("managerLanguage", JoinType.LEFT);
        Join<Invoice, ManageInvoiceStatus> invoiceStatusJoin = root.join("status", JoinType.LEFT);

        Join<Invoice, ManageHotel> hotelJoin = root.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> agencyJoin = root.join("agency", JoinType.LEFT);
        Join<ManageAgency, ManageAgencyType> agencyTypeJoin = agencyJoin.join("agencyType", JoinType.LEFT);
        Join<ManageAgency, ManageClient> clientJoin = agencyJoin.join("client", JoinType.LEFT);
        Join<ManageAgency, ManageCountry> countryJoin = agencyJoin.join("country", JoinType.LEFT);
        Join<ManageCountry, ManageLanguage> countryManageLanguageJoin = countryJoin.join("managerLanguage", JoinType.LEFT);

        Join<Invoice, ManageInvoiceStatus> statusJoin = root.join("status", JoinType.LEFT);

        List<Selection<?>> selections = this.getInvoiceSelections(root,
                invoiceJoin,
                invoiceHotelJoin,
                invoiceAgencyJoin,
                invoiceAgencyTypeJoin,
                invoiceClientJoin,
                invoiceCountryJoin,
                invoiceCountryManageLanguageJoin,
                invoiceStatusJoin,
                hotelJoin,
                agencyJoin,
                agencyTypeJoin,
                clientJoin,
                countryJoin,
                countryManageLanguageJoin,
                statusJoin);

        query.multiselect(selections.toArray(new Selection[0]));

        query.where(root.get("id").in(ids));

        List<Tuple> tuples = entityManager.createQuery(query).getResultList();

        Map<UUID, List<Booking>> bookingsMap = this.getBookingsByInvoiceIdsMap(ids);

        List<Invoice> results = tuples.stream()
                .map(tuple -> this.convertTupleToInvoice(tuple, 0, bookingsMap.get(tuple.get(0, UUID.class))))
                .collect(Collectors.toList());

        return results;
    }

    private List<Selection<?>> getInvoiceSelections(Root<Invoice> root,
                                                    Join<Invoice, Invoice> invoiceJoin,

                                                    Join<Invoice, ManageHotel> invoiceHotelJoin,
                                                    Join<Invoice, ManageAgency> invoiceAgencyJoin,

                                                    Join<ManageAgency, ManageAgencyType> invoiceAgencyTypeJoin,
                                                    Join<ManageAgency, ManageClient> invoiceClientJoin,
                                                    Join<ManageAgency, ManageCountry> invoiceCountryJoin,
                                                    Join<ManageCountry, ManageLanguage> invoiceCountryManageLanguageJoin,
                                                    Join<Invoice, ManageInvoiceStatus> invoiceStatusJoin,

                                                    Join<Invoice, ManageHotel> hotelJoin,
                                                    Join<Invoice, ManageAgency> agencyJoin,
                                                    Join<ManageAgency, ManageAgencyType> agencyTypeJoin,
                                                    Join<ManageAgency, ManageClient> clientJoin,
                                                    Join<ManageAgency, ManageCountry> countryJoin,
                                                    Join<ManageCountry, ManageLanguage> countryManageLanguageJoin,
                                                    Join<Invoice, ManageInvoiceStatus> statusJoin){
        List<Selection<?>> selections = new ArrayList<>();

        selections.add(root.get("id"));
        selections.add(root.get("invoiceId"));
        selections.add(root.get("invoiceNo"));
        selections.add(root.get("invoiceNumber"));
        selections.add(root.get("invoiceAmount"));
        selections.add(root.get("invoiceBalance"));
        selections.add(root.get("invoiceDate"));
        selections.add(root.get("hasAttachment"));

        //Invoice Parent
        selections.add(invoiceJoin.get("id"));
        selections.add(invoiceJoin.get("invoiceId"));
        selections.add(invoiceJoin.get("invoiceNo"));
        selections.add(invoiceJoin.get("invoiceNumber"));
        selections.add(invoiceJoin.get("invoiceAmount"));
        selections.add(invoiceJoin.get("invoiceDate"));
        selections.add(invoiceJoin.get("hasAttachment"));
        selections.add(invoiceJoin.get("invoiceType"));

        selections.add(invoiceHotelJoin.get("id"));
        selections.add(invoiceHotelJoin.get("code"));
        selections.add(invoiceHotelJoin.get("deleted"));
        selections.add(invoiceHotelJoin.get("name"));
        selections.add(invoiceHotelJoin.get("status"));
        selections.add(invoiceHotelJoin.get("applyByTradingCompany"));
        selections.add(invoiceHotelJoin.get("manageTradingCompany"));
        selections.add(invoiceHotelJoin.get("autoApplyCredit"));
        selections.add(invoiceHotelJoin.get("createdAt"));
        selections.add(invoiceHotelJoin.get("updatedAt"));
        selections.add(invoiceHotelJoin.get("deletedAt"));

        selections.add(invoiceAgencyJoin.get("id"));
        selections.add(invoiceAgencyJoin.get("code"));
        selections.add(invoiceAgencyJoin.get("name"));
        selections.add(invoiceAgencyJoin.get("status"));

        selections.add(invoiceAgencyTypeJoin.get("id"));
        selections.add(invoiceAgencyTypeJoin.get("code"));
        selections.add(invoiceAgencyTypeJoin.get("status"));
        selections.add(invoiceAgencyTypeJoin.get("name"));

        selections.add(invoiceClientJoin.get("id"));
        selections.add(invoiceClientJoin.get("code"));
        selections.add(invoiceClientJoin.get("status"));
        selections.add(invoiceClientJoin.get("name"));

        selections.add(invoiceCountryJoin.get("id"));
        selections.add(invoiceCountryJoin.get("code"));
        selections.add(invoiceCountryJoin.get("name"));
        selections.add(invoiceCountryJoin.get("description"));
        selections.add(invoiceCountryJoin.get("isDefault"));
        selections.add(invoiceCountryJoin.get("status"));

        selections.add(invoiceCountryManageLanguageJoin.get("id"));
        selections.add(invoiceCountryManageLanguageJoin.get("code"));
        selections.add(invoiceCountryManageLanguageJoin.get("name"));
        selections.add(invoiceCountryManageLanguageJoin.get("defaults"));
        selections.add(invoiceCountryManageLanguageJoin.get("status"));
        selections.add(invoiceCountryManageLanguageJoin.get("createdAt"));
        selections.add(invoiceCountryManageLanguageJoin.get("updatedAt"));

        selections.add(invoiceCountryJoin.get("createdAt"));
        selections.add(invoiceCountryJoin.get("updateAt"));
        selections.add(invoiceCountryJoin.get("deleteAt"));
        selections.add(invoiceCountryJoin.get("iso3"));

        selections.add(invoiceAgencyJoin.get("createdAt"));
        selections.add(invoiceAgencyJoin.get("updatedAt"));

        selections.add(invoiceJoin.get("autoRec"));

        selections.add(invoiceStatusJoin.get("id"));
        selections.add(invoiceStatusJoin.get("code"));
        selections.add(invoiceStatusJoin.get("name"));

        //Fin Invoice Parent

        selections.add(root.get("invoiceType"));

        selections.add(hotelJoin.get("id"));
        selections.add(hotelJoin.get("code"));
        selections.add(hotelJoin.get("deleted"));
        selections.add(hotelJoin.get("name"));
        selections.add(hotelJoin.get("status"));
        selections.add(hotelJoin.get("applyByTradingCompany"));
        selections.add(hotelJoin.get("manageTradingCompany"));
        selections.add(hotelJoin.get("autoApplyCredit"));
        selections.add(hotelJoin.get("createdAt"));
        selections.add(hotelJoin.get("updatedAt"));
        selections.add(hotelJoin.get("deletedAt"));

        selections.add(agencyJoin.get("id"));
        selections.add(agencyJoin.get("code"));
        selections.add(agencyJoin.get("name"));
        selections.add(agencyJoin.get("status"));

        selections.add(agencyTypeJoin.get("id"));
        selections.add(agencyTypeJoin.get("code"));
        selections.add(agencyTypeJoin.get("status"));
        selections.add(agencyTypeJoin.get("name"));

        selections.add(clientJoin.get("id"));
        selections.add(clientJoin.get("code"));
        selections.add(clientJoin.get("status"));
        selections.add(clientJoin.get("name"));

        selections.add(countryJoin.get("id"));
        selections.add(countryJoin.get("code"));
        selections.add(countryJoin.get("name"));
        selections.add(countryJoin.get("description"));
        selections.add(countryJoin.get("isDefault"));
        selections.add(countryJoin.get("status"));

        selections.add(countryManageLanguageJoin.get("id"));
        selections.add(countryManageLanguageJoin.get("code"));
        selections.add(countryManageLanguageJoin.get("name"));
        selections.add(countryManageLanguageJoin.get("defaults"));
        selections.add(countryManageLanguageJoin.get("status"));
        selections.add(countryManageLanguageJoin.get("createdAt"));
        selections.add(countryManageLanguageJoin.get("updatedAt"));

        selections.add(countryJoin.get("createdAt"));
        selections.add(countryJoin.get("updateAt"));
        selections.add(countryJoin.get("deleteAt"));
        selections.add(countryJoin.get("iso3"));

        selections.add(agencyJoin.get("createdAt"));
        selections.add(agencyJoin.get("updatedAt"));

        selections.add(root.get("autoRec"));

        selections.add(statusJoin.get("id"));
        selections.add(statusJoin.get("code"));
        selections.add(statusJoin.get("name"));
        return selections;
    }

    private Invoice convertTupleToInvoice(Tuple tuple, int i, List<Booking> bookings){
        IndexRef index = new IndexRef(i);
        return this.invoiceMapper.map(tuple, index);

        /*return new Invoice(
                tuple.get(i++, UUID.class),
                tuple.get(i++, Long.class),
                tuple.get(i++, Long.class),
                tuple.get(i++, String.class),
                tuple.get(i++, Double.class),
                tuple.get(i++, Double.class),
                tuple.get(i++, LocalDateTime.class),
                tuple.get(i++, Boolean.class),
                (tuple.get(i, UUID.class) != null) ? new Invoice(
                        tuple.get(i++, UUID.class),
                        tuple.get(i++, Long.class),
                        tuple.get(i++, Long.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, Double.class),
                        tuple.get(i++, Double.class),
                        tuple.get(i++, LocalDateTime.class),
                        tuple.get(i++, Boolean.class),
                        null,
                        tuple.get(i++, EInvoiceType.class),
                        null,
                        (tuple.get(i, UUID.class) != null) ? new ManageHotel(
                                tuple.get(i++, UUID.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, Boolean.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, Boolean.class),
                                tuple.get(i++, UUID.class),
                                tuple.get(i++, Boolean.class),
                                tuple.get(i++, LocalDateTime.class),
                                tuple.get(i++, LocalDateTime.class),
                                tuple.get(i++, LocalDateTime.class)
                        ) : skip( i += 11),
                        (tuple.get(i, UUID.class) != null) ? new ManageAgency(
                                tuple.get(i++, UUID.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class),
                                (tuple.get(i, UUID.class) != null) ? new ManageAgencyType(
                                        tuple.get(i++, UUID.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class)
                                ) : skip(i += 4),
                                (tuple.get(i, UUID.class) != null) ? new ManageClient(
                                        tuple.get(i++, UUID.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class)
                                ) : skip( i+= 4),
                                (tuple.get(i, UUID.class) != null) ? new ManageCountry(
                                        tuple.get(i++, UUID.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, Boolean.class),
                                        tuple.get(i++, String.class),
                                        (tuple.get(i, UUID.class) != null) ? new ManageLanguage(
                                                tuple.get(i++, UUID.class),
                                                tuple.get(i++, String.class),
                                                tuple.get(i++, String.class),
                                                tuple.get(i++, Boolean.class),
                                                tuple.get(i++, String.class),
                                                tuple.get(i++, LocalDateTime.class),
                                                tuple.get(i++, LocalDateTime.class)
                                        ) : skip( i+= 7),
                                        tuple.get(i++, LocalDateTime.class),
                                        tuple.get(i++, LocalDateTime.class),
                                        tuple.get(i++, LocalDateTime.class),
                                        tuple.get(i++, String.class)
                                ) : skip( i+= 17 ),
                                tuple.get(i++, LocalDateTime.class),
                                tuple.get(i++, LocalDateTime.class)
                        ) : skip( i += 31),
                        tuple.get(i++, Boolean.class),
                        (tuple.get(i, UUID.class) != null) ? new ManageInvoiceStatus(
                                tuple.get(i++, UUID.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class)
                        ) : skip( i += 3)
                ) : skip( i += 55),
                tuple.get(i++, EInvoiceType.class),
                bookings,
                (tuple.get(i, UUID.class) != null) ? new ManageHotel(
                        tuple.get(i++, UUID.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, Boolean.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, Boolean.class),
                        tuple.get(i++, UUID.class),
                        tuple.get(i++, Boolean.class),
                        tuple.get(i++, LocalDateTime.class),
                        tuple.get(i++, LocalDateTime.class),
                        tuple.get(i++, LocalDateTime.class)
                ) : skip( i += 11),
                (tuple.get(i, UUID.class) != null) ? new ManageAgency(tuple.get(i++, UUID.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, String.class),
                        (tuple.get(i, UUID.class) != null) ? new ManageAgencyType(
                                tuple.get(i++, UUID.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class)
                        ) : skip(i += 4),
                        (tuple.get(i, UUID.class) != null) ? new ManageClient(
                                tuple.get(i++, UUID.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class)
                        ) : skip( i+= 4),
                        (tuple.get(i, UUID.class) != null) ? new ManageCountry(
                                tuple.get(i++, UUID.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, Boolean.class),
                                tuple.get(i++, String.class),
                                (tuple.get(i, UUID.class) != null) ? new ManageLanguage(
                                        tuple.get(i++, UUID.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, Boolean.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, LocalDateTime.class),
                                        tuple.get(i++, LocalDateTime.class)
                                ) : skip( i+= 7),
                                tuple.get(i++, LocalDateTime.class),
                                tuple.get(i++, LocalDateTime.class),
                                tuple.get(i++, LocalDateTime.class),
                                tuple.get(i++, String.class)
                        ) : skip( i+= 17 ),
                        tuple.get(i++, LocalDateTime.class),
                        tuple.get(i++, LocalDateTime.class)
                ) : skip( i += 31),
                tuple.get(i++, Boolean.class),
                (tuple.get(i, UUID.class) != null) ? new ManageInvoiceStatus(
                        tuple.get(i++, UUID.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, String.class)
                ) : skip( i += 3)
        );
        */
    }

    private List<Booking> getBookingsByInvoiceId(UUID invoiceId){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<Invoice> root = query.from(Invoice.class);

        //Join para los bookings del Invoice
        Join<Invoice, Booking> bookingJoin = root.join("bookings", JoinType.LEFT);

        //Join para el Invoice parent del Booking del invoice
        Join<Booking, Invoice> bookingInvoiceJoin = bookingJoin.join("invoice", JoinType.LEFT);
        Join<Invoice, ManageHotel> bookingInvoiceHotelJoin = bookingInvoiceJoin.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> bookingInvoiceAgencyJoin = bookingInvoiceJoin.join("agency", JoinType.LEFT);
        Join<ManageAgency, ManageAgencyType> bookingInvoiceAgencyTypeJoin = bookingInvoiceAgencyJoin.join("agencyType", JoinType.LEFT);
        Join<ManageAgency, ManageClient> bookingInvoiceClientJoin = bookingInvoiceAgencyJoin.join("client", JoinType.LEFT);
        Join<ManageAgency, ManageCountry> bookingInvoiceCountryJoin = bookingInvoiceAgencyJoin.join("country", JoinType.LEFT);
        Join<ManageCountry, ManageLanguage> bookingInvoiceCountryManageLanguageJoin = bookingInvoiceCountryJoin.join("managerLanguage", JoinType.LEFT);
        Join<Invoice, ManageInvoiceStatus> bookingInvoiceManageStatus = bookingInvoiceJoin.join("status", JoinType.LEFT);

        //Join para el Booking parent del Booking del invoice
        Join<Booking, Booking> bookingBookingJoin = bookingJoin.join("parent", JoinType.LEFT);

        List<Selection<?>> selections = this.getBookingByInvoiceIdSelections(root,
                bookingJoin,
                bookingInvoiceJoin,
                bookingInvoiceHotelJoin,
                bookingInvoiceAgencyJoin,
                bookingInvoiceAgencyTypeJoin,
                bookingInvoiceClientJoin,
                bookingInvoiceCountryJoin,
                bookingInvoiceCountryManageLanguageJoin,
                bookingInvoiceManageStatus,
                bookingBookingJoin
        );

        query.multiselect(selections.toArray(new Selection[0]));

        query.where(cb.equal(root.get("id"), invoiceId));

        List<Tuple> tuples = entityManager.createQuery(query).getResultList();

        List<Booking> bookingsByEmployeeId = tuples.stream()
                .map(tuple -> {
                    return this.convertTupleToBooking(tuple, 1);
                })
                .collect(Collectors.toList());

        return bookingsByEmployeeId;
    }

    private Map<UUID, List<Booking>> getBookingsByInvoiceIdsMap(List<UUID> invoiceIds){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();

        Root<Invoice> root = query.from(Invoice.class);
        //Join para los bookings del Invoice
        Join<Invoice, Booking> bookingJoin = root.join("bookings", JoinType.LEFT);

        //Join para el Invoice parent del Booking del invoice
        Join<Booking, Invoice> bookingInvoiceJoin = bookingJoin.join("invoice", JoinType.LEFT);
        Join<Invoice, ManageHotel> bookingInvoiceHotelJoin = bookingInvoiceJoin.join("hotel", JoinType.LEFT);
        Join<Invoice, ManageAgency> bookingInvoiceAgencyJoin = bookingInvoiceJoin.join("agency", JoinType.LEFT);
        Join<ManageAgency, ManageAgencyType> bookingInvoiceAgencyTypeJoin = bookingInvoiceAgencyJoin.join("agencyType", JoinType.LEFT);
        Join<ManageAgency, ManageClient> bookingInvoiceClientJoin = bookingInvoiceAgencyJoin.join("client", JoinType.LEFT);
        Join<ManageAgency, ManageCountry> bookingInvoiceCountryJoin = bookingInvoiceAgencyJoin.join("country", JoinType.LEFT);
        Join<ManageCountry, ManageLanguage> bookingInvoiceCountryManageLanguageJoin = bookingInvoiceCountryJoin.join("managerLanguage", JoinType.LEFT);
        Join<Invoice, ManageInvoiceStatus> bookingInvoiceManageStatus = bookingInvoiceJoin.join("status", JoinType.LEFT);

        //Join para el Booking parent del Booking del invoice
        Join<Booking, Booking> bookingBookingJoin = bookingJoin.join("parent", JoinType.LEFT);


        List<Selection<?>> selections = this.getBookingByInvoiceIdSelections(root,
                bookingJoin,
                bookingInvoiceJoin,
                bookingInvoiceHotelJoin,
                bookingInvoiceAgencyJoin,
                bookingInvoiceAgencyTypeJoin,
                bookingInvoiceClientJoin,
                bookingInvoiceCountryJoin,
                bookingInvoiceCountryManageLanguageJoin,
                bookingInvoiceManageStatus,
                bookingBookingJoin
                );

        query.multiselect(selections.toArray(new Selection[0]));

        query.where(root.get("id").in(invoiceIds));

        List<Tuple> tuples = entityManager.createQuery(query).getResultList();

        Map<UUID, List<Booking>> bookingsByEmployeeIdMap = tuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(0, UUID.class),
                        Collectors.mapping(tuple -> {
                            return this.convertTupleToBooking(tuple, 1);
                            }, Collectors.toList())));

        return bookingsByEmployeeIdMap;
    }

    private List<Selection<?>> getBookingByInvoiceIdSelections(Root<Invoice> root,
                                                               Join<Invoice, Booking> bookingJoin,
                                                               Join<Booking, Invoice> bookingInvoiceJoin,
                                                               Join<Invoice, ManageHotel> bookingInvoiceHotelJoin,
                                                               Join<Invoice, ManageAgency> bookingInvoiceAgencyJoin,
                                                               Join<ManageAgency, ManageAgencyType> bookingInvoiceAgencyTypeJoin,
                                                               Join<ManageAgency, ManageClient> bookingInvoiceClientJoin,
                                                               Join<ManageAgency, ManageCountry> bookingInvoiceCountryJoin,
                                                               Join<ManageCountry, ManageLanguage> bookingInvoiceCountryManageLanguageJoin,
                                                               Join<Invoice, ManageInvoiceStatus> bookingInvoiceManageStatus,
                                                               Join<Booking, Booking> bookingBookingJoin){
        List<Selection<?>> selections = new ArrayList<>();

        selections.add(root.get("id"));

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

        //bookingInvoiceJoin

        selections.add(bookingInvoiceJoin.get("id"));
        selections.add(bookingInvoiceJoin.get("invoiceId"));
        selections.add(bookingInvoiceJoin.get("invoiceNo"));
        selections.add(bookingInvoiceJoin.get("invoiceNumber"));
        selections.add(bookingInvoiceJoin.get("invoiceAmount"));
        selections.add(bookingInvoiceJoin.get("invoiceBalance"));
        selections.add(bookingInvoiceJoin.get("invoiceDate"));
        selections.add(bookingInvoiceJoin.get("hasAttachment"));
        selections.add(bookingInvoiceJoin.get("invoiceType"));

        selections.add(bookingInvoiceHotelJoin.get("id"));
        selections.add(bookingInvoiceHotelJoin.get("code"));
        selections.add(bookingInvoiceHotelJoin.get("deleted"));
        selections.add(bookingInvoiceHotelJoin.get("name"));
        selections.add(bookingInvoiceHotelJoin.get("status"));
        selections.add(bookingInvoiceHotelJoin.get("applyByTradingCompany"));
        selections.add(bookingInvoiceHotelJoin.get("manageTradingCompany"));
        selections.add(bookingInvoiceHotelJoin.get("autoApplyCredit"));
        selections.add(bookingInvoiceHotelJoin.get("createdAt"));
        selections.add(bookingInvoiceHotelJoin.get("updatedAt"));
        selections.add(bookingInvoiceHotelJoin.get("deletedAt"));

        selections.add(bookingInvoiceAgencyJoin.get("id"));
        selections.add(bookingInvoiceAgencyJoin.get("code"));
        selections.add(bookingInvoiceAgencyJoin.get("name"));
        selections.add(bookingInvoiceAgencyJoin.get("status"));

        selections.add(bookingInvoiceAgencyTypeJoin.get("id"));
        selections.add(bookingInvoiceAgencyTypeJoin.get("code"));
        selections.add(bookingInvoiceAgencyTypeJoin.get("status"));
        selections.add(bookingInvoiceAgencyTypeJoin.get("name"));

        selections.add(bookingInvoiceClientJoin.get("id"));
        selections.add(bookingInvoiceClientJoin.get("code"));
        selections.add(bookingInvoiceClientJoin.get("status"));
        selections.add(bookingInvoiceClientJoin.get("name"));

        selections.add(bookingInvoiceCountryJoin.get("id"));
        selections.add(bookingInvoiceCountryJoin.get("code"));
        selections.add(bookingInvoiceCountryJoin.get("name"));
        selections.add(bookingInvoiceCountryJoin.get("description"));
        selections.add(bookingInvoiceCountryJoin.get("isDefault"));
        selections.add(bookingInvoiceCountryJoin.get("status"));

        selections.add(bookingInvoiceCountryManageLanguageJoin.get("id"));
        selections.add(bookingInvoiceCountryManageLanguageJoin.get("code"));
        selections.add(bookingInvoiceCountryManageLanguageJoin.get("name"));
        selections.add(bookingInvoiceCountryManageLanguageJoin.get("defaults"));
        selections.add(bookingInvoiceCountryManageLanguageJoin.get("status"));
        selections.add(bookingInvoiceCountryManageLanguageJoin.get("createdAt"));
        selections.add(bookingInvoiceCountryManageLanguageJoin.get("updatedAt"));

        selections.add(bookingInvoiceCountryJoin.get("createdAt"));
        selections.add(bookingInvoiceCountryJoin.get("updateAt"));
        selections.add(bookingInvoiceCountryJoin.get("deleteAt"));
        selections.add(bookingInvoiceCountryJoin.get("iso3"));

        selections.add(bookingInvoiceAgencyJoin.get("createdAt"));
        selections.add(bookingInvoiceAgencyJoin.get("updatedAt"));

        selections.add(bookingInvoiceJoin.get("autoRec"));

        selections.add(bookingInvoiceManageStatus.get("id"));
        selections.add(bookingInvoiceManageStatus.get("code"));
        selections.add(bookingInvoiceManageStatus.get("name"));
        //Fin Invoice Parent

        //Booking parent
        selections.add(bookingBookingJoin.get("id"));
        selections.add(bookingBookingJoin.get("bookingId"));
        selections.add(bookingBookingJoin.get("reservationNumber"));
        selections.add(bookingBookingJoin.get("checkIn"));
        selections.add(bookingBookingJoin.get("checkOut"));
        selections.add(bookingBookingJoin.get("fullName"));
        selections.add(bookingBookingJoin.get("firstName"));
        selections.add(bookingBookingJoin.get("lastName"));
        selections.add(bookingBookingJoin.get("invoiceAmount"));
        selections.add(bookingBookingJoin.get("amountBalance"));
        selections.add(bookingBookingJoin.get("couponNumber"));
        selections.add(bookingBookingJoin.get("adults"));
        selections.add(bookingBookingJoin.get("children"));
        selections.add(bookingBookingJoin.get("bookingDate"));
        //Fin Booking parent

        selections.add(bookingJoin.get("bookingDate"));

        return selections;
    }

    private Booking convertTupleToBooking(Tuple tuple, int i){
        IndexRef index = new IndexRef(i);
        return this.bookingMapper.map(tuple, index, true);
        /*return new Booking(
                tuple.get(i++, UUID.class),
                tuple.get(i++, Long.class),
                tuple.get(i++, String.class),
                tuple.get(i++, LocalDateTime.class),
                tuple.get(i++, LocalDateTime.class),
                tuple.get(i++, String.class),
                tuple.get(i++, String.class),
                tuple.get(i++, String.class),
                tuple.get(i++, Double.class),
                tuple.get(i++, Double.class),
                tuple.get(i++, String.class),
                tuple.get(i++, Integer.class),
                tuple.get(i++, Integer.class),
                (tuple.get(i, UUID.class) != null) ? new Invoice(
                        tuple.get(i++, UUID.class),
                        tuple.get(i++, Long.class),
                        tuple.get(i++, Long.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, Double.class),
                        tuple.get(i++, Double.class),
                        tuple.get(i++, LocalDateTime.class),
                        tuple.get(i++, Boolean.class),
                        null,
                        tuple.get(i++, EInvoiceType.class),
                        null,
                        (tuple.get(i, UUID.class) != null) ? new ManageHotel(
                                tuple.get(i++, UUID.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, Boolean.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, Boolean.class),
                                tuple.get(i++, UUID.class),
                                tuple.get(i++, Boolean.class),
                                tuple.get(i++, LocalDateTime.class),
                                tuple.get(i++, LocalDateTime.class),
                                tuple.get(i++, LocalDateTime.class)
                        ) : skip( i += 11),
                        (tuple.get(i, UUID.class) != null) ? new ManageAgency(tuple.get(i++, UUID.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class),
                                tuple.get(i++, String.class),
                                (tuple.get(i, UUID.class) != null) ? new ManageAgencyType(
                                        tuple.get(i++, UUID.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class)
                                ) : skip(i += 4),
                                (tuple.get(i, UUID.class) != null) ? new ManageClient(
                                        tuple.get(i++, UUID.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class)
                                ) : skip( i+= 4),
                                (tuple.get(i, UUID.class) != null) ? new ManageCountry(
                                        tuple.get(i++, UUID.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, String.class),
                                        tuple.get(i++, Boolean.class),
                                        tuple.get(i++, String.class),
                                        (tuple.get(i, UUID.class) != null) ? new ManageLanguage(
                                                tuple.get(i++, UUID.class),
                                                tuple.get(i++, String.class),
                                                tuple.get(i++, String.class),
                                                tuple.get(i++, Boolean.class),
                                                tuple.get(i++, String.class),
                                                tuple.get(i++, LocalDateTime.class),
                                                tuple.get(i++, LocalDateTime.class)
                                        ) : skip( i+= 7),
                                        tuple.get(i++, LocalDateTime.class),
                                        tuple.get(i++, LocalDateTime.class),
                                        tuple.get(i++, LocalDateTime.class),
                                        tuple.get(i++, String.class)
                                ) : skip( i+= 17 ),
                                tuple.get(i++, LocalDateTime.class),
                                tuple.get(i++, LocalDateTime.class)
                        ) : skip( i += 31),
                        tuple.get(i++, Boolean.class),
                        (tuple.get(i, UUID.class) != null) ? new ManageInvoiceStatus(
                            tuple.get(i++, UUID.class),
                            tuple.get(i++, String.class),
                            tuple.get(i++, String.class)
                        ) : skip( i += 3)
                ) : skip( i += 55),
                (tuple.get(i, UUID.class) != null) ? new Booking(
                        tuple.get(i++, UUID.class),
                        tuple.get(i++, Long.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, LocalDateTime.class),
                        tuple.get(i++, LocalDateTime.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, Double.class),
                        tuple.get(i++, Double.class),
                        tuple.get(i++, String.class),
                        tuple.get(i++, Integer.class),
                        tuple.get(i++, Integer.class),
                        null,
                        null,
                        tuple.get(i++, LocalDateTime.class)
                ) : skip( i  += 14),
                tuple.get(i++, LocalDateTime.class)
        );
         */
    }

    @SuppressWarnings("unchecked")
    private <T> T skip(int i) {
        return null;
    }
}
