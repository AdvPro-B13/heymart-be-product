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
        return productRepository.saveProduct(product);
    }

    @Override
    public Product findById(String id) {
        return productRepository.findProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Override
    public Product edit(Product product) {
        return productRepository.saveProduct(product);
    }

    @Override
    public void deleteById(String id) {
        productRepository.deleteProductById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }
}
