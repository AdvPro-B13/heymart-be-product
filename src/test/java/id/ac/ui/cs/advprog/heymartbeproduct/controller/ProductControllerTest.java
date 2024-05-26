package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductRequestDto;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductResponseDto;
import id.ac.ui.cs.advprog.heymartbeproduct.service.AuthServiceClient;
import id.ac.ui.cs.advprog.heymartbeproduct.service.ProductService;
import id.ac.ui.cs.advprog.heymartbeproduct.enums.ErrorResponse;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private AuthServiceClient authServiceClient;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() throws ExecutionException, InterruptedException {
        ProductResponseDto productDto = new ProductResponseDto();
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(productService.create(any())).thenReturn(CompletableFuture.completedFuture(productDto));

        ResponseEntity<Object> responseEntity = productController.create(new ProductRequestDto(), "auth").get();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testCreateProductUnauthorized() throws ExecutionException, InterruptedException {
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = productController.create(new ProductRequestDto(), "auth").get();

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.UNAUTHORIZED.getValue(), responseEntity.getBody());
    }

    @Test
    void testEditProduct() throws ExecutionException, InterruptedException {
        String id = "1";
        ProductResponseDto productDto = new ProductResponseDto();
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(productService.edit(anyString(), any())).thenReturn(CompletableFuture.completedFuture(productDto));

        ResponseEntity<Object> responseEntity = productController.edit(id, new ProductRequestDto(), "auth").get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testEditProductUnauthorized() throws ExecutionException, InterruptedException {
        String id = "1";
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = productController.edit(id, new ProductRequestDto(), "auth").get();

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.UNAUTHORIZED.getValue(), responseEntity.getBody());
    }

    @Test
    void testEditProductNotFound() throws ExecutionException, InterruptedException {
        String id = "1";
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(productService.edit(anyString(), any()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException()));

        ResponseEntity<Object> responseEntity = productController.edit(id, new ProductRequestDto(), "auth").get();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.NOT_FOUND.getValue(), responseEntity.getBody());
    }

    @Test
    void testFindProductById() {
        String id = "1";
        ProductResponseDto productDto = new ProductResponseDto();
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(productService.findById(id)).thenReturn(productDto);

        ResponseEntity<Object> responseEntity = productController.findById(id, "auth");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testFindProductByIdUnauthorized() {
        String id = "1";
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = productController.findById(id, "auth");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.UNAUTHORIZED.getValue(), responseEntity.getBody());
    }

    @Test
    void testFindProductByIdNotFound() {
        String id = "1";
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(productService.findById(id)).thenThrow(new RuntimeException());

        ResponseEntity<Object> responseEntity = productController.findById(id, "auth");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.NOT_FOUND.getValue(), responseEntity.getBody());
    }

    @Test
    void testDeleteProductById() {
        String id = "1";
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        doNothing().when(productService).deleteById(id);

        ResponseEntity<Object> responseEntity = productController.deleteById(id, "auth");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("DELETE SUCCESS", responseEntity.getBody());
    }

    @Test
    void testDeleteProductByIdUnauthorized() {
        String id = "1";
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = productController.deleteById(id, "auth");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.UNAUTHORIZED.getValue(), responseEntity.getBody());
    }

    @Test
    void testDeleteProductByIdNotFound() {
        String id = "1";
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        doThrow(new RuntimeException()).when(productService).deleteById(id);

        ResponseEntity<Object> responseEntity = productController.deleteById(id, "auth");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.NOT_FOUND.getValue(), responseEntity.getBody());
    }

    @Test
    void testGetAllProducts() throws ExecutionException, InterruptedException {
        List<ProductResponseDto> productDtoList = Collections.emptyList();
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(productService.getAllProducts()).thenReturn(CompletableFuture.completedFuture(productDtoList));

        ResponseEntity<Object> responseEntity = productController.getAllProducts("auth").get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDtoList, responseEntity.getBody());
    }

    @Test
    void testGetAllProductsUnauthorized() throws ExecutionException, InterruptedException {
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = productController.getAllProducts("auth").get();

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.UNAUTHORIZED.getValue(), responseEntity.getBody());
    }

    @Test
    void testFindProductsBySupermarketIdUnauthorized() {
        Long supermarketId = 1L;
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = productController.findProductsBySupermarketId(supermarketId, "auth");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.UNAUTHORIZED.getValue(), responseEntity.getBody());
    }

    @Test
    void testFindProductsBySupermarketIdNotFound() {
        Long supermarketId = 1L;
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(productService.findProductsBySupermarketId(supermarketId)).thenThrow(new RuntimeException());

        ResponseEntity<Object> responseEntity = productController.findProductsBySupermarketId(supermarketId, "auth");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.NOT_FOUND.getValue(), responseEntity.getBody());
    }

    @Test
    void testFindProductByIdAndSupermarketIdUnauthorized() {
        Long supermarketId = 1L;
        String productId = "1";
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = productController.findProductByIdAndSupermarketId(supermarketId,
                productId, "auth");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.UNAUTHORIZED.getValue(), responseEntity.getBody());
    }

    @Test
    void testFindProductByIdAndSupermarketIdNotFound() {
        Long supermarketId = 1L;
        String productId = "1";
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(productService.findProductByIdAndSupermarketId(productId, supermarketId))
                .thenThrow(new RuntimeException());

        ResponseEntity<Object> responseEntity = productController.findProductByIdAndSupermarketId(supermarketId,
                productId, "auth");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ErrorResponse.NOT_FOUND.getValue(), responseEntity.getBody());
    }

    @Test
    void testFindProductsBySupermarketId() {
        Long supermarketId = 1L;
        List<ProductResponseDto> productDtoList = Collections.emptyList();
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(productService.findProductsBySupermarketId(supermarketId)).thenReturn(productDtoList);

        ResponseEntity<Object> responseEntity = productController.findProductsBySupermarketId(supermarketId, "auth");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDtoList, responseEntity.getBody());
    }

    @Test
    void testFindProductByIdAndSupermarketId() {
        Long supermarketId = 1L;
        String productId = "1";
        ProductResponseDto productDto = new ProductResponseDto();
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(productService.findProductByIdAndSupermarketId(productId, supermarketId)).thenReturn(productDto);

        ResponseEntity<Object> responseEntity = productController.findProductByIdAndSupermarketId(supermarketId,
                productId, "auth");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }
}
