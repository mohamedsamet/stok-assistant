package com.yesmind.stok.adapter.repository;

import com.yesmind.stok.adapter.repository.jpa.WorkStationJpaRepository;
import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.domain.entity.WorkStation;
import com.yesmind.stok.core.port.out.IWorkStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class WorkStationRepository implements IWorkStationRepository {

    private final WorkStationJpaRepository workStationJpaRepository;

    @Override
    public WorkStation saveWorkStation(WorkStation workStation) {
        return workStationJpaRepository.save(workStation);
    }

    @Override
    public Optional<WorkStation> findWorkStationByUuid(UUID publicId) {
        return workStationJpaRepository.findByPublicId(publicId);
    }

    @Override
    public List<WorkStation> searchStations(SearchDto searchDto) {
        long limit = 10;
        BigInteger offset = BigInteger.valueOf(searchDto.getPage() * limit);

        return workStationJpaRepository.search(searchDto.getName(), offset, limit);
    }

    @Override
    public Optional<WorkStation> findLastWorkStation() {
        return workStationJpaRepository.findLastWorkStationsEntry().stream().max(
                Comparator.comparingLong(WorkStation::getRefIncrement)
        );
    }

    @Override
    public Long count(SearchDto searchDto) {
        return workStationJpaRepository.countAll(searchDto.getName());
    }
}
