package com.example.demo.service;

import com.example.demo.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category saveCategory(Category category);
    Category getCategoryById(Long id);
    void deleteCategoryById(Long id);
    Category toggleCategoryStatus(Long id);
    boolean existsByName(String name);
    List<Category> getAllActiveCategories();
}
