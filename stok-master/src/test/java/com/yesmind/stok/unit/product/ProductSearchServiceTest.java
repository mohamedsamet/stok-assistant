package com.yesmind.stok.unit.product;

import com.yesmind.stok.application.exception.ResourceNotFoundException;
import com.yesmind.stok.core.business.product.ProductSearchService;
import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.domain.data.ProductResponse;
import com.yesmind.stok.core.domain.data.SearchProductDto;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.domain.entity.ProductType;
import com.yesmind.stok.core.domain.entity.Unit;
import com.yesmind.stok.core.port.out.IProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductSearchServiceTest {

    @InjectMocks
    private ProductSearchService productService;

    @Mock
    private IProductRepository productRepository;

    @Test
    void shouldFindByType() {

        Product product1 = buildProduct(1L, 5.2F, 5.2F, "pro 1", Unit.KG, "prod1");

        Product product2 = buildProduct(2L, 5.1F, 5.3F, "pro 2", Unit.M, "prod2");
        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        Mockito.when(productRepository.searchProducts(any())).thenReturn(products);
        Mockito.when(productRepository.count(any())).thenReturn(2L);
        ProductResponse productResponse = productService.search(SearchProductDto.builder()
                .types(Collections.singletonList(ProductType.RAW))
                .build());

        Assertions.assertNotNull(productResponse);
        Assertions.assertEquals(2, productResponse.getCount());

        ProductDto p1 = productResponse.getProducts().stream()
                .filter(product -> product.getName().equals("pro 1"))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(p1);
        Assertions.assertEquals("prod1", p1.getProvider());
        Assertions.assertEquals(BigDecimal.valueOf(5.2F), p1.getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(5.2F), p1.getInitialValue());
        Assertions.assertEquals(Unit.KG, p1.getUnit());

        ProductDto p2 = productResponse.getProducts().stream()
                .filter(product -> product.getName().equals("pro 2"))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(p2);
        Assertions.assertEquals("prod2", p2.getProvider());
        Assertions.assertEquals(BigDecimal.valueOf(5.3F), p2.getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(5.1F), p2.getInitialValue());
        Assertions.assertEquals(Unit.M, p2.getUnit());

    }

    @Test
    void shouldFindByUUID() {

        UUID publicId = UUID.randomUUID();
        Product product1 = buildProduct(3L, 5.2F, 5.2F, "pro 1", Unit.KG, "prod1");

        Mockito.when(productRepository.getProductByPublicId(publicId)).thenReturn(Optional.of(product1));
        ProductDto productDto = productService.findByUuid(publicId);

        Assertions.assertNotNull(productDto);

        Assertions.assertEquals("pro 1", productDto.getName());
        Assertions.assertEquals("prod1", productDto.getProvider());
        Assertions.assertEquals(BigDecimal.valueOf(5.2F), productDto.getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(5.2F), productDto.getInitialValue());
        Assertions.assertEquals(Unit.KG, productDto.getUnit());

    }

    @Test
    void shouldReturnResourceNotFoundByUUID() {

        UUID publicId = UUID.randomUUID();
        Mockito.when(productRepository.getProductByPublicId(publicId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.findByUuid(publicId));
    }

    @Test
    void shouldFindByUUIDs() {

        UUID publicId1 = UUID.randomUUID();
        Product product1 = buildProduct(4L, 5.2F, 5.2F, "pro 1", Unit.KG, "prod1");
        product1.setPublicId(publicId1);

        UUID publicId2 = UUID.randomUUID();
        Product product2 = buildProduct(6L, 5.1F, 5.3F, "pro 2", Unit.M, "prod2");
        product2.setPublicId(publicId2);

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);

        Set<UUID> uuids = new HashSet<>();
        uuids.add(publicId1);
        uuids.add(publicId2);

        Mockito.when(productRepository.findProductsByUuids(uuids)).thenReturn(products);
        Map<UUID, Product> productDtoMap = productService.findByUuids(uuids);

        Assertions.assertNotNull(productDtoMap.get(publicId1));
        Assertions.assertEquals("pro 1", productDtoMap.get(publicId1).getName());
        Assertions.assertEquals("prod1", productDtoMap.get(publicId1).getProvider());
        Assertions.assertEquals(BigDecimal.valueOf(5.2F), productDtoMap.get(publicId1).getInitialValue());
        Assertions.assertEquals(BigDecimal.valueOf(5.2F), productDtoMap.get(publicId1).getQuantity());
        Assertions.assertEquals(Unit.KG, productDtoMap.get(publicId1).getUnit());
        Assertions.assertEquals(ProductType.RAW, productDtoMap.get(publicId1).getType());

        Assertions.assertNotNull(productDtoMap.get(publicId2));
        Assertions.assertEquals("pro 2", productDtoMap.get(publicId2).getName());
        Assertions.assertEquals("prod2", productDtoMap.get(publicId2).getProvider());
        Assertions.assertEquals(BigDecimal.valueOf(5.1F), productDtoMap.get(publicId2).getInitialValue());
        Assertions.assertEquals(BigDecimal.valueOf(5.3F), productDtoMap.get(publicId2).getQuantity());
        Assertions.assertEquals(Unit.M, productDtoMap.get(publicId2).getUnit());
        Assertions.assertEquals(ProductType.RAW, productDtoMap.get(publicId2).getType());

    }

    private Product buildProduct(Long id, float initalValue, float quantity, String name, Unit unit, String provider) {
        return Product.builder()
                .id(id)
                .deleted(false)
                .initialValue(BigDecimal.valueOf(initalValue))
                .quantity(BigDecimal.valueOf(quantity))
                .type(ProductType.RAW)
                .name(name)
                .unit(unit)
                .provider(provider)
                .build();
    }


}
