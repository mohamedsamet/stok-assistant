package com.yesmind.stok.adapter.repository;

import com.yesmind.stok.adapter.repository.jpa.InvoiceJpaRepository;
import com.yesmind.stok.core.domain.data.SearchDto;
import com.yesmind.stok.core.domain.entity.Invoice;
import com.yesmind.stok.core.port.out.IInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class InvoiceRepository implements IInvoiceRepository {

    private final InvoiceJpaRepository invoiceJpaRepository;

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        return invoiceJpaRepository.save(invoice);
    }

    @Override
    public Optional<Invoice> getInvoiceByPublicId(UUID publicId) {
        return invoiceJpaRepository.findByPublicId(publicId);
    }

    @Override
    public List<Invoice> searchInvoices(SearchDto searchDto) {
        long limit = searchDto.getPageSize() != 0 ? searchDto.getPageSize() : 10;
        BigInteger offset = BigInteger.valueOf(searchDto.getPage() * limit);
        return invoiceJpaRepository.searchInvoices(searchDto.getName(), searchDto.getClientPublicId(), offset, limit);
    }

    @Override
    public Long count(SearchDto searchDto) {
        return invoiceJpaRepository.countInvoices(searchDto.getName(), searchDto.getClientPublicId());
    }

    @Override
    public List<Invoice> searchBls(SearchDto searchDto) {
        long limit = searchDto.getPageSize() != 0 ? searchDto.getPageSize() : 10;
        BigInteger offset = BigInteger.valueOf(searchDto.getPage() * limit);
        return invoiceJpaRepository.searchBls(searchDto.getName(), searchDto.getClientPublicId(), offset, limit);
    }

    @Override
    public Long countBls(SearchDto searchDto) {
        return invoiceJpaRepository.countBls(searchDto.getName(), searchDto.getClientPublicId());
    }

    @Override
    public Optional<Invoice> findLastInvoice() {
        return invoiceJpaRepository.findLastInvoiceEntry().stream().max(
                Comparator.comparingLong(Invoice::getRefIncrement)
        );
    }

    @Override
    public Optional<Invoice> findLastInvoiceReference() {
        return invoiceJpaRepository.findTopByReferenceInvoiceIsNotNullOrderByReferenceInvoiceDesc();
    }

    @Override
    public Optional<Invoice> findLastBlReference() {
        return invoiceJpaRepository.findTopByReferenceBlIsNotNullOrderByReferenceBlDesc();
    }

    @Override
    public List<Invoice> findByBlsReferences(List<String> references) {
        return invoiceJpaRepository.findByReferenceInAndIsBlIsTrue(references);
    }

}
