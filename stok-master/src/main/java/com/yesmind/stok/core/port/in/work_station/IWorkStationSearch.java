package com.yesmind.stok.core.port.in.work_station;

import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.data.WorkStationDto;
import com.yesmind.stok.core.domain.data.WorkStationResponse;

import java.util.List;
import java.util.UUID;

public interface IWorkStationSearch {

    WorkStationResponse searchWorkStations(SearchDto searchDto);
    WorkStationDto findWorkStation(UUID workStationUuid);
}
