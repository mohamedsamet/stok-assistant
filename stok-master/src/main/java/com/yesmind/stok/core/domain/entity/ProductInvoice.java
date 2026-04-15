package com.yesmind.stok.core.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductInvoice {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private UUID publicId = UUID.randomUUID();

    private UUID productPublicId;
    private String productName;
    private String productReference;
    private String unit;

    @Column(precision = 11, scale = 3)
    private BigDecimal quantity;

    @Column(precision = 11, scale = 3)
    private BigDecimal unitPrice;

    private Float discount;

    @Column(precision = 11, scale = 3)
    private BigDecimal totalPrice;

    private Float tva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="invoice_id")
    private Invoice invoice;

    private Boolean deleted;

}
