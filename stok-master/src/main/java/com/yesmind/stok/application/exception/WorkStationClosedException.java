package com.yesmind.stok.application.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkStationClosedException extends RuntimeException {

    public WorkStationClosedException(String message) {
        super(message);
        log.error("Work Station Closed, Description : " + message);
    }

}
