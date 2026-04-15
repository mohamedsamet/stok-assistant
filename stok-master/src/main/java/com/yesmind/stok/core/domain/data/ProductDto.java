package com.yesmind.stok.core.domain.data;

import com.yesmind.stok.core.domain.entity.CreationStatus;
import com.yesmind.stok.core.domain.entity.ProductType;
import com.yesmind.stok.core.domain.entity.Unit;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String name;
    private String provider;
    @Setter
    private BigDecimal quantity;
    @Setter
    private BigDecimal initialValue;
    private Unit unit;
    private ProductType type;
    private UUID publicId;
    private String reference;
    private LocalDateTime creationDate;
    private BigDecimal price;
    @Setter
    private CreationStatus creationStatus;
}
