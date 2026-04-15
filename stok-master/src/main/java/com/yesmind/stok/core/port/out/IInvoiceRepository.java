package com.yesmind.stok.core.port.out;

import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.entity.Invoice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IInvoiceRepository {

    Invoice saveInvoice(Invoice invoice);
    Optional<Invoice> getInvoiceByPublicId(UUID publicId);
    List<Invoice> searchInvoices(SearchDto searchDto);
    Long count(SearchDto searchDto);

    List<Invoice> searchBls(SearchDto searchDto);
    Long countBls(SearchDto searchDto);
    Optional<Invoice> findLastInvoice();
    Optional<Invoice> findLastInvoiceReference();
    Optional<Invoice> findLastBlReference();

    List<Invoice> findByBlsReferences(List<String> references);
}
