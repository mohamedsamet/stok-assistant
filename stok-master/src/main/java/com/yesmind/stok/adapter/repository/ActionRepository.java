package com.yesmind.stok.adapter.repository;

import com.yesmind.stok.adapter.repository.jpa.ActionJpaRepository;
import com.yesmind.stok.core.domain.entity.Action;
import com.yesmind.stok.core.domain.entity.ActionType;
import com.yesmind.stok.core.port.out.IActionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ActionRepository implements IActionRepository {

    private final ActionJpaRepository actionJpaRepository;

    @Override
    public void saveAction(Action action) {
        this.actionJpaRepository.save(action);
    }

    @Override
    public List<Action> findAll() {
        return actionJpaRepository.findAll();
    }

    @Override
    public List<Action> findByType(ActionType actionType) {
        return actionJpaRepository.findByType(actionType);
    }
}
