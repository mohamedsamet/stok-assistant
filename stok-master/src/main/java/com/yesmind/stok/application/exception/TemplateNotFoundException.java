package com.yesmind.stok.application.exception;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;

@Slf4j
public class TemplateNotFoundException extends FileNotFoundException {

    public TemplateNotFoundException(String message) {
        super("Template file not found : " + message);
        log.error("Template file not found : " + message);
    }

}
