package com.yesmind.stok.application.exception;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;

@Slf4j
public class InvoiceGenerationException extends RuntimeException {

    public InvoiceGenerationException(Exception ex) {
        super(ex);
    }

    public InvoiceGenerationException(String message) {
        super(message);
    }

}
