package com.yesmind.stok.core.domain.data;

import com.yesmind.stok.core.domain.entity.ActionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ActionDto {

    private UUID actionPublicID;

    private ActionType type;

    private String operator;

    private String description;
    private LocalDateTime dateTime;
}
