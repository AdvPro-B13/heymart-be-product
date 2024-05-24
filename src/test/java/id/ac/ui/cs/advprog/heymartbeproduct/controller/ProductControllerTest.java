package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductRequestDto;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductResponseDto;
import id.ac.ui.cs.advprog.heymartbeproduct.service.ProductService;

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

    @InjectMocks
    private ProductController productController;

    public ProductControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() throws ExecutionException, InterruptedException {
        ProductResponseDto productDto = new ProductResponseDto();
        when(productService.create(any())).thenReturn(CompletableFuture.completedFuture(productDto));

        ResponseEntity<ProductResponseDto> responseEntity = productController.create(new ProductRequestDto()).get();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testEditProduct() throws ExecutionException, InterruptedException {
        String id = "1";
        ProductResponseDto productDto = new ProductResponseDto();
        when(productService.edit(anyString(), any())).thenReturn(CompletableFuture.completedFuture(productDto));

        ResponseEntity<ProductResponseDto> responseEntity = productController.edit(id, new ProductRequestDto()).get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testFindProductById() {
        String id = "1";
        ProductResponseDto productDto = new ProductResponseDto();
        when(productService.findById(id)).thenReturn(productDto);

        ResponseEntity<ProductResponseDto> responseEntity = productController.findById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testDeleteProductById() {
        String id = "1";
        doNothing().when(productService).deleteById(id);

        ResponseEntity<String> responseEntity = productController.deleteById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("DELETE SUCCESS", responseEntity.getBody());
    }

    @Test
    void testGetAllProducts() throws ExecutionException, InterruptedException {
        List<ProductResponseDto> productDtoList = Collections.emptyList();
        when(productService.getAllProducts()).thenReturn(CompletableFuture.completedFuture(productDtoList));

        ResponseEntity<List<ProductResponseDto>> responseEntity = productController.getAllProducts().get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDtoList, responseEntity.getBody());
    }
}