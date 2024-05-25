package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductResponseDto;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductRequestDto;
import id.ac.ui.cs.advprog.heymartbeproduct.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<ProductResponseDto>> create(
            @RequestBody ProductRequestDto productRequestDto) {
        return productService.create(productRequestDto)
                .thenApply(product -> new ResponseEntity<>(product, HttpStatus.CREATED));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable String id) {
        ProductResponseDto product = productService.findById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductResponseDto>> edit(@PathVariable String id,
            @RequestBody ProductRequestDto productRequestDto) {
        return productService.edit(id, productRequestDto)
                .thenApply(product -> new ResponseEntity<>(product, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable String id) {
        productService.deleteById(id);
        return new ResponseEntity<>("DELETE SUCCESS", HttpStatus.OK);
    }

    @GetMapping("/list")
    public CompletableFuture<ResponseEntity<List<ProductResponseDto>>> getAllProducts() {
        return productService.getAllProducts()
                .thenApply(products -> new ResponseEntity<>(products, HttpStatus.OK));
    }
}