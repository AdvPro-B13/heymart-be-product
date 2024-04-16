package id.ac.ui.cs.advprog.heymartbeproduct.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) {
        // Implementations will be added here
        return null;
    }

    @Override
    public Product findById(String id) {
        // Implementations will be added here
        return null;
    }

    @Override
    public Product edit(Product product) {
        // Implementations will be added here
        return null;
    }

    @Override
    public void deleteById(String id) {
        // Implementations will be added here
    }

    @Override
    public List<Product> getAllProducts() {
        // Implementations will be added here
        return null;
    }
}
