package com.example.demo.service;

import com.example.demo.entity.Product;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product saveProduct(Product product);
    void deleteProductById(Long id);
    long countAllProducts();
    Product saveProductWithImage(Product product, MultipartFile imageFile) throws IOException;
    Product updateProduct(Product product, MultipartFile imageFile) throws IOException;
    void deleteImageFile(String imagePath) throws IOException;
    String saveImageFile(MultipartFile imageFile) throws IOException;
}
