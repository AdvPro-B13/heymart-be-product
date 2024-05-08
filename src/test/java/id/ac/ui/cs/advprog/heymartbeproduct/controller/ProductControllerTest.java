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
    void testCreateProduct() {
        ProductDto productDto = new ProductDto();
        when(productService.create(any())).thenReturn(productDto);

        ResponseEntity<ProductDto> responseEntity = productController.create(productDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testFindProductById() {
        String id = "1";
        ProductDto productDto = new ProductDto();
        when(productService.findById(id)).thenReturn(productDto);

        ResponseEntity<ProductDto> responseEntity = productController.findById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testEditProduct() {
        String id = "1";
        ProductDto productDto = new ProductDto();
        when(productService.edit(any())).thenReturn(productDto);

        ResponseEntity<ProductDto> responseEntity = productController.edit(id, productDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    void testDeleteProductById() {
        String id = "1";

        ResponseEntity<String> responseEntity = productController.deleteById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("DELETE SUCCESS", responseEntity.getBody());
        verify(productService, times(1)).deleteById(id);
    }

    @Test
    void testGetAllProducts() {
        List<ProductDto> productDtoList = Collections.emptyList();
        when(productService.getAllProducts()).thenReturn(productDtoList);

        ResponseEntity<List<ProductDto>> responseEntity = productController.getAllProducts();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDtoList, responseEntity.getBody());
    }
}