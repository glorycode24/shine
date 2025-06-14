package com.shine.shine.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shine.shine.Entity.Categories;
import com.shine.shine.Repository.CategoriesRepository;

@Service

public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    public Optional<Categories> getCategoryById(Integer id) {
        return categoriesRepository.findById(id);
    }

    @Transactional
    public Categories createCategory(Categories category) {
        return categoriesRepository.save(category);
    }
    @Transactional

public Categories addCategory(Categories category) {
    Optional<Categories> existing = categoriesRepository.findByCategoryName(category.getCategoryName());
    if (existing.isPresent()) {
        throw new IllegalArgumentException("Category name already exists.");
    }
    return categoriesRepository.save(category);
}
    @Transactional
    public Categories updateCategory(Integer id, Categories categoryDetails) {
        Optional<Categories> optionalCategory = categoriesRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Categories existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(categoryDetails.getCategoryName());
            return categoriesRepository.save(existingCategory);
        } else {
 
            return null;
        }
    }

    @Transactional
    public boolean deleteCategory(Integer id) {
        if (categoriesRepository.existsById(id)) {
            categoriesRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Categories> getCategoryByName(String categoryName) {
        return categoriesRepository.findByCategoryName(categoryName);
    }

    public boolean categoryExistsByName(String categoryName) {
        return categoriesRepository.existsByCategoryName(categoryName);
    }
}
