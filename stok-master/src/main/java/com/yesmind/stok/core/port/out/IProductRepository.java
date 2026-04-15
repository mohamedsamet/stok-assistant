package com.yesmind.stok.core.port.out;

import com.yesmind.stok.core.domain.data.SearchProductDto;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.domain.entity.ProductType;

import java.util.*;

public interface IProductRepository {

    Product saveProduct(Product product);
    Optional<Product> getProductByName(String name);
    Optional<Product> getProductByPublicId(UUID publicId);
    List<Product> searchProducts(SearchProductDto searchProductDto);
    List<Product> findProductsByUuids(Set<UUID> publicIds);
    Long count(SearchProductDto searchProductDto);
    Optional<Product> findLastProduct();

}
