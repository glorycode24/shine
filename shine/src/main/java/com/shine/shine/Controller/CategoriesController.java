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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shine.shine.Entity.Categories;
import com.shine.shine.Service.CategoriesService;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final CategoriesService categoryService;

    public CategoriesController(CategoriesService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Categories>> getAllCategories() {
        List<Categories> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categories> getCategoryById(@PathVariable("id") Integer id) {
        Optional<Categories> category = categoryService.getCategoryById(id);
        return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Categories category) {
        try {
            Categories savedCategory = categoryService.addCategory(category);
            return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        // The following code is unreachable due to the try-catch above.
        // If you want to check for existing category before adding, move this check before the try-catch.
        /*
        if (categoryService.categoryExistsByName(category.getCategoryName())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); 
        }
        Categories createdCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        */
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categories> updateCategory(@PathVariable("id") Integer id, @RequestBody Categories categoryDetails) {
        Categories updatedCategory = categoryService.updateCategory(id, categoryDetails);
        if (updatedCategory != null) {
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable("id") Integer id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-name")
    public ResponseEntity<Categories> getCategoryByName(@RequestParam("name") String categoryName) {
        Optional<Categories> category = categoryService.getCategoryByName(categoryName);
        return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}