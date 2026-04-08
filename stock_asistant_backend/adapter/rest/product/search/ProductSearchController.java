package com.yesmind.stok.adapter.rest.product.search;

import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.domain.data.ProductResponse;
import com.yesmind.stok.core.domain.data.SearchProductDto;
import com.yesmind.stok.core.port.in.product.IProductSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductSearchController implements IProductSearchController {

    private final IProductSearch productSearch;

    @Override
    @PostMapping(value = "/product/search")
    public ResponseEntity<ProductResponse> searchProducts(@RequestBody SearchProductDto searchProductDto) {
        return ResponseEntity.ok(productSearch.search(searchProductDto));
    }

    @Override
    @GetMapping("/product/search/{uuid}")
    public ResponseEntity<ProductDto> searchProductByUuid(@PathVariable("uuid") UUID productUuid) {
        return ResponseEntity.ok(productSearch.findByUuid(productUuid));
    }
}
