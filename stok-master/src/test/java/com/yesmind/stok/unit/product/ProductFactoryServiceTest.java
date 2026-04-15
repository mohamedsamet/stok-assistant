package com.yesmind.stok.unit.product;

import com.yesmind.stok.application.exception.ConflictException;
import com.yesmind.stok.application.exception.ResourceNotFoundException;
import com.yesmind.stok.core.business.product.ProductFactoryService;
import com.yesmind.stok.core.business.mapper.ProductMapper;
import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.domain.entity.ProductType;
import com.yesmind.stok.core.domain.entity.Unit;
import com.yesmind.stok.core.port.out.IProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductFactoryServiceTest {

    @InjectMocks
    private ProductFactoryService productService;

    @Mock
    private IProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Test
    void shouldBuildNewProduct() {
        ProductDto productDto = ProductDto.builder()
                .name("product")
                .provider("provider")
                .initialValue(BigDecimal.valueOf(2.2d))
                .type(ProductType.RAW)
                .unit(Unit.KG)
                .build();

        Mockito.when(productRepository.getProductByName("product")).thenReturn(Optional.empty());

        Mockito.when(productRepository.getProductByPublicId(any())).thenReturn(Optional.of(Product.builder().build()));
        Mockito.when(productRepository.saveProduct(any())).thenReturn(ProductMapper.toProduct(productDto));
        ProductDto createdProduct = productService.buildProduct(productDto);

        Mockito.verify(productRepository, Mockito.times(1)).getProductByName("product");
        Mockito.verify(productRepository, Mockito.times(1)).saveProduct(productCaptor.capture());

        Assertions.assertEquals("product", productCaptor.getValue().getName());
        Assertions.assertEquals("provider", productCaptor.getValue().getProvider());
        Assertions.assertEquals(BigDecimal.valueOf(2.2d), productCaptor.getValue().getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(2.2d), productCaptor.getValue().getInitialValue());
        Assertions.assertEquals(ProductType.RAW, productCaptor.getValue().getType());
        Assertions.assertEquals(Unit.KG, productCaptor.getValue().getUnit());

        Assertions.assertEquals("product", createdProduct.getName());
        Assertions.assertEquals("provider", createdProduct.getProvider());
        Assertions.assertEquals(BigDecimal.valueOf(2.2d), createdProduct.getQuantity());
        Assertions.assertEquals(ProductType.RAW, createdProduct.getType());
        Assertions.assertEquals(Unit.KG, createdProduct.getUnit());
    }

    @Test
    void shouldNotAddNewProductWhenNameExist() {
        ProductDto productDto = ProductDto.builder()
                .name("product")
                .provider("provider")
                .quantity(BigDecimal.valueOf(2.2d))
                .type(ProductType.RAW)
                .unit(Unit.KG)
                .build();

        Product existing = Product.builder()
                .name("product")
                .type(ProductType.RAW)
                .build();

        Mockito.when(productRepository.getProductByName("product")).thenReturn(Optional.of(existing));
        Assertions.assertThrows(ConflictException.class, () -> productService.buildProduct(productDto));

    }

    @Test
    void shouldThrowResourceNotFoundWhenRemoveProductNotFound() {
        UUID publicId = UUID.randomUUID();

        Mockito.when(productRepository.getProductByPublicId(publicId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.removeProduct(publicId));

    }

    @Test
    void shouldRemoveProduct() {
        UUID publicId = UUID.randomUUID();

        Product existing = Product.builder()
                .name("product")
                .type(ProductType.RAW)
                .build();

        Mockito.when(productRepository.getProductByPublicId(publicId)).thenReturn(Optional.of(existing));
        Mockito.when(productRepository.saveProduct(productCaptor.capture())).thenReturn(null);

        productService.removeProduct(publicId);

        Assertions.assertTrue(productCaptor.getValue().getDeleted());
    }


    @Test
    void shouldAddProductQuantity() {
        UUID publicId = UUID.randomUUID();

        Product existing = Product.builder()
                .name("product")
                .type(ProductType.RAW)
                .quantity(BigDecimal.valueOf(99.5d))
                .build();

        Mockito.when(productRepository.getProductByPublicId(publicId)).thenReturn(Optional.of(existing));
        Mockito.when(productRepository.saveProduct(productCaptor.capture())).thenReturn(existing);

        productService.addProductQuantity(publicId, BigDecimal.valueOf(6.5F));

        Assertions.assertEquals(BigDecimal.valueOf(106d), productCaptor.getValue().getQuantity());
    }

    @Test
    void shouldSubtractProductQuantity() {
        UUID publicId = UUID.randomUUID();

        Product existing = Product.builder()
                .name("product")
                .type(ProductType.RAW)
                .quantity(BigDecimal.valueOf(99.5d))
                .build();

        Mockito.when(productRepository.getProductByPublicId(publicId)).thenReturn(Optional.of(existing));
        Mockito.when(productRepository.saveProduct(productCaptor.capture())).thenReturn(existing);

        productService.subtractProductQuantity(publicId, BigDecimal.valueOf(9.6d));

        Assertions.assertEquals(BigDecimal.valueOf(89.9d), productCaptor.getValue().getQuantity());
    }
}
