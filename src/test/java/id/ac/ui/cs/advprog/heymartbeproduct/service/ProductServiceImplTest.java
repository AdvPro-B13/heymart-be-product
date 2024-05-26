package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductResponseDto;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductRequestDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Define a taskExecutor bean
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setQueueCapacity(500);
        taskExecutor.setThreadNamePrefix("Test-");
        taskExecutor.initialize();

        // Set the taskExecutor in productService
        ReflectionTestUtils.setField(productService, "taskExecutor", taskExecutor);
    }

    @Test
    public void testFindProductByIdAndSupermarketId_ProductFound() {
        String id = "product123";
        Long supermarketId = 1L;
        Product product = new Product();
        ProductResponseDto productResponseDto = new ProductResponseDto();

        when(productRepository.findProductByIdAndSupermarketId(id, supermarketId)).thenReturn(Optional.of(product));
        when(productMapper.convertToDto(product)).thenReturn(productResponseDto);

        ProductResponseDto result = productService.findProductByIdAndSupermarketId(id, supermarketId);

        assertNotNull(result);
        assertEquals(productResponseDto, result);
    }

    @Test
    public void testFindProductByIdAndSupermarketId_ProductNotFound() {
        String id = "product123";
        Long supermarketId = 1L;

        when(productRepository.findProductByIdAndSupermarketId(id, supermarketId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            productService.findProductByIdAndSupermarketId(id, supermarketId);
        });
    }

    @Test
    public void testFindProductsBySupermarketId_ProductsFound() {
        Long supermarketId = 1L;
        Product product = new Product();
        ProductResponseDto productResponseDto = new ProductResponseDto();
        List<Product> products = Collections.singletonList(product);

        when(productRepository.findProductsBySupermarketId(supermarketId)).thenReturn(products);
        when(productMapper.convertToDto(product)).thenReturn(productResponseDto);

        List<ProductResponseDto> result = productService.findProductsBySupermarketId(supermarketId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(productResponseDto, result.get(0));
    }

    @Test
    public void testFindProductsBySupermarketId_NoProductsFound() {
        Long supermarketId = 1L;

        when(productRepository.findProductsBySupermarketId(supermarketId)).thenReturn(Collections.emptyList());

        List<ProductResponseDto> result = productService.findProductsBySupermarketId(supermarketId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateProduct() throws Exception {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductRequestDto productRequestDto = new ProductRequestDto();
        when(productMapper.convertToEntity(productRequestDto)).thenReturn(product);
        when(productRepository.saveProduct(product)).thenReturn(product);
        ProductResponseDto productDto = productMapper.convertToDto(product);
        when(productMapper.convertToDto(product)).thenReturn(productDto);

        CompletableFuture<ProductResponseDto> future = productService.create(productRequestDto);
        ProductResponseDto savedProductDto = future.get();

        assertEquals(productDto, savedProductDto);
        verify(productRepository, times(1)).saveProduct(product);
    }

    @Test
    void testEditProduct() throws Exception {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductRequestDto productRequestDto = new ProductRequestDto();
        when(productMapper.convertToEntity(productRequestDto)).thenReturn(product);
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.saveProduct(product)).thenReturn(product);
        ProductResponseDto productDto = productMapper.convertToDto(product);
        when(productMapper.convertToDto(product)).thenReturn(productDto);

        CompletableFuture<ProductResponseDto> future = productService.edit(product.getId(), productRequestDto);
        ProductResponseDto editedProductDto = future.get();

        assertEquals(productDto, editedProductDto);
        verify(productRepository, times(1)).saveProduct(product);
    }

    @Test
    void testFindProductById() {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductResponseDto productDto = new ProductResponseDto();
        String productId = product.getId();
        when(productRepository.findProductById(productId)).thenReturn(Optional.of(product));
        when(productMapper.convertToDto(product)).thenReturn(productDto);

        ProductResponseDto foundProductDto = productService.findById(productId);

        assertEquals(productDto, foundProductDto);
        verify(productRepository, times(1)).findProductById(productId);
    }

    @Test
    void testFindProductByIdNonexistent() {
        String productId = "nonexistent";
        when(productRepository.findProductById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productService.findById(productId));
        verify(productRepository, times(1)).findProductById(productId);
    }

    @Test
    void testDeleteProductById() {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteProductById(product.getId());

        productService.deleteById(product.getId());
        verify(productRepository, times(1)).deleteProductById(product.getId());
    }

    @Test
    void testDeleteProductByIdNotFound() {
        String productId = "nonexistent";
        when(productRepository.findProductById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productService.deleteById(productId));
        verify(productRepository, times(1)).findProductById(productId);
    }

    @Test
    void testDeleteProductByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> productService.deleteById(null));
        verify(productRepository, never()).findProductById(anyString());
    }

    @Test
    void testDeleteProductByIdEmpty() {
        assertThrows(IllegalArgumentException.class, () -> productService.deleteById(""));
        verify(productRepository, never()).findProductById(anyString());
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product product1 = new Product.ProductBuilder("Product 1", 5.0, 2).build();
        Product product2 = new Product.ProductBuilder("Product 2", 1.99, 3).build();
        ProductResponseDto productDto1 = new ProductResponseDto();
        ProductResponseDto productDto2 = new ProductResponseDto();
        List<Product> products = Arrays.asList(product1, product2);
        List<ProductResponseDto> productDtos = Arrays.asList(productDto1, productDto2);
        when(productRepository.getAllProducts()).thenReturn(products);
        when(productMapper.convertToDto(product1)).thenReturn(productDto1);
        when(productMapper.convertToDto(product2)).thenReturn(productDto2);

        CompletableFuture<List<ProductResponseDto>> future = productService.getAllProducts();
        List<ProductResponseDto> returnedProductDtos = future.get();

        assertEquals(productDtos, returnedProductDtos);
        verify(productRepository, times(1)).getAllProducts();
    }

    @Test
    void testEditProductWithNullProductRequestDto() {
        ProductRequestDto productRequestDto = null;
        String productId = "1";

        ExecutionException exception = assertThrows(ExecutionException.class,
                () -> productService.edit(productId, productRequestDto).get());
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        verify(productRepository, never()).findProductById(anyString());
        verify(productRepository, never()).saveProduct(any(Product.class));
    }

    @Test
    void testEditProductWithNonexistentId() throws Exception {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductRequestDto productRequestDto = new ProductRequestDto();
        when(productMapper.convertToEntity(productRequestDto)).thenReturn(product);
        String productId = "nonexistent";
        when(productRepository.findProductById(productId)).thenReturn(Optional.empty());

        ExecutionException exception = assertThrows(ExecutionException.class,
                () -> productService.edit(productId, productRequestDto).get());
        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        verify(productRepository, times(1)).findProductById(productId);
        verify(productRepository, never()).saveProduct(any(Product.class));
    }
}