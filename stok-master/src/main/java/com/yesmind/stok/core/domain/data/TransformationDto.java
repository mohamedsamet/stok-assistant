package com.yesmind.stok.core.domain.data;

import com.yesmind.stok.core.domain.entity.TransformationType;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransformationDto {

    @NonNull
    private UUID productPublicId;

    private BigDecimal quantity;
    private UUID publicId;

    private TransformationType type;
    private ProductDto product;
}
