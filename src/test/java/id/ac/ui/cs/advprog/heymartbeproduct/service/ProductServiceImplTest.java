package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
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
    void testCreateProduct() throws Exception {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductDto productDto = new ProductDto();
        when(productMapper.convertToEntity(productDto)).thenReturn(product);
        when(productRepository.saveProduct(product)).thenReturn(product);
        when(productMapper.convertToDto(product)).thenReturn(productDto);

        CompletableFuture<ProductDto> future = productService.create(productDto);
        ProductDto savedProductDto = future.get();

        assertEquals(productDto, savedProductDto);
        verify(productRepository, times(1)).findProductById(product.getId());
    }

    @Test
    void testFindProductById() throws Exception {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductDto productDto = new ProductDto();
        String productId = product.getId();
        when(productRepository.findProductById(productId)).thenReturn(Optional.of(product));
        when(productMapper.convertToDto(product)).thenReturn(productDto);

        CompletableFuture<ProductDto> future = productService.findById(productId);
        ProductDto foundProductDto = future.get();

        assertEquals(productDto, foundProductDto);
        verify(productRepository, times(1)).findProductById(productId);
    }

    @Test
    void testEditProduct() throws Exception {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName("Edited Product");
        productDto.setPrice(200.0);
        productDto.setQuantity(20);

        Product editedProduct = new Product.ProductBuilder(productDto.getName(), productDto.getPrice(),
                productDto.getQuantity()).build();
        editedProduct.setId(productDto.getId());

        when(productMapper.convertToEntity(productDto)).thenReturn(editedProduct);
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.saveProduct(editedProduct)).thenReturn(editedProduct);
        when(productMapper.convertToDto(editedProduct)).thenReturn(productDto);

        CompletableFuture<ProductDto> future = productService.edit(productDto);
        ProductDto editedProductDto = future.get();

        assertEquals(productDto, editedProductDto);
        assertEquals(productDto.getName(), editedProductDto.getName());
        assertEquals(productDto.getPrice(), editedProductDto.getPrice());
        assertEquals(productDto.getQuantity(), editedProductDto.getQuantity());
        verify(productRepository, times(1)).saveProduct(editedProduct);
    }

    @Test
    void testDeleteProductById() throws Exception {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        when(productMapper.convertToEntity(productDto)).thenReturn(product);
        when(productRepository.saveProduct(product)).thenReturn(product);
        when(productMapper.convertToDto(product)).thenReturn(productDto);

        CompletableFuture<ProductDto> future = productService.create(productDto);
        ProductDto savedProductDto = future.get();
        String productId = savedProductDto.getId();

        assertNotNull(productId, "Product ID should not be null");
        when(productRepository.findProductById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteProductById(productId);

        CompletableFuture<Void> deleteFuture = productService.deleteById(productId);
        deleteFuture.get();
        verify(productRepository, times(1)).deleteProductById(productId);
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product product1 = new Product.ProductBuilder("Product 1", 5.0, 2).build();
        Product product2 = new Product.ProductBuilder("Product 2", 1.99, 3).build();
        ProductDto productDto1 = new ProductDto();
        ProductDto productDto2 = new ProductDto();
        List<Product> products = Arrays.asList(product1, product2);
        List<ProductDto> productDtos = Arrays.asList(productDto1, productDto2);
        when(productRepository.getAllProducts()).thenReturn(products);
        when(productMapper.convertToDto(product1)).thenReturn(productDto1);
        when(productMapper.convertToDto(product2)).thenReturn(productDto2);

        CompletableFuture<List<ProductDto>> future = productService.getAllProducts();
        List<ProductDto> returnedProductDtos = future.get();

        assertEquals(productDtos, returnedProductDtos);
        verify(productRepository, times(1)).getAllProducts();
    }
}