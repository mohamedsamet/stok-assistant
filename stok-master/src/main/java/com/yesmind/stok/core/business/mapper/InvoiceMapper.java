package com.yesmind.stok.core.business.mapper;

import com.yesmind.stok.core.domain.data.InvoiceDto;
import com.yesmind.stok.core.domain.data.ProductInvoiceDto;
import com.yesmind.stok.core.domain.entity.CreationStatus;
import com.yesmind.stok.core.domain.entity.Invoice;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.domain.entity.ProductInvoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceMapper {

    private InvoiceMapper() {}

    public static InvoiceDto toInvoiceDto(Invoice invoice) {
        return InvoiceDto.builder()
                .client(invoice.getClient() != null ? ClientMapper.toClientDto(invoice.getClient()) : null)
                .timbre(invoice.getTimbre())
                .priceAsText(invoice.getPriceAsText())
                .totalNet(invoice.getTotalNet())
                .totalTva(invoice.getTotalTva())
                .totalBrut(invoice.getTotalBrut())
                .reference(invoice.getReference())
                .publicId(invoice.getPublicId())
                .closed(invoice.getClosed())
                .creationDate(invoice.getCreationDate())
                .productInvoices(toProductInvoicesDto(invoice.getProductInvoices()))
                .build();
    }

    private static List<ProductInvoiceDto> toProductInvoicesDto(List<ProductInvoice> productInvoices) {
        return productInvoices.stream()
                .filter(productInv -> Boolean.FALSE.equals(productInv.getDeleted()))
                .map(InvoiceMapper::toProductInvoiceDto)
                .toList();

    }

    public static ProductInvoiceDto toProductInvoiceDto(ProductInvoice productInvoice) {
        return ProductInvoiceDto.builder()
                .productPublicId(productInvoice.getProductPublicId())
                .productName(productInvoice.getProductName())
                .productReference(productInvoice.getProductReference())
                .quantity(productInvoice.getQuantity())
                .tva(productInvoice.getTva())
                .discount(productInvoice.getDiscount())
                .totalPrice(productInvoice.getTotalPrice())
                .unitPrice(productInvoice.getUnitPrice())
                .unit(productInvoice.getUnit())
                .build();
    }

    public static ProductInvoice toProductInvoice(ProductInvoiceDto productInvoiceDto, Product product, Invoice invoice) {
        return ProductInvoice.builder()
                .productPublicId(product.getPublicId())
                .productName(product.getName())
                .productReference(product.getReference())
                .quantity(productInvoiceDto.getQuantity())
                .tva(productInvoiceDto.getTva())
                .discount(productInvoiceDto.getDiscount())
                .unit(productInvoiceDto.getUnit())
                .invoice(invoice)
                .deleted(false)
                .totalPrice(
                        productInvoiceDto.getUnitPrice()
                                .subtract((productInvoiceDto.getUnitPrice()
                                        .multiply(productInvoiceDto.getDiscount() == null ? BigDecimal.ZERO : BigDecimal.valueOf(productInvoiceDto.getDiscount()))
                                        .setScale(10, RoundingMode.FLOOR)
                                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)))
                                .multiply(productInvoiceDto.getQuantity()))
                .unitPrice(productInvoiceDto.getUnitPrice())
                .build();
    }

    public static Invoice toInvoiceByReference(String reference) {
        return Invoice.builder()
                .creationDate(LocalDateTime.now())
                .reference(reference)
                .creationStatus(CreationStatus.DRAFT)
                .build();
    }
}
