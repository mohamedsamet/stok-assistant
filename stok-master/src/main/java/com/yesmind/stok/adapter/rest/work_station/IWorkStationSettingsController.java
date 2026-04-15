package com.yesmind.stok.adapter.rest.work_station;

import com.yesmind.stok.core.domain.data.TransformationsDto;
import com.yesmind.stok.core.domain.data.WorkStationDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IWorkStationSettingsController {

    ResponseEntity<WorkStationDto> addTransformations(TransformationsDto transformationDto);
    ResponseEntity<WorkStationDto> closeWorkStation(UUID workStationUuid);
}
