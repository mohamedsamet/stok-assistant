package com.yesmind.stok.core.business.mapper;

import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.domain.entity.CreationStatus;
import com.yesmind.stok.core.domain.entity.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public class ProductMapper {

    private ProductMapper() {}

    public static Product toProduct(ProductDto productDto) {
        return Product.builder()
                .name(productDto.getName())
                .provider(productDto.getProvider())
                .quantity(productDto.getInitialValue())
                .initialValue(productDto.getInitialValue())
                .type(productDto.getType())
                .unit(productDto.getUnit())
                .price(productDto.getPrice())
                .deleted(false)
                .creationStatus(productDto.getCreationStatus())
                .build();
    }

    public static ProductDto toProductDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .publicId(product.getPublicId())
                .provider(product.getProvider())
                .quantity(product.getQuantity())
                .initialValue(product.getInitialValue())
                .type(product.getType())
                .price(product.getPrice())
                .unit(product.getUnit())
                .reference(product.getReference())
                .creationDate(product.getCreationDate())
                .build();
    }

    public static Product toProductByReference(String reference) {
        return Product.builder()
                .name("")
                .creationDate(LocalDateTime.now())
                .reference(reference)
                .deleted(false)
                .creationStatus(CreationStatus.DRAFT)
                .build();
    }
}
