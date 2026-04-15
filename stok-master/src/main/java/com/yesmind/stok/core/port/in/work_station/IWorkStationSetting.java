package com.yesmind.stok.core.port.in.work_station;

import com.yesmind.stok.core.domain.data.TransformationsDto;
import com.yesmind.stok.core.domain.data.WorkStationDto;

import java.util.UUID;

public interface IWorkStationSetting {

    WorkStationDto setTransformations(TransformationsDto transformationsDto);
    WorkStationDto closeWorkStation(UUID workStationPublicId);
}
