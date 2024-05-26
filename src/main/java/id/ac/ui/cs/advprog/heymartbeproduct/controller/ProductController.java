package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductResponseDto;
import id.ac.ui.cs.advprog.heymartbeproduct.enums.ErrorResponse;
import id.ac.ui.cs.advprog.heymartbeproduct.enums.ProductAction;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductRequestDto;
import id.ac.ui.cs.advprog.heymartbeproduct.service.AuthServiceClient;
import id.ac.ui.cs.advprog.heymartbeproduct.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final AuthServiceClient authServiceClient;
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService, AuthServiceClient authServiceClient) {
        this.productService = productService;
        this.authServiceClient = authServiceClient;
    }

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<Object>> create(
            @RequestBody ProductRequestDto productRequestDto,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!authServiceClient.verifyUserAuthorization(ProductAction.CREATE.getValue(), authorizationHeader)) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.UNAUTHORIZED.getValue()));
        }
        return productService.create(productRequestDto)
                .thenApply(product -> new ResponseEntity<>(product, HttpStatus.CREATED));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!authServiceClient.verifyUserAuthorization(ProductAction.READ.getValue(), authorizationHeader)) {
            return new ResponseEntity<>(ErrorResponse.UNAUTHORIZED.getValue(), HttpStatus.UNAUTHORIZED);
        }
        try {
            ProductResponseDto product = productService.findById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorResponse.NOT_FOUND.getValue(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("edit/{id}")
    public CompletableFuture<ResponseEntity<Object>> edit(
            @PathVariable String id,
            @RequestBody ProductRequestDto productRequestDto,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!authServiceClient.verifyUserAuthorization(ProductAction.EDIT.getValue(), authorizationHeader)) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.UNAUTHORIZED.getValue()));
        }
        return productService.edit(id, productRequestDto)
                .thenApply(productResponseDto -> {

                    Object responseBody = (Object) productResponseDto;
                    return new ResponseEntity<>(responseBody, HttpStatus.OK);
                })
                .exceptionally(
                        e -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.NOT_FOUND.getValue()));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteById(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!authServiceClient.verifyUserAuthorization(ProductAction.DELETE.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.UNAUTHORIZED.getValue());
        }
        try {
            productService.deleteById(id);
            return new ResponseEntity<>("DELETE SUCCESS", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.NOT_FOUND.getValue());
        }
    }

    @GetMapping("/list")
    public CompletableFuture<ResponseEntity<Object>> getAllProducts(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!authServiceClient.verifyUserAuthorization(ProductAction.READ.getValue(), authorizationHeader)) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.UNAUTHORIZED.getValue()));
        }
        return productService.getAllProducts()
                .thenApply(products -> new ResponseEntity<>(products, HttpStatus.OK));
    }
}