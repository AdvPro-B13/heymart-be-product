package id.ac.ui.cs.advprog.heymartbeproduct.service;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface ProductService {
    CompletableFuture<ProductDto> create(ProductDto productDto);

    CompletableFuture<ProductDto> findById(String id);

    CompletableFuture<ProductDto> edit(ProductDto productDto);

    CompletableFuture<Void> deleteById(String id);

    CompletableFuture<List<ProductDto>> getAllProducts();
}