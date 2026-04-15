package com.yesmind.stok.adapter.rest.export;

import com.yesmind.stok.application.exception.TemplateNotFoundException;
import com.yesmind.stok.core.domain.data.InvoiceDto;
import com.yesmind.stok.core.domain.data.InvoiceResponse;
import com.yesmind.stok.core.domain.data.SearchDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface IExportController {

    ResponseEntity<byte[]> exportInvoice(UUID publicId) throws TemplateNotFoundException;
    ResponseEntity<byte[]> exportBl(UUID publicId) throws TemplateNotFoundException;

}
