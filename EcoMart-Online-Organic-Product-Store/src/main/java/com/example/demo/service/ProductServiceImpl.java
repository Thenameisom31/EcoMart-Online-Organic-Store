package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    // Constants
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    
    // Repositories
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, 
                             CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // ========== READ OPERATIONS ==========
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllProducts() {
        return productRepository.count();
    }

    // ========== CREATE/UPDATE OPERATIONS ==========
    
    @Override
    @Transactional
    public Product saveProduct(Product product) {
        resolveCategoryAssociation(product);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Product product, MultipartFile imageFile) throws IOException {
        resolveCategoryAssociation(product);
        handleImageUpdate(product, imageFile);
        return productRepository.save(product);
    }

    // ========== DELETE OPERATIONS ==========
    
    @Override
    @Transactional
    public void deleteProductById(Long id) {
        Product product = getProductById(id);
        deleteProductImage(product);
        productRepository.deleteById(id);
    }

    // ========== IMAGE HANDLING METHODS ==========
    
    @Override
    public String saveImageFile(MultipartFile imageFile) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), filePath);
        
        return "/uploads/" + fileName;
    }

    @Override
    public void deleteImageFile(String imagePath) throws IOException {
        if (imagePath != null && !imagePath.isEmpty()) {
            String fileName = imagePath.substring(imagePath.lastIndexOf('/') + 1);
            Path filePath = Paths.get(UPLOAD_DIRECTORY, fileName);
            Files.deleteIfExists(filePath);
        }
    }

    // ========== PRIVATE HELPER METHODS ==========
    
    private void resolveCategoryAssociation(Product product) {
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category managedCategory = categoryRepository.findById(product.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(managedCategory);
        }
    }
    
    private void handleImageUpdate(Product product, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete old image if exists
            if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
                deleteImageFile(product.getImagePath());
            }
            
            // Save new image
            String imagePath = saveImageFile(imageFile);
            product.setImagePath(imagePath);
        }
    }
    
    private void deleteProductImage(Product product) {
        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            try {
                deleteImageFile(product.getImagePath());
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete product image: " + e.getMessage());
            }
        }
    }

    // Remove unused method
    @Override
    public Product saveProductWithImage(Product product, MultipartFile imageFile) throws IOException {
        throw new UnsupportedOperationException("Use saveProduct or updateProduct instead");
    }
}
