package com.yesmind.stok.adapter.rest.product.search;

import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.domain.data.ProductResponse;
import com.yesmind.stok.core.domain.data.SearchProductDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IProductSearchController {

    ResponseEntity<ProductResponse> searchProducts(SearchProductDto searchProductDto);
    ResponseEntity<ProductDto> searchProductByUuid(UUID productUuid);
}
