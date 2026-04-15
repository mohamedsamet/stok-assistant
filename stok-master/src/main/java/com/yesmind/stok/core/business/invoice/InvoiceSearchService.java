package com.yesmind.stok.core.business.invoice;

import com.yesmind.stok.application.exception.ResourceNotFoundException;
import com.yesmind.stok.core.business.mapper.InvoiceMapper;
import com.yesmind.stok.core.domain.data.InvoiceDto;
import com.yesmind.stok.core.domain.data.InvoiceResponse;
import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.data.SearchInvoiceDto;
import com.yesmind.stok.core.domain.entity.Invoice;
import com.yesmind.stok.core.port.in.invoice.IInvoiceSearch;
import com.yesmind.stok.core.port.out.IInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceSearchService implements IInvoiceSearch {

    private final IInvoiceRepository invoiceRepository;

    @Override
    public InvoiceDto findByUuid(UUID invoiceUUid) {
        Optional<Invoice> invoice = invoiceRepository.getInvoiceByPublicId(invoiceUUid);

        if (invoice.isPresent()) {
            return InvoiceMapper.toInvoiceDto(invoice.get());
        }

        throw new ResourceNotFoundException(String.format("Invoice with UUID %s Not Found", invoiceUUid));
    }

    @Override
    public InvoiceResponse search(SearchDto searchDto) {
        if (searchDto.getName() != null) {
            searchDto.setName("%" + searchDto.getName() + "%");
        }
        List<InvoiceDto> invoiceList = invoiceRepository.searchInvoices(searchDto)
                .stream()
                .map(InvoiceMapper::toInvoiceDto)
                .toList();

        return InvoiceResponse.builder()
                .invoices(invoiceList)
                .count(invoiceRepository.count(searchDto))
                .build();

    }

    @Override
    public InvoiceResponse searchBl(SearchDto searchDto) {
        if (searchDto.getName() != null) {
            searchDto.setName("%" + searchDto.getName() + "%");
        }
        List<InvoiceDto> invoiceList = invoiceRepository.searchBls(searchDto)
                .stream()
                .map(InvoiceMapper::toInvoiceDto)
                .toList();

        return InvoiceResponse.builder()
                .invoices(invoiceList)
                .count(invoiceRepository.countBls(searchDto))
                .build();
    }

}
