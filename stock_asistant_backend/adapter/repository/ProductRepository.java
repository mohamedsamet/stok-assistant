package com.yesmind.stok.adapter.repository;

import com.yesmind.stok.adapter.repository.jpa.ProductJpaRepository;
import com.yesmind.stok.core.domain.data.SearchProductDto;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.domain.entity.ProductType;
import com.yesmind.stok.core.port.out.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class ProductRepository implements IProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product saveProduct(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Optional<Product> getProductByName(String name) {
        return productJpaRepository.findByNameAndDeletedIsFalseOrDeletedIsNull(name);
    }

    @Override
    public Optional<Product> getProductByPublicId(UUID publicId) {
        return productJpaRepository.findByPublicId(publicId);
    }

    @Override
    public List<Product> searchProducts(SearchProductDto searchProductDto) {
        long limit = searchProductDto.getPageSize() != 0 ? searchProductDto.getPageSize() : 10;
        BigInteger offset = BigInteger.valueOf(searchProductDto.getPage() * limit);
        List<String> types = searchProductDto.getTypes().stream()
                .map(ProductType::name)
                .toList();
        return productJpaRepository.search(searchProductDto.getName(), types, offset, limit);
    }

    @Override
    public Long count(SearchProductDto searchProductDto) {
        return productJpaRepository.countAll(searchProductDto.getName(), searchProductDto.getTypes());
    }

    @Override
    public Optional<Product> findLastProduct() {
        return productJpaRepository.findLastProductsEntry().stream().max(
                Comparator.comparingLong(Product::getRefIncrement)
        );
    }

    @Override
    public List<Product> findProductsByUuids(Set<UUID> publicIds) {
        return productJpaRepository.findByPublicIdInAndDeletedIsFalseOrDeletedIsNull(publicIds);
    }
}
