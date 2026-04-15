package com.yesmind.stok.core.port.in.invoice;

import com.yesmind.stok.core.domain.data.InvoiceDto;
import com.yesmind.stok.core.domain.data.InvoiceResponse;
import com.yesmind.stok.core.domain.data.SearchDto;

import java.util.UUID;

public interface IInvoiceSearch {

    InvoiceDto findByUuid(UUID invoiceUUid);
    InvoiceResponse search(SearchDto searchDto);
    InvoiceResponse searchBl(SearchDto searchDto);
}
