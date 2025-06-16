package com.shine.shine.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Entity.ProductVariations;
import com.shine.shine.Service.ProductVariationsService;

@RestController
@RequestMapping("/api/product_variations")
public class ProductVariationsController {

    private final ProductVariationsService productVariationService;

    public ProductVariationsController(ProductVariationsService productVariationsService) {
        this.productVariationService = productVariationsService;
    }

    @GetMapping
    public ResponseEntity<List<ProductVariations>> getAllProductVariations() {
        List<ProductVariations> variations = productVariationService.getAllProductVariations();
        return new ResponseEntity<>(variations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariations> getProductVariationById(@PathVariable("id") Integer id) {
        Optional<ProductVariations> variation = productVariationService.getProductVariationById(id);
        return variation.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ProductVariations> createProductVariation(@RequestBody ProductVariations productVariation) {
        try {
            ProductVariations createdVariation = productVariationService.createProductVariation(productVariation);
            return new ResponseEntity<>(createdVariation, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Or HttpStatus.CONFLICT if due to duplicate combo
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductVariations> updateProductVariation(@PathVariable("id") Integer id, @RequestBody ProductVariations productVariationDetails) {
        try {
            ProductVariations updatedVariation = productVariationService.updateProductVariation(id, productVariationDetails);
            if (updatedVariation != null) {
                return new ResponseEntity<>(updatedVariation, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProductVariation(@PathVariable("id") Integer id) {
        boolean deleted = productVariationService.deleteProductVariation(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<List<ProductVariations>> getProductVariationsByProductId(@PathVariable("productId") Integer productId) {
        List<ProductVariations> variations = productVariationService.getProductVariationsByProductId(productId);
        if (!variations.isEmpty()) {
            return new ResponseEntity<>(variations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}