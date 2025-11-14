package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.service.DummyDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;   // ✅ Added
    private final UserService userService;     // ✅ Added
    private final DummyDataService dummyDataService;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    // ✅ Updated constructor
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             OrderService orderService,
                             UserService userService,
                             DummyDataService dummyDataService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.userService = userService;
        this.dummyDataService = dummyDataService;
    }

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalProducts", productService.countAllProducts());
        model.addAttribute("totalOrders", orderService.countAllOrders());   // ✅ Now dynamic
        model.addAttribute("totalUsers", userService.countAllUsers());     // ✅ Now dynamic
        return "admin/admin-dashboard";
    }

    // List all products
    @GetMapping("/products")
    public String manageProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/admin-products";
    }

    // Show add product form
    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllActiveCategories());
        return "admin/add-product";
    }

    // Save product
    @PostMapping("/products/add")
    public String saveProduct(
            @ModelAttribute("product") Product product,
            @RequestParam("category") Long categoryId,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model) {

        try {
            Category category = categoryService.getCategoryById(categoryId);
            product.setCategory(category);

            if (!imageFile.isEmpty()) {
                String imagePath = productService.saveImageFile(imageFile);
                product.setImagePath(imagePath);
            }

            productService.saveProduct(product);
            return "redirect:/admin/products?success";

        } catch (RuntimeException e) {
            model.addAttribute("error", "Invalid category selected: " + e.getMessage());
            model.addAttribute("categories", categoryService.getAllActiveCategories());
            return "admin/add-product";
        } catch (IOException e) {
            model.addAttribute("error", "File upload failed: " + e.getMessage());
            model.addAttribute("categories", categoryService.getAllActiveCategories());
            return "admin/add-product";
        }
    }

    // Show edit product form
    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.getAllActiveCategories());
            return "admin/edit-product";
        } catch (RuntimeException e) {
            return "redirect:/admin/products?error=notfound";
        }
    }

    // Update product
    @PostMapping("/products/update/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute("product") Product product,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {

        try {
            product.setId(id);
            productService.updateProduct(product, imageFile);
            redirectAttributes.addFlashAttribute("success", "Product updated successfully");
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/admin/products/edit/" + id;
        }
    }

    // Delete product
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/admin/products?deleted";
    }

    @GetMapping("/manage-orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());  // ✅ Show orders
        return "admin/admin-orders";
    }

    @GetMapping("/manage-users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());  // ✅ Show users
        return "admin/admin-users";
    }

    // Add dummy products endpoint
    @GetMapping("/products/add-dummy")
    public String addDummyProducts(RedirectAttributes redirectAttributes) {
        try {
            dummyDataService.initializeDummyData();
            redirectAttributes.addFlashAttribute("success", 
                "Dummy products and categories added successfully!");
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Error adding dummy products: " + e.getMessage());
            return "redirect:/admin/products";
        }
    }

}
