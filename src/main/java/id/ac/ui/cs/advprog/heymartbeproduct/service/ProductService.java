package id.ac.ui.cs.advprog.heymartbeproduct.service;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductRequestDto;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductResponseDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface ProductService {
    CompletableFuture<ProductResponseDto> create(ProductRequestDto productDto);

    ProductResponseDto findById(String id);

    CompletableFuture<ProductResponseDto> edit(String id, ProductRequestDto productDto);

    void deleteById(String id);

    CompletableFuture<List<ProductResponseDto>> getAllProducts();
}