package com.yesmind.stok.adapter.rest.invoice.factory;

import com.yesmind.stok.core.domain.data.InvoiceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

public interface IInvoiceFactoryController {

    ResponseEntity<InvoiceDto> createInvoice(InvoiceDto invoiceDto);

    ResponseEntity<InvoiceDto> createDraftInvoice();

    ResponseEntity<InvoiceDto> createInvoicesByBl(List<String> references);

    ResponseEntity<InvoiceDto> closeInvoice(UUID publicId);
}