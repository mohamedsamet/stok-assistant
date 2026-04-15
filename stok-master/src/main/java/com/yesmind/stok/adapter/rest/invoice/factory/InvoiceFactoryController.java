package com.yesmind.stok.adapter.rest.invoice.factory;

import com.yesmind.stok.core.domain.data.InvoiceDto;
import com.yesmind.stok.core.port.in.invoice.IInvoiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class InvoiceFactoryController implements IInvoiceFactoryController {

    private final IInvoiceFactory invoiceFactory;


    @Override
    @PostMapping("/invoice")
    public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceDto invoiceDto) {
        return ResponseEntity.ok(invoiceFactory.buildInvoice(invoiceDto));
    }

    @Override
    @PostMapping("/invoice/draft")
    public ResponseEntity<InvoiceDto> createDraftInvoice() {
        return ResponseEntity.ok(invoiceFactory.buildDraftInvoice());
    }

    @Override
    @PostMapping("/invoice/by/bl")
    public ResponseEntity<InvoiceDto> createInvoicesByBl(@RequestBody List<String> references) {
        return ResponseEntity.ok(invoiceFactory.buildInvoiceByBl(references));
    }

    @PostMapping("/invoice/close/{publicId}")
    public ResponseEntity<InvoiceDto> closeInvoice(@PathVariable("publicId") UUID publicId) {
        return ResponseEntity.ok(invoiceFactory.closeInvoice(publicId));
    }

}
