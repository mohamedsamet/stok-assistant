package com.yesmind.stok.core.port.out;

import com.yesmind.stok.core.domain.entity.Action;
import com.yesmind.stok.core.domain.entity.ActionType;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.domain.entity.ProductType;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IActionRepository {

    void saveAction(Action action);
    List<Action> findAll();
    List<Action> findByType(ActionType actionType);

}
