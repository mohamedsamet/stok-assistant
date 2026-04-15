package com.yesmind.stok.core.port.in.service;

import com.yesmind.stok.core.domain.data.ActionDto;
import com.yesmind.stok.core.domain.entity.ActionType;
import com.yesmind.stok.core.domain.entity.WorkStation;

import java.util.List;

public interface IActionService {

    void record(ActionDto actionRequest);

    List<ActionDto> findAll();

    List<ActionDto> findByType(ActionType actionType);

    void manageWorkStationActions(WorkStation workStation);
}
