package com.yesmind.stok.adapter.rest.invoice.search;

import com.yesmind.stok.core.domain.data.InvoiceDto;
import com.yesmind.stok.core.domain.data.InvoiceResponse;
import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.data.SearchInvoiceDto;
import com.yesmind.stok.core.port.in.invoice.IInvoiceSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class InvoiceSearchController implements IInvoiceSearchController {

    private final IInvoiceSearch invoiceSearch;

    @Override
    @PostMapping(value = "/invoice/search")
    public ResponseEntity<InvoiceResponse> searchInvoices(@RequestBody SearchDto searchDto) {
        return ResponseEntity.ok(invoiceSearch.search(searchDto));
    }

    @Override
    @PostMapping(value = "/invoice/bl/search")
    public ResponseEntity<InvoiceResponse> searchBls(@RequestBody SearchDto searchDto) {
        return ResponseEntity.ok(invoiceSearch.searchBl(searchDto));
    }

    @Override
    @GetMapping("/invoice/search/{uuid}")
    public ResponseEntity<InvoiceDto> searchByUuid(@PathVariable("uuid") UUID invoiceUuid) {
        return ResponseEntity.ok(invoiceSearch.findByUuid(invoiceUuid));
    }
}
