package com.yesmind.stok.core.domain.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {

    private List<InvoiceDto> invoices;
    private long count;
}
