package com.yesmind.stok.adapter.rest.invoice.search;

import com.yesmind.stok.core.domain.data.InvoiceDto;
import com.yesmind.stok.core.domain.data.InvoiceResponse;
import com.yesmind.stok.core.domain.data.SearchDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IInvoiceSearchController {

    ResponseEntity<InvoiceResponse> searchInvoices(SearchDto searchDto);
    ResponseEntity<InvoiceResponse> searchBls(SearchDto searchDto);
    ResponseEntity<InvoiceDto> searchByUuid(UUID searchUuid);
}
