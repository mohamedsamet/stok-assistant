package com.yesmind.stok.core.port.in.work_station;

import com.yesmind.stok.core.domain.data.WorkStationDto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IWorkStationFactory {

    WorkStationDto buildWorkStation(WorkStationDto workStationDto);
    WorkStationDto updateWorkStation(UUID publicId, WorkStationDto workStationDto);
    WorkStationDto buildDraftWorkStation();
    void deleteWorkStation(UUID publicId);
}
