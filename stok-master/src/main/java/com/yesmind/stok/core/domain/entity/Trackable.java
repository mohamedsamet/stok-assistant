package com.yesmind.stok.core.domain.entity;

import java.util.UUID;

public interface Trackable {

    ActionType getActionType();
    UUID getPublicId();
    String getDescription();
    String getUser();
    long getRefIncrement();
}
