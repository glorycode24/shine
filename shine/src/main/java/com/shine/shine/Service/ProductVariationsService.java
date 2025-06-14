package com.shine.shine.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.Colors;
import com.shine.shine.Entity.ProductVariations;
import com.shine.shine.Entity.Products;
import com.shine.shine.Entity.Sizes;
import com.shine.shine.Repository.ColorsRepository;
import com.shine.shine.Repository.ProductVariationsRepository;
import com.shine.shine.Repository.ProductsRepository;
import com.shine.shine.Repository.SizesRepository;

@Service

public class ProductVariationsService {

    private final ProductVariationsRepository productVariationsRepository;
    private final ProductsRepository productsRepository;
    private final SizesRepository sizesRepository;
    private final ColorsRepository colorsRepository;

    public ProductVariationsService(ProductVariationsRepository productVariationsRepository,
                                   ProductsRepository productsRepository,
                                   SizesRepository sizesRepository,
                                   ColorsRepository colorsRepository) { 
        this.productVariationsRepository = productVariationsRepository;
        this.productsRepository = productsRepository;
        this.sizesRepository = sizesRepository;
        this.colorsRepository = colorsRepository;
    }

    public List<ProductVariations> getAllProductVariations() {
        return productVariationsRepository.findAll();
    }

    public Optional<ProductVariations> getProductVariationById(Integer id) {
        return productVariationsRepository.findById(id);
    }

    @Transactional
    public ProductVariations createProductVariation(ProductVariations productVariation) {
        Products product = productsRepository.findById(productVariation.getProduct().getProductId())
                                           .orElseThrow(() -> new IllegalArgumentException("Product not found for ID: " + productVariation.getProduct().getProductId()));
        Sizes size = sizesRepository.findById(productVariation.getSize().getSizeId())
                                   .orElseThrow(() -> new IllegalArgumentException("Size not found for ID: " + productVariation.getSize().getSizeId()));
     
        Colors color = colorsRepository.findById(productVariation.getColor().getColorId())
                                     .orElseThrow(() -> new IllegalArgumentException("Color not found for ID: " + productVariation.getColor().getColorId()));

        productVariation.setProduct(product);
        productVariation.setSize(size);
        productVariation.setColor(color);

        if (productVariationsRepository.findByProduct_ProductIdAndSize_SizeIdAndColor_ColorId(
            product.getProductId(), size.getSizeId(), color.getColorId()).isPresent()) {
            throw new IllegalArgumentException("Product variation for this product, size, and color already exists.");
        }

        return productVariationsRepository.save(productVariation);
    }

    @Transactional
    public ProductVariations updateProductVariation(Integer id, ProductVariations productVariationDetails) {
        Optional<ProductVariations> optionalProductVariation = productVariationsRepository.findById(id);
        if (optionalProductVariation.isPresent()) {
            ProductVariations existingVariation = optionalProductVariation.get();

            if (productVariationDetails.getProduct() != null && productVariationDetails.getProduct().getProductId() != null) {
                Products product = productsRepository.findById(productVariationDetails.getProduct().getProductId())
                                                   .orElseThrow(() -> new IllegalArgumentException("Product not found for ID: " + productVariationDetails.getProduct().getProductId()));
                existingVariation.setProduct(product);
            }
            if (productVariationDetails.getSize() != null && productVariationDetails.getSize().getSizeId() != null) {
                Sizes size = sizesRepository.findById(productVariationDetails.getSize().getSizeId())
                                           .orElseThrow(() -> new IllegalArgumentException("Size not found for ID: " + productVariationDetails.getSize().getSizeId()));
                existingVariation.setSize(size);
            }
            if (productVariationDetails.getColor() != null && productVariationDetails.getColor().getColorId() != null) {
              
                Colors color = colorsRepository.findById(productVariationDetails.getColor().getColorId())
                                             .orElseThrow(() -> new IllegalArgumentException("Color not found for ID: " + productVariationDetails.getColor().getColorId()));
                existingVariation.setColor(color);
            }

            existingVariation.setAdditionalStock(productVariationDetails.getAdditionalStock());

            Optional<ProductVariations> duplicateCheck = productVariationsRepository.findByProduct_ProductIdAndSize_SizeIdAndColor_ColorId(
                existingVariation.getProduct().getProductId(),
                existingVariation.getSize().getSizeId(),
                existingVariation.getColor().getColorId() 
            );
            if (duplicateCheck.isPresent() && !duplicateCheck.get().getVariationId().equals(existingVariation.getVariationId())) {
                throw new IllegalArgumentException("Another product variation with this product, size, and color already exists.");
            }

            return productVariationsRepository.save(existingVariation);
        } else {
            return null;
        }
    }

    @Transactional
    public boolean deleteProductVariation(Integer id) {
        if (productVariationsRepository.existsById(id)) {
            productVariationsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ProductVariations> getProductVariationsByProductId(Integer productId) {
        return productVariationsRepository.findByProduct_ProductId(productId);
    }
}