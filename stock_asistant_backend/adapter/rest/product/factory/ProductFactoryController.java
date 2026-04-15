package com.yesmind.stok.adapter.rest.product.factory;

import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.port.in.product.IProductFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductFactoryController implements IProductFactoryController {

    private final IProductFactory productFactory;

    @Override
    @DeleteMapping("/product/{publicId}")
    public void removeProduct(@PathVariable("publicId") UUID productPublicId) {
        productFactory.removeProduct(productPublicId);
    }

    @Override
    @PostMapping("/product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productFactory.buildProduct(productDto));
    }

    @Override
    @PostMapping("/product/draft")
    public ResponseEntity<ProductDto> createDraftProduct() {
        return ResponseEntity.ok(productFactory.buildDraftProduct());
    }

    @Override
    @PutMapping("/product")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productFactory.updateProduct(productDto));
    }

    @Override
    @PostMapping("/product/add/{publicId}")
    public ResponseEntity<ProductDto> addProductQuantity(@PathVariable("publicId") UUID publicId, @RequestBody BigDecimal quantity) {
        return ResponseEntity.ok(productFactory.addProductQuantity(publicId, quantity));
    }

    @Override
    @PostMapping("/product/subtract/{publicId}")
    public ResponseEntity<ProductDto> subtractProductQuantity(@PathVariable("publicId") UUID publicId, @RequestBody BigDecimal quantity) {
        return ResponseEntity.ok(productFactory.subtractProductQuantity(publicId, quantity));
    }
}
