package com.yesmind.stok.core.port.in.invoice;

import com.yesmind.stok.core.domain.data.InvoiceDto;

import java.util.List;
import java.util.UUID;

public interface IInvoiceFactory {

    InvoiceDto buildInvoice(InvoiceDto invoiceDto);
    InvoiceDto closeInvoice(UUID invoicePublicId);
    InvoiceDto buildDraftInvoice();
    String incrementInvoiceReference(UUID invoice);
    String incrementBlReference(UUID invoice);

    InvoiceDto buildInvoiceByBl(List<String> references);
}
