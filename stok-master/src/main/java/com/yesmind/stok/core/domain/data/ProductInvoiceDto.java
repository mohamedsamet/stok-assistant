package com.yesmind.stok.core.domain.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInvoiceDto {

    private UUID publicId;
    private UUID productPublicId;
    private String productName;
    private String productReference;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String unit;
    private Float discount;
    private BigDecimal totalPrice;
    private Float tva;
}
