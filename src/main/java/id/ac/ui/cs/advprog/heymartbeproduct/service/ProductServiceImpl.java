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
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (productRepository.findProductById(product.getId()).isPresent()) {
            throw new IllegalArgumentException("Product with this ID already exists");
        }
        return productRepository.saveProduct(product);
    }

    @Override
    public Product findById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        return productRepository.findProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product isn't found"));
    }

    @Override
    public Product edit(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!productRepository.findProductById(product.getId()).isPresent()) {
            throw new IllegalArgumentException("Product not found");
        }
        return productRepository.saveProduct(product);
    }

    @Override
    public void deleteById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (!productRepository.findProductById(id).isPresent()) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteProductById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }
}
