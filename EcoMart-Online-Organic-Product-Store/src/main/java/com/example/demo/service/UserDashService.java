package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import java.util.List;

public interface UserDashService {

    User getUserByEmail(String email);

    List<Category> getAllCategories();

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(Long categoryId);
}
