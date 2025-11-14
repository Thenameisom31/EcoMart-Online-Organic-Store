package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class DummyDataService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void initializeDummyData() {
        // Initialize categories if they don't exist
        if (categoryRepository.count() == 0) {
            initializeCategories();
        }
        // Initialize products if they don't exist
        if (productRepository.count() == 0) {
            initializeProducts();
        }
    }
    
    @Transactional
    public void forceInitializeProducts() {
        // Force initialize products even if some exist (adds missing ones)
        initializeProducts();
    }

    @Transactional
    public void initializeCategories() {
        List<Category> categories = Arrays.asList(
            new Category("Fruits & Vegetables", true),
            new Category("Grains & Pulses", true),
            new Category("Dairy Products", true),
            new Category("Beverages", true),
            new Category("Personal Care", true),
            new Category("Household Items", true),
            new Category("Snacks", true),
            new Category("Honey & Jams", true)
        );
        categoryRepository.saveAll(categories);
    }

    @Transactional
    public void initializeProducts() {
        // Get or create categories
        Category fruits = categoryRepository.findByName("Fruits & Vegetables")
            .orElseGet(() -> categoryRepository.save(new Category("Fruits & Vegetables", true)));
        Category grains = categoryRepository.findByName("Grains & Pulses")
            .orElseGet(() -> categoryRepository.save(new Category("Grains & Pulses", true)));
        Category dairy = categoryRepository.findByName("Dairy Products")
            .orElseGet(() -> categoryRepository.save(new Category("Dairy Products", true)));
        Category beverages = categoryRepository.findByName("Beverages")
            .orElseGet(() -> categoryRepository.save(new Category("Beverages", true)));
        Category personalCare = categoryRepository.findByName("Personal Care")
            .orElseGet(() -> categoryRepository.save(new Category("Personal Care", true)));
        Category household = categoryRepository.findByName("Household Items")
            .orElseGet(() -> categoryRepository.save(new Category("Household Items", true)));
        Category snacks = categoryRepository.findByName("Snacks")
            .orElseGet(() -> categoryRepository.save(new Category("Snacks", true)));
        Category honey = categoryRepository.findByName("Honey & Jams")
            .orElseGet(() -> categoryRepository.save(new Category("Honey & Jams", true)));

        // Create dummy products
        List<Product> products = Arrays.asList(
            // Fruits & Vegetables
            createProduct("Organic Apples", 120.0, "kg", 
                "Fresh, crisp organic apples grown without pesticides. Rich in fiber and vitamin C.", 
                fruits, "Active"),
            createProduct("Organic Tomatoes", 80.0, "kg", 
                "Juicy, red organic tomatoes perfect for salads and cooking. Farm fresh and pesticide-free.", 
                fruits, "Active"),
            createProduct("Organic Spinach", 60.0, "bunch", 
                "Fresh organic spinach leaves, rich in iron and vitamins. Perfect for salads and smoothies.", 
                fruits, "Active"),
            createProduct("Organic Carrots", 70.0, "kg", 
                "Sweet and crunchy organic carrots. Great source of beta-carotene and fiber.", 
                fruits, "Active"),
            createProduct("Organic Bananas", 90.0, "dozen", 
                "Naturally ripened organic bananas. Rich in potassium and natural sugars.", 
                fruits, "Active"),

            // Grains & Pulses
            createProduct("Organic Basmati Rice", 160.0, "kg", 
                "Premium quality long-grain organic basmati rice. Aromatic and perfect for biryanis and pulao.", 
                grains, "Active"),
            createProduct("Organic Brown Rice", 140.0, "kg", 
                "Nutritious whole grain organic brown rice. High in fiber and essential nutrients.", 
                grains, "Active"),
            createProduct("Organic Toor Dal", 180.0, "kg", 
                "Premium organic toor dal (pigeon peas). Rich in protein and essential amino acids.", 
                grains, "Active"),
            createProduct("Organic Moong Dal", 170.0, "kg", 
                "Organic split green gram. Easy to digest and high in protein.", 
                grains, "Active"),
            createProduct("Organic Wheat Flour", 55.0, "kg", 
                "Stone-ground organic whole wheat flour. Perfect for rotis and breads.", 
                grains, "Active"),

            // Dairy Products
            createProduct("Organic Milk", 65.0, "liter", 
                "Fresh organic cow milk. Free from hormones and antibiotics. Rich in calcium and protein.", 
                dairy, "Active"),
            createProduct("Organic Ghee", 650.0, "500g", 
                "Pure organic clarified butter. Made from organic milk, rich in healthy fats.", 
                dairy, "Active"),
            createProduct("Organic Curd", 50.0, "500g", 
                "Fresh organic curd made from organic milk. Probiotic-rich and creamy.", 
                dairy, "Active"),
            createProduct("Organic Paneer", 320.0, "kg", 
                "Fresh organic cottage cheese. High in protein and perfect for cooking.", 
                dairy, "Active"),

            // Beverages
            createProduct("Organic Green Tea", 350.0, "100g", 
                "Premium organic green tea leaves. Rich in antioxidants and natural flavor.", 
                beverages, "Active"),
            createProduct("Organic Coffee Beans", 450.0, "250g", 
                "Single-origin organic coffee beans. Medium roast with rich, smooth flavor.", 
                beverages, "Active"),
            createProduct("Organic Coconut Water", 40.0, "500ml", 
                "Fresh organic coconut water. Natural electrolytes and refreshing taste.", 
                beverages, "Active"),
            createProduct("Organic Herbal Tea", 280.0, "100g", 
                "Blend of organic herbs including tulsi, ginger, and lemongrass. Soothing and aromatic.", 
                beverages, "Active"),

            // Personal Care
            createProduct("Organic Aloe Vera Gel", 150.0, "200g", 
                "Pure organic aloe vera gel. Perfect for skin care, sunburn relief, and moisturizing.", 
                personalCare, "Active"),
            createProduct("Bamboo Toothbrush", 80.0, "piece", 
                "Eco-friendly bamboo toothbrush with soft bristles. Biodegradable and sustainable.", 
                personalCare, "Active"),
            createProduct("Organic Herbal Shampoo", 220.0, "200ml", 
                "Natural organic shampoo with neem and amla. Gentle on hair and scalp.", 
                personalCare, "Active"),
            createProduct("Organic Face Wash", 180.0, "100ml", 
                "Gentle organic face wash with neem and tulsi. Cleanses without drying.", 
                personalCare, "Active"),
            createProduct("Organic Soap", 120.0, "100g", 
                "Handmade organic soap with natural ingredients. Moisturizing and gentle.", 
                personalCare, "Active"),

            // Household Items
            createProduct("Organic Floor Cleaner", 180.0, "500ml", 
                "Natural organic floor cleaner. Safe for kids and pets, effective cleaning.", 
                household, "Active"),
            createProduct("Organic Dish Soap", 150.0, "500ml", 
                "Eco-friendly organic dish soap. Cuts through grease naturally.", 
                household, "Active"),
            createProduct("Organic Laundry Detergent", 250.0, "1kg", 
                "Biodegradable organic laundry detergent. Gentle on clothes and environment.", 
                household, "Active"),
            createProduct("Coconut Coir Scrub Pad", 50.0, "piece", 
                "Natural coconut coir scrub pad. Perfect for cleaning without chemicals.", 
                household, "Active"),

            // Snacks
            createProduct("Organic Multigrain Chips", 120.0, "150g", 
                "Crispy organic multigrain chips. Baked, not fried. Healthy snacking option.", 
                snacks, "Active"),
            createProduct("Organic Roasted Almonds", 450.0, "250g", 
                "Premium organic roasted almonds. Rich in protein and healthy fats.", 
                snacks, "Active"),
            createProduct("Organic Popcorn", 80.0, "200g", 
                "Organic popcorn kernels. Perfect for healthy snacking. No preservatives.", 
                snacks, "Active"),
            createProduct("Organic Trail Mix", 350.0, "200g", 
                "Mix of organic nuts, seeds, and dried fruits. Energy-boosting snack.", 
                snacks, "Active"),

            // Honey & Jams
            createProduct("Organic Forest Honey", 450.0, "500g", 
                "Pure organic wild forest honey. Unprocessed and rich in natural enzymes.", 
                honey, "Active"),
            createProduct("Organic Strawberry Jam", 180.0, "250g", 
                "Homemade organic strawberry jam. No artificial preservatives or colors.", 
                honey, "Active"),
            createProduct("Organic Mango Jam", 170.0, "250g", 
                "Delicious organic mango jam. Made from fresh organic mangoes.", 
                honey, "Active")
        );

        productRepository.saveAll(products);
    }

    private Product createProduct(String name, Double price, String unit, 
                                  String description, Category category, String status) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setUnit(unit);
        product.setDescription(description);
        product.setCategory(category);
        product.setStatus(status);
        return product;
    }
}
