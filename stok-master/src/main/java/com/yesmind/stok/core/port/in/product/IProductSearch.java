package com.yesmind.stok.core.port.in.product;

import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.domain.data.ProductResponse;
import com.yesmind.stok.core.domain.data.SearchProductDto;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.domain.entity.ProductType;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IProductSearch {

    ProductDto findByUuid(UUID productUuid);
    Map<UUID, Product> findByUuids(Set<UUID> productUuid);
    ProductResponse search(SearchProductDto searchProductDto);
}
