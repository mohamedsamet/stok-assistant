package com.yesmind.stok.core.business.mapper;

import com.yesmind.stok.core.domain.data.ActionDto;
import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.domain.entity.Action;
import com.yesmind.stok.core.domain.entity.Product;

import java.math.BigDecimal;

public class ActionMapper {

    private ActionMapper() {}

    public static ActionDto toActionDto(Action action) {
        return ActionDto.builder()
                .actionPublicID(action.getActionPublicId())
                .description(action.getDescription())
                .operator(action.getOperator())
                .type(action.getType())
                .dateTime(action.getDateTime())
                .build();
    }

}
