package com.yesmind.stok.core.domain.data;

import com.yesmind.stok.core.domain.entity.ProductType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchProductDto extends SearchDto {

    @Setter
    @Builder.Default
    private List<ProductType> types = new ArrayList<>();
}
