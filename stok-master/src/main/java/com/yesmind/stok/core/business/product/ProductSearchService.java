package com.yesmind.stok.core.business.product;

import com.yesmind.stok.application.exception.ResourceNotFoundException;
import com.yesmind.stok.core.business.mapper.ProductMapper;
import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.domain.data.ProductResponse;
import com.yesmind.stok.core.domain.data.SearchProductDto;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.port.in.product.IProductSearch;
import com.yesmind.stok.core.port.out.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService implements IProductSearch {

    private final IProductRepository productRepository;

    @Override
    public ProductDto findByUuid(UUID productUuid) {
        Optional<Product> product = productRepository.getProductByPublicId(productUuid);

        if (product.isPresent()) {
            return ProductMapper.toProductDto(product.get());
        }

        throw new ResourceNotFoundException(String.format("Product with UUID %s Not Found", productUuid));
    }

    @Override
    public Map<UUID, Product> findByUuids(Set<UUID> productUuid) {
        return productRepository.findProductsByUuids(productUuid).stream()
                .filter(product -> !product.getDeleted())
                .collect(Collectors.toMap(Product::getPublicId, product -> product));
    }

    @Override
    public ProductResponse search(SearchProductDto searchProductDto) {
        if (searchProductDto.getName() != null) {
            searchProductDto.setName("%" + searchProductDto.getName() + "%");
        }
        List<ProductDto> products = productRepository.searchProducts(searchProductDto)
                .stream()
                .map(ProductMapper::toProductDto)
                .toList();

        return ProductResponse.builder()
                .products(products)
                .count(productRepository.count(searchProductDto))
                .build();

    }

}
