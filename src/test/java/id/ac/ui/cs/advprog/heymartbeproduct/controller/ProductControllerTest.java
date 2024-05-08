package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductDto;
import id.ac.ui.cs.advprog.heymartbeproduct.service.ProductService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        ProductDto productDto = new ProductDto();
        when(productService.create(any())).thenReturn(CompletableFuture.completedFuture(productDto));

        ResponseEntity<ProductDto> responseEntity = productController.create(productDto).get();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testFindProductById() throws ExecutionException, InterruptedException {
        String id = "1";
        ProductDto productDto = new ProductDto();
        when(productService.findById(id)).thenReturn(CompletableFuture.completedFuture(productDto));

        ResponseEntity<ProductDto> responseEntity = productController.findById(id).get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testEditProduct() throws ExecutionException, InterruptedException {
        String id = "1";
        ProductDto productDto = new ProductDto();
        when(productService.edit(any())).thenReturn(CompletableFuture.completedFuture(productDto));

        ResponseEntity<ProductDto> responseEntity = productController.edit(id, productDto).get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testDeleteProductById() throws ExecutionException, InterruptedException {
        String id = "1";
        when(productService.deleteById(id)).thenReturn(CompletableFuture.completedFuture(null));

        ResponseEntity<String> responseEntity = productController.deleteById(id).get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("DELETE SUCCESS", responseEntity.getBody());
    }

    @Test
    void testGetAllProducts() throws ExecutionException, InterruptedException {
        List<ProductDto> productDtoList = Collections.emptyList();
        when(productService.getAllProducts()).thenReturn(CompletableFuture.completedFuture(productDtoList));

        ResponseEntity<List<ProductDto>> responseEntity = productController.getAllProducts().get();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDtoList, responseEntity.getBody());
    }
}