package com.yesmind.stok.adapter.rest.product.factory;

import com.yesmind.stok.core.domain.data.ProductDto;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

public interface IProductFactoryController {

    void removeProduct(UUID productPublicId);
    ResponseEntity<ProductDto> createProduct(ProductDto productDto);
    ResponseEntity<ProductDto> createDraftProduct();
    ResponseEntity<ProductDto> updateProduct(ProductDto productDto);
    ResponseEntity<ProductDto> addProductQuantity(UUID publicId, BigDecimal quantity);
    ResponseEntity<ProductDto> subtractProductQuantity(UUID publicId,BigDecimal quantity);
}
