package com.yesmind.stok.core.business;

import com.yesmind.stok.adapter.repository.ActionRepository;
import com.yesmind.stok.core.business.mapper.ActionMapper;
import com.yesmind.stok.core.domain.data.ActionDto;
import com.yesmind.stok.core.domain.entity.Action;
import com.yesmind.stok.core.domain.entity.ActionType;
import com.yesmind.stok.core.domain.entity.WorkStation;
import com.yesmind.stok.core.port.in.service.IActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionService implements IActionService {

    private final ActionRepository actionRepository;

    @Override
    public void record(ActionDto actionRequest) {
        Action action = Action.builder()
                .actionPublicId(actionRequest.getActionPublicID())
                .dateTime(LocalDateTime.now())
                .description(actionRequest.getDescription())
                .type(actionRequest.getType())
                .operator(actionRequest.getOperator())
                .build();

        actionRepository.saveAction(action);
    }

    @Override
    public void manageWorkStationActions(WorkStation workStation) {
        if (workStation.getTransformations().isEmpty() || workStation.isClosed()) {
            record(ActionDto.builder()
                    .actionPublicID(workStation.getPublicId())
                    .type(workStation.getActionType())
                    .description(workStation.getDescription())
                    .operator(workStation.getUser())
                    .build());
        } else {
            saveTransformationsActions(workStation);
        }
    }

    private void saveTransformationsActions(WorkStation workStation) {
        workStation.getTransformations()
                .forEach(trans ->
                        record(ActionDto.builder()
                                .actionPublicID(trans.getPublicId())
                                .operator("")
                                .type(ActionType.TRANSFORMATION)
                                .description("Transformation: Station: " + trans.getWorkStation().getName()
                                        + " Product: " + trans.getProduct().getName() + " Q: " + trans.getProduct().getQuantity().toString()
                                        + " Type: " + trans.getType().name() + " " + trans.getQuantity().toString()
                                ).build())
                );
    }

    @Override
    public List<ActionDto> findAll() {
        return actionRepository.findAll().stream()
                .map(ActionMapper::toActionDto)
                .toList();
    }

    @Override
    public List<ActionDto> findByType(ActionType actionType) {
        return actionRepository.findByType(actionType).stream()
                .map(ActionMapper::toActionDto)
                .toList();
    }
}
