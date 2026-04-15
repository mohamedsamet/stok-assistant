package com.yesmind.stok.adapter.repository.jpa;

import com.yesmind.stok.core.domain.entity.Client;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientJpaRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByNameAndDeletedIsFalseOrDeletedIsNull(String name);

    @Override
    Client save(Client client);

    Optional<Client> findByPublicId(UUID publicId);

    @Query(value = "SELECT * FROM Client WHERE " +
            "((deleted IS FALSE OR deleted IS NULL) AND (creation_status = 'COMPLETED') " +
            "AND (:name is null OR lower(name) like lower(:name))) ORDER BY id desc OFFSET :offset LIMIT :limit", nativeQuery = true)
    List<Client> search(@Param("name") String name, @Param("offset") BigInteger offset, @Param("limit") Long limit);

    @Query(value = "SELECT count(c) FROM Client c WHERE " +
            "((c.deleted IS FALSE OR c.deleted IS NULL) AND (creation_status = 'COMPLETED') " +
            "AND (:name is null OR LOWER(c.name) like LOWER(:name)))", nativeQuery = true)
    long countAll(@Param("name") String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Client c WHERE c.creationDate = (SELECT MAX(creationDate) FROM Client)")
    List<Client> findLastClientEntry();

}
