package com.yesmind.stok.adapter.repository.jpa;

import com.yesmind.stok.core.domain.entity.Invoice;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceJpaRepository extends JpaRepository<Invoice, Long> {


    @Override
    Invoice save(Invoice invoice);

    Optional<Invoice> findByPublicId(UUID publicId);

    @Query(value = " SELECT i.* FROM invoice i " +
            "JOIN client c ON i.client_id = c.id WHERE i.creation_status = 'COMPLETED' AND i.is_bl = FALSE " +
            "AND (:clientPublicId IS NULL OR c.public_id = :clientPublicId) " +
            "AND (:reference IS NULL OR lower(i.reference) LIKE lower(:reference) " +
            "OR lower(c.name) LIKE lower(:reference)) " +
            "ORDER BY i.id DESC OFFSET CAST(:offset AS BIGINT) LIMIT CAST(:limit AS BIGINT) ", nativeQuery = true)
    List<Invoice> searchInvoices(@Param("reference") String reference, @Param("clientPublicId") UUID clientPublicId, @Param("offset") BigInteger offset, @Param("limit") Long limit);

    @Query(value = "SELECT count(i) FROM Invoice i WHERE " +
            "((i.creationStatus = 'COMPLETED') AND i.isBl = FALSE AND " +
            "(:clientPublicId is null or i.client.publicId = :clientPublicId) AND " +
            "(i.reference like :reference or lower(i.client.name) like lower(:reference) or :reference is null))")
    long countInvoices(@Param("reference") String reference, @Param("clientPublicId") UUID clientPublicId);

    @Query(value = " SELECT i.* FROM invoice i " +
            "JOIN client c ON i.client_id = c.id WHERE i.creation_status = 'COMPLETED'  AND IS_BL = TRUE " +
            "AND (:clientPublicId IS NULL OR c.public_id = :clientPublicId) " +
            "AND (:reference IS NULL OR lower(i.reference) LIKE lower(:reference) " +
            "OR lower(c.name) LIKE lower(:reference)) " +
            "ORDER BY i.id DESC OFFSET CAST(:offset AS BIGINT) LIMIT CAST(:limit AS BIGINT) ", nativeQuery = true)
    List<Invoice> searchBls(@Param("reference") String reference, @Param("clientPublicId") UUID clientPublicId, @Param("offset") BigInteger offset, @Param("limit") Long limit);

    @Query(value = "SELECT count(i) FROM Invoice i WHERE " +
            "((i.creationStatus = 'COMPLETED') AND i.isBl = TRUE AND " +
            "(:clientPublicId is null or i.client.publicId = :clientPublicId) AND " +
            "(i.reference like :reference or lower(i.client.name) like lower(:reference) or :reference is null))")
    long countBls(@Param("reference") String reference, @Param("clientPublicId") UUID clientPublicId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Invoice i WHERE i.creationDate = (SELECT MAX(creationDate) FROM Invoice)")
    List<Invoice> findLastInvoiceEntry();

    Optional<Invoice> findTopByReferenceInvoiceIsNotNullOrderByReferenceInvoiceDesc();
    Optional<Invoice> findTopByReferenceBlIsNotNullOrderByReferenceBlDesc();
    List<Invoice> findByReferenceInAndIsBlIsTrue(List<String> references);
}
