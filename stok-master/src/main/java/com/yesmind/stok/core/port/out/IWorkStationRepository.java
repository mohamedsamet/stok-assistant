package com.yesmind.stok.core.port.out;

import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.entity.WorkStation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IWorkStationRepository {

    WorkStation saveWorkStation(WorkStation workStation);
    Optional<WorkStation> findWorkStationByUuid(UUID publicId);
    Long count(SearchDto searchDto);
    List<WorkStation> searchStations(SearchDto searchDto);
    Optional<WorkStation> findLastWorkStation();
}
