package com.yesmind.stok.core.business.invoice;

import com.yesmind.stok.application.exception.ConflictException;
import com.yesmind.stok.application.exception.ResourceNotFoundException;
import com.yesmind.stok.core.business.PriceToFrensh;
import com.yesmind.stok.core.business.ReferenceGenerationUtils;
import com.yesmind.stok.core.business.mapper.InvoiceMapper;
import com.yesmind.stok.core.domain.data.ClientDto;
import com.yesmind.stok.core.domain.data.InvoiceDto;
import com.yesmind.stok.core.domain.data.ProductInvoiceDto;
import com.yesmind.stok.core.domain.entity.*;
import com.yesmind.stok.core.port.in.invoice.IInvoiceFactory;
import com.yesmind.stok.core.port.in.product.IProductSearch;
import com.yesmind.stok.core.port.out.IClientRepository;
import com.yesmind.stok.core.port.out.IInvoiceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceFactoryService implements IInvoiceFactory {

    private final IInvoiceRepository invoiceRepository;
    private final IClientRepository clientRepository;
    private final IProductSearch productSearch;

    @Override
    @Transactional
    public InvoiceDto buildInvoice(InvoiceDto invoiceDto) {

        Invoice invoiceToBuild = invoiceRepository.getInvoiceByPublicId(invoiceDto.getPublicId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice Not found by public id" + invoiceDto.getPublicId()));

//        if (Boolean.TRUE.equals(invoiceToBuild.getClosed())) {
//            throw new ConflictException("Invoice already closed public id:" + invoiceDto.getPublicId());
//        }

        Client client = clientRepository.getClientByPublicId(invoiceDto.getClient().getPublicId())
                        .orElseThrow(() -> new ResourceNotFoundException("Client Not found by public id"));

        invoiceToBuild.setCreationStatus(CreationStatus.COMPLETED);
        invoiceToBuild.setClient(client);
        invoiceToBuild.setIsBl(invoiceDto.getIsBl());
        invoiceToBuild.setTimbre(invoiceDto.getTimbre());

        invoiceToBuild.getProductInvoices().forEach(productInvoice -> productInvoice.setDeleted(Boolean.TRUE));
        invoiceToBuild.setProductInvoices(getProductInvoices(invoiceDto.getProductInvoices(), invoiceToBuild));

        invoiceToBuild.setTotalBrut(
                invoiceToBuild.getProductInvoices().stream()
                        .filter(prod -> Boolean.FALSE.equals(prod.getDeleted()))
                        .map(ProductInvoice::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

        invoiceToBuild.setTotalTva(
                invoiceToBuild.getProductInvoices().stream()
                        .filter(prod -> Boolean.FALSE.equals(prod.getDeleted()))
                        .map(productInvoice -> productInvoice.getTotalPrice()
                                .multiply(BigDecimal.valueOf(productInvoice.getTva()))
                                .setScale(10, RoundingMode.FLOOR)
                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP))
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

        invoiceToBuild.setTotalNet(
                invoiceToBuild.getTotalBrut()
                        .add(invoiceToBuild.getTotalTva())
                        .add(invoiceToBuild.getTimbre() == null ? BigDecimal.ZERO : BigDecimal.valueOf(invoiceToBuild.getTimbre()))
                        .setScale(3, RoundingMode.HALF_UP));

        invoiceToBuild.setPriceAsText(PriceToFrensh.priceToFrenchText(invoiceToBuild.getTotalNet()));
        Invoice savedInvoice = invoiceRepository.saveInvoice(invoiceToBuild);

        return InvoiceMapper.toInvoiceDto(savedInvoice);

    }

    @Override
    @Transactional
    public InvoiceDto closeInvoice(UUID invoicePublicId) {
        Invoice invoiceToClose = invoiceRepository.getInvoiceByPublicId(invoicePublicId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice Not found by public id" + invoicePublicId));

        invoiceToClose.setClosed(Boolean.TRUE);

        return InvoiceMapper.toInvoiceDto(invoiceRepository.saveInvoice(invoiceToClose));
    }

    private List<ProductInvoice> getProductInvoices(List<ProductInvoiceDto> productInvoices, Invoice invoice) {
        Map<UUID, Product> productMap = productSearch.findByUuids(
                productInvoices.stream().map(ProductInvoiceDto::getProductPublicId).collect(Collectors.toSet()));
        return new ArrayList<>(productInvoices.stream()
                .map(productInvoiceDto -> InvoiceMapper.toProductInvoice(
                        productInvoiceDto,
                        productMap.get(productInvoiceDto.getProductPublicId()),
                        invoice))
               .toList());
    }

    @Transactional
    @Override
    public InvoiceDto buildDraftInvoice() {
        Optional<Invoice> lastInvoiceEntry = invoiceRepository.findLastInvoice();

        String reference = lastInvoiceEntry
                .map(invoice -> ReferenceGenerationUtils.generateInvoiceReference(invoice.getReference()))
                .orElseGet(ReferenceGenerationUtils::generateFirstInvoiceReference);

        Invoice savedInvoice = invoiceRepository.saveInvoice(InvoiceMapper.toInvoiceByReference(reference));
        return InvoiceMapper.toInvoiceDto(savedInvoice);
    }

    @Override
    public String incrementInvoiceReference(UUID invoicePublicId) {
        Optional<Invoice> maxInvoiceRef = invoiceRepository.findLastInvoiceReference();

        Invoice invoiceToUpdate = invoiceRepository.getInvoiceByPublicId(invoicePublicId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice Not found by public id" + invoicePublicId));

        if (invoiceToUpdate.getReferenceInvoice() != null) {
            return invoiceToUpdate.getReferenceInvoice().toString();
        }
        String reference = maxInvoiceRef
                .map(ref -> ReferenceGenerationUtils.generateInvoiceReference(ref.getReferenceInvoice().toString()))
                .orElseGet(ReferenceGenerationUtils::generateFirstInvoiceReference);

        invoiceToUpdate.setReferenceInvoice(Long.parseLong(reference));
        invoiceRepository.saveInvoice(invoiceToUpdate);
        return reference;
    }

    @Override
    public String incrementBlReference(UUID invoicePublicId) {
        Optional<Invoice> maxIBlRef = invoiceRepository.findLastBlReference();

        Invoice invoiceToUpdate = invoiceRepository.getInvoiceByPublicId(invoicePublicId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice Not found by public id" + invoicePublicId));

        if (invoiceToUpdate.getReferenceBl() != null) {
            return invoiceToUpdate.getReferenceBl().toString();
        }

        String reference = maxIBlRef
                .map(ref -> ReferenceGenerationUtils.generateInvoiceReference(ref.getReferenceBl().toString()))
                .orElseGet(ReferenceGenerationUtils::generateFirstInvoiceReference);

        invoiceToUpdate.setReferenceBl(Long.parseLong(reference));
        invoiceRepository.saveInvoice(invoiceToUpdate);
        return reference;
    }

    @Override
    @Transactional
    public InvoiceDto buildInvoiceByBl(List<String> references) {

        List<Invoice> bls = invoiceRepository.findByBlsReferences(references);

        InvoiceDto invoiceDto = InvoiceDto.builder()
                .publicId(this.buildDraftInvoice().getPublicId())
                .isBl(false)
                .client(ClientDto.builder()
                        .publicId(bls.get(0).getClient().getPublicId())
                        .build())
                .productInvoices(bls.stream()
                        .flatMap(invoice -> invoice.getProductInvoices().stream())
                        .filter(product -> product.getDeleted() == null || Boolean.FALSE.equals(product.getDeleted()))
                        .map(InvoiceMapper::toProductInvoiceDto)
                        .toList())
                .build();

        return buildInvoice(invoiceDto);
    }

}
