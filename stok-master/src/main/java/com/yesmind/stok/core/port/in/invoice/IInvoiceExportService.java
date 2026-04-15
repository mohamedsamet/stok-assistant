package com.yesmind.stok.core.port.in.invoice;

import com.yesmind.stok.application.exception.TemplateNotFoundException;

import java.util.UUID;

public interface IInvoiceExportService {
    byte[] export(UUID publicId) throws TemplateNotFoundException;

    byte[] exportBl(UUID publicId) throws TemplateNotFoundException;
}
