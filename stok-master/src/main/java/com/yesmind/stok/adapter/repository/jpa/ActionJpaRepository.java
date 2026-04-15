package com.yesmind.stok.adapter.repository.jpa;

import com.yesmind.stok.core.domain.entity.Action;
import com.yesmind.stok.core.domain.entity.ActionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActionJpaRepository extends JpaRepository<Action, Long> {

    @Override
    Action save(Action action);

    @Override
    @Query("select ac from Action ac order by dateTime desc")
    List<Action> findAll();

    @Query("select ac from Action ac where type=:actionType order by dateTime desc")
    List<Action> findByType(@Param("actionType") ActionType actionType);

}
