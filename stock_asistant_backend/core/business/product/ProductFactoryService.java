package com.yesmind.stok.core.business.product;

import com.yesmind.stok.application.exception.ConflictException;
import com.yesmind.stok.application.exception.ResourceNotFoundException;
import com.yesmind.stok.core.business.ReferenceGenerationUtils;
import com.yesmind.stok.core.business.mapper.ProductMapper;
import com.yesmind.stok.core.domain.data.ProductDto;
import com.yesmind.stok.core.domain.data.ReferenceTools;
import com.yesmind.stok.core.domain.entity.CreationStatus;
import com.yesmind.stok.core.domain.entity.Product;
import com.yesmind.stok.core.port.in.product.IProductFactory;
import com.yesmind.stok.core.port.out.IProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductFactoryService implements IProductFactory {

    private final IProductRepository productRepository;

    @Override
    @Transactional
    public ProductDto buildProduct(ProductDto productDto) {
        Optional<Product> product = productRepository.getProductByName(productDto.getName());

        if (product.isPresent()) {
            throw new ConflictException(String.format("Product with name %s already exists", productDto.getName()));
        }

        Product productToBuild = productRepository.getProductByPublicId(productDto.getPublicId())
                .orElseThrow(() -> new ResourceNotFoundException("Product Not found by public id"));

        productToBuild.setCreationStatus(CreationStatus.COMPLETED);
        productToBuild.setName(productDto.getName());
        productToBuild.setType(productDto.getType());
        productToBuild.setProvider(productDto.getProvider());
        productToBuild.setUnit(productDto.getUnit());
        productToBuild.setQuantity(productDto.getInitialValue());
        productToBuild.setInitialValue(productDto.getInitialValue());
        productToBuild.setPrice(productDto.getPrice());
        Product savedProduct = productRepository.saveProduct(productToBuild);
        return ProductMapper.toProductDto(savedProduct);

    }

    @Transactional
    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepository.getProductByPublicId(productDto.getPublicId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product with public id %s does not exist", productDto.getPublicId())));

        if (!product.getName().equals(productDto.getName())) {
            productRepository.getProductByName(productDto.getName())
                    .ifPresent(productWithSameName -> {throw new ConflictException("Product with same name already exist");});
        }
        product.setName(productDto.getName());
        product.setType(productDto.getType());
        product.setProvider(productDto.getProvider());
        product.setUnit(productDto.getUnit());
        product.setPrice(productDto.getPrice());
        Product savedProduct = productRepository.saveProduct(product);
        return ProductMapper.toProductDto(savedProduct);

    }

    @Override
    @Transactional
    public void removeProduct(UUID productPublicId) {
        Optional<Product> product = productRepository.getProductByPublicId(productPublicId);

        product.ifPresentOrElse(
                this::removeProduct,
                () -> {
                    throw new ResourceNotFoundException(
                            String.format("Product with publicId %s does not exists", productPublicId));
                });

    }

    @Transactional
    @Override
    public ProductDto addProductQuantity(UUID publicId, BigDecimal quantity) {
        Product product = productRepository.getProductByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Product with publicId %s does not exists", publicId)));

        product.setQuantity(product.getQuantity().add(quantity));

        return ProductMapper.toProductDto(productRepository.saveProduct(product));
    }

    @Transactional
    @Override
    public ProductDto subtractProductQuantity(UUID publicId, BigDecimal quantity) {
        Product product = productRepository.getProductByPublicId(publicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Product with publicId %s does not exists", publicId)));


        product.setQuantity(product.getQuantity().subtract(quantity));

        return ProductMapper.toProductDto(productRepository.saveProduct(product));
    }

    @Transactional
    @Override
    public ProductDto buildDraftProduct() {
        Optional<Product> lastProductEntry = productRepository.findLastProduct();

        String reference = lastProductEntry
                .map(product -> ReferenceGenerationUtils.generateReference(product.getReference(), ReferenceTools.PRODUCT))
                .orElseGet(() -> ReferenceGenerationUtils.generateFirstReference(ReferenceTools.PRODUCT));

        Product savedProduct = productRepository.saveProduct(ProductMapper.toProductByReference(reference));
        return ProductMapper.toProductDto(savedProduct);
    }

    private void removeProduct(Product product) {
        product.setDeleted(true);
        productRepository.saveProduct(product);
    }
}
