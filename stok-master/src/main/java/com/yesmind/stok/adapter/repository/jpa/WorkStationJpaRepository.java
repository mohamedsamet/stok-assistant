package com.yesmind.stok.adapter.repository.jpa;

import com.yesmind.stok.core.domain.entity.WorkStation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkStationJpaRepository extends JpaRepository<WorkStation, Long> {

    @Override
    WorkStation save(WorkStation product);

    Optional<WorkStation> findByPublicId(UUID publicId);

    @Query(value = "SELECT * FROM Work_Station WHERE " +
            "(deleted IS FALSE OR deleted IS NULL) AND (creation_status = 'COMPLETED') AND " +
            "(:name is null OR name like :name) ORDER BY id desc OFFSET :offset LIMIT :limit", nativeQuery = true)
    List<WorkStation> search(@Param("name") String name, @Param("offset") BigInteger offset, @Param("limit") Long limit);

    @Query(value = "SELECT count(p) FROM WorkStation p WHERE " +
            "(deleted IS FALSE OR deleted IS NULL) AND (creationStatus = 'COMPLETED') AND " +
            ":name is null OR p.name like :name")
    long countAll(@Param("name") String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WorkStation w WHERE w.creationDate = (SELECT MAX(creationDate) FROM WorkStation)")
    List<WorkStation> findLastWorkStationsEntry();
}
