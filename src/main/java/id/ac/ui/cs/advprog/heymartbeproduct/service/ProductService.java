package id.ac.ui.cs.advprog.heymartbeproduct.service;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.heymartbeproduct.Dto.ProductDto;

import java.util.List;

@Service
public interface ProductService {
    ProductDto create(ProductDto productDto);

    ProductDto findById(String id);

    ProductDto edit(ProductDto productDto);

    void deleteById(String id);

    List<ProductDto> getAllProducts();
}