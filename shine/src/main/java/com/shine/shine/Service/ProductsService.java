package com.shine.shine.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shine.shine.Entity.Products;
import com.shine.shine.Repository.ProductsRepository;

@Service
public class ProductsService {

    private final ProductsRepository productsRepository;

    public List<Products> getAllProducts() {
        return productsRepository.findAll();
    }

    public ProductsService(ProductsRepository productRepository) {
        this.productsRepository = productRepository;
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
}
