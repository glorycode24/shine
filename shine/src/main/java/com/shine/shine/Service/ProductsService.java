package com.shine.shine.Service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.Products;
import com.shine.shine.Entity.Categories;
import com.shine.shine.Entity.Sizes;
import com.shine.shine.Entity.Colors;
import com.shine.shine.Entity.ProductVariations;
import com.shine.shine.Repository.ProductsRepository;
import com.shine.shine.Repository.CategoriesRepository;
import com.shine.shine.Repository.SizesRepository;
import com.shine.shine.Repository.ColorsRepository;
import com.shine.shine.Repository.ProductVariationsRepository;
import com.shine.shine.dto.ProductWithVariationsRequest;

@Service
public class ProductsService {

    private final ProductsRepository productsRepository;
    private final CategoriesRepository categoriesRepository;
    private final SizesRepository sizesRepository;
    private final ColorsRepository colorsRepository;
    private final ProductVariationsRepository productVariationsRepository;

    public ProductsService(ProductsRepository productsRepository,
                          CategoriesRepository categoriesRepository,
                          SizesRepository sizesRepository,
                          ColorsRepository colorsRepository,
                          ProductVariationsRepository productVariationsRepository) {
        this.productsRepository = productsRepository;
        this.categoriesRepository = categoriesRepository;
        this.sizesRepository = sizesRepository;
        this.colorsRepository = colorsRepository;
        this.productVariationsRepository = productVariationsRepository;
    }

    public List<Products> getAllProducts() {
        return productsRepository.findAll();
    }

    public Page<Products> getAllProducts(Pageable pageable) {
    return productsRepository.findAll(pageable);
}

    

    public Optional<Products> getProductById(Integer id) {
        return productsRepository.findById(id);
    }

    public Products createProduct(Products product) {
        Optional<Products> existing = productsRepository.findByProductName(product.getProductName());
if (existing.isPresent()) {
    throw new IllegalArgumentException("Product already exists: " + product.getProductName());
}

        return productsRepository.save(product);
    }

    public Products updateProduct(Integer id, Products productDetails) {
        Optional<Products> optionalProduct = productsRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Products existingProduct = optionalProduct.get();
            existingProduct.setProductName(productDetails.getProductName());
            existingProduct.setDescription(productDetails.getDescription());
            existingProduct.setSku(productDetails.getSku());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setImageUrl(productDetails.getImageUrl());
            existingProduct.setStockQuantity(productDetails.getStockQuantity());
            existingProduct.setCategory(productDetails.getCategory()); 
            return productsRepository.save(existingProduct);
        } else {
            return null;
        }
    }

    public boolean deleteProduct(Integer id) {
        if (productsRepository.existsById(id)) {
            productsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void createProductWithVariations(ProductWithVariationsRequest request) {
        // 1. Find category
        Categories category = categoriesRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // 2. Create product
        Products product = new Products();
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getBasePrice());
        product.setCategory(category);
        product.setImageUrl(request.getImageUrl());
        productsRepository.save(product);

        // 3. For each variation
        for (ProductWithVariationsRequest.VariationDto v : request.getVariations()) {
            // Find or create size
            Sizes size = sizesRepository.findBySizeName(v.getSize())
                .orElseGet(() -> sizesRepository.save(new Sizes(v.getSize())));

            // Find or create color
            Colors color = colorsRepository.findByColorName(v.getColor())
                .orElseGet(() -> colorsRepository.save(new Colors(v.getColor())));

            // Create variation
            ProductVariations variation = new ProductVariations();
            variation.setProduct(product);
            variation.setSize(size);
            variation.setColor(color);
            variation.setAdditionalStock(v.getStockQuantity());
            // Set per-variation price, fallback to product base price if not provided
            BigDecimal variationPrice = v.getPrice() != null ? v.getPrice() : request.getBasePrice();
            variation.setPrice(variationPrice);
            productVariationsRepository.save(variation);
        }
    }
}
