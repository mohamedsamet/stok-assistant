package com.yesmind.stok.core.domain.data;

import com.yesmind.stok.core.domain.entity.CreationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {

    private UUID publicId;
    private ClientDto client;
    private String priceAsText;
    private LocalDateTime creationDate;
    private String reference;
    private BigDecimal totalBrut;
    private BigDecimal totalTva;
    private Float timbre;
    private Boolean closed;
    private BigDecimal totalNet;
    @Setter
    private CreationStatus creationStatus;

    List<ProductInvoiceDto> productInvoices;
    private Boolean isBl;

}
