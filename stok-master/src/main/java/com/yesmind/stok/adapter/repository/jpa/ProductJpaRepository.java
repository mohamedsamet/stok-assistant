package com.yesmind.stok.adapter.repository.jpa;

import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.domain.entity.ProductType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByNameAndDeletedIsFalseOrDeletedIsNull(String name);

    @Override
    Product save(Product product);

    Optional<Product> findByPublicId(UUID publicId);

    List<Product> findByPublicIdInAndDeletedIsFalseOrDeletedIsNull(Set<UUID> publicIds);

    @Query(value = "SELECT * FROM Product WHERE " +
            "((type in (:types)) AND (deleted IS FALSE OR deleted IS NULL) AND (creation_status = 'COMPLETED') " +
            "AND (:name is null OR lower(name) like lower(:name) or lower(reference) like lower(:name))) ORDER BY id desc OFFSET :offset LIMIT :limit", nativeQuery = true)
    List<Product> search(@Param("name") String name, @Param("types") List<String> types, @Param("offset") BigInteger offset, @Param("limit") Long limit);

    @Query("SELECT COUNT(p) FROM Product p WHERE " +
            "(p.type IN :types) " +
            "AND (p.deleted = false OR p.deleted IS NULL) " +
            "AND (p.creationStatus = 'COMPLETED') " +
            "AND (LOWER(p.name) LIKE LOWER(:name) OR LOWER(p.reference) LIKE LOWER(:name) OR :name IS NULL)")
    long countAll(@Param("name") String name, @Param("types") List<ProductType> types);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.creationDate = (SELECT MAX(creationDate) FROM Product)")
    List<Product> findLastProductsEntry();

}
