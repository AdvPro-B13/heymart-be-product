package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductDto;
import id.ac.ui.cs.advprog.heymartbeproduct.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<ProductDto>> create(@RequestBody ProductDto productDto) {
        return productService.create(productDto)
                .thenApply(product -> new ResponseEntity<>(product, HttpStatus.CREATED));
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductDto>> findById(@PathVariable String id) {
        return productService.findById(id)
                .thenApply(product -> new ResponseEntity<>(product, HttpStatus.OK));
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<ProductDto>> edit(@PathVariable String id,
            @RequestBody ProductDto productDto) {
        productDto.setId(id);
        return productService.edit(productDto)
                .thenApply(product -> new ResponseEntity<>(product, HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> deleteById(@PathVariable String id) {
        return productService.deleteById(id)
                .thenApply(v -> new ResponseEntity<>("DELETE SUCCESS", HttpStatus.OK));
    }

    @GetMapping("/list")
    public CompletableFuture<ResponseEntity<List<ProductDto>>> getAllProducts() {
        return productService.getAllProducts()
                .thenApply(products -> new ResponseEntity<>(products, HttpStatus.OK));
    }
}