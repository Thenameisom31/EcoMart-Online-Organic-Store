package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String manageCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/categories";
    }

    @PostMapping("/add")
    public String addCategory(
        @RequestParam String name,
        @RequestParam boolean status,
        RedirectAttributes redirectAttributes) {
        
        if (categoryService.existsByName(name)) {
            redirectAttributes.addFlashAttribute("error", "Category name already exists");
            return "redirect:/admin/categories";
        }
        
        Category newCategory = new Category(name, status);
        categoryService.saveCategory(newCategory);
        redirectAttributes.addFlashAttribute("success", "Category added successfully");
        return "redirect:/admin/categories";
    }

    @GetMapping("/toggle-status/{id}")
    public String toggleCategoryStatus(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {
        
        try {
            categoryService.toggleCategoryStatus(id);
            redirectAttributes.addFlashAttribute("success", "Category status updated");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(
        @PathVariable Long id,
        RedirectAttributes redirectAttributes) {
        
        try {
            categoryService.deleteCategoryById(id);
            redirectAttributes.addFlashAttribute("success", "Category deleted");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categories";
    }
    
}
