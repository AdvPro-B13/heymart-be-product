package id.ac.ui.cs.advprog.heymartbeproduct.service;

import java.util.List;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;

@Service
public interface ProductService {
    Product create(Product product);

    Product findById(String id);

    Product edit(Product product);

    void deleteById(String id);

    List<Product> getAllProducts();
}
