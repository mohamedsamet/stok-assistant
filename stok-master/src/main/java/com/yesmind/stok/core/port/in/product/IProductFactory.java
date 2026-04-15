package com.yesmind.stok.core.port.in.product;

import com.yesmind.stok.core.domain.data.ProductDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface IProductFactory {

    ProductDto buildProduct(ProductDto productDto);
    ProductDto updateProduct(ProductDto productDto);
    void removeProduct(UUID productPublicId);
    ProductDto addProductQuantity(UUID publicId, BigDecimal quantity);
    ProductDto subtractProductQuantity(UUID publicId, BigDecimal quantity);
    ProductDto buildDraftProduct();
}
