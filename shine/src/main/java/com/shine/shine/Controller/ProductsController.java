package com.shine.shine.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.shine.shine.Entity.Products;
import com.shine.shine.Service.ProductsService;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    @Autowired
    private final ProductsService productService;
    // Add this line near your other @Autowired fields
    public ProductsController(ProductsService productService) {
        this.productService = productService;
    }
    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts() {
        List<Products> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    // In ProductsController.java

    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable("id") Integer id) {
        Optional<Products> product = productService.getProductById(id);
        return product.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Products product) {
        try {
            Products saved = productService.createProduct(product);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Products> updateProduct(@PathVariable("id") Integer id, @RequestBody Products productDetails) {
        Products updatedProduct = productService.updateProduct(id, productDetails);
        if (updatedProduct != null) {
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") Integer id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}