package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.Dto.ProductDto;
import id.ac.ui.cs.advprog.heymartbeproduct.Dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    }

    @Test
    void testCreateProduct() {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductDto productDto = new ProductDto();
        when(productMapper.convertToEntity(productDto)).thenReturn(product);
        when(productRepository.saveProduct(product)).thenReturn(product);
        when(productMapper.convertToDto(product)).thenReturn(productDto);

        ProductDto savedProductDto = productService.create(productDto);

        assertEquals(productDto, savedProductDto);
        verify(productRepository, times(1)).findProductById(product.getId());
    }

    @Test
    void testCreateProductWithExistingId() {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductDto productDto = new ProductDto();
        when(productMapper.convertToEntity(productDto)).thenReturn(product);
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () -> productService.create(productDto));
    }

    @Test
    void testCreateProductWithNull() {
        assertThrows(IllegalArgumentException.class, () -> productService.create(null));
    }

    @Test
    void testFindProductById() {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductDto productDto = new ProductDto();
        String productId = product.getId();
        when(productRepository.findProductById(productId)).thenReturn(Optional.of(product));
        when(productMapper.convertToDto(product)).thenReturn(productDto);

        ProductDto foundProductDto = productService.findById(productId);

        assertEquals(productDto, foundProductDto);
        verify(productRepository, times(1)).findProductById(productId);
    }

    @Test
    void testFindProductByIdNotFound() {
        when(productRepository.findProductById("eb558e9f-1c39-460e-8860-71af6af63bd6"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> productService.findById("eb558e9f-1c39-460e-8860-71af6af63bd6"));
    }

    @Test
    void testFindProductByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> productService.findById(null));
    }

    @Test
    void testFindProductByIdEmpty() {
        assertThrows(IllegalArgumentException.class, () -> productService.findById(""));
    }

    @Test
    void testEditProduct() {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductDto productDto = new ProductDto();
        when(productMapper.convertToEntity(productDto)).thenReturn(product);
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.saveProduct(product)).thenReturn(product);
        when(productMapper.convertToDto(product)).thenReturn(productDto);

        ProductDto editedProductDto = productService.edit(productDto);

        assertEquals(productDto, editedProductDto);
        verify(productRepository, times(1)).saveProduct(product);
    }

    @Test
    void testEditProductNotFound() {
        ProductDto productDto = new ProductDto();
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        when(productMapper.convertToEntity(productDto)).thenReturn(product);
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productService.edit(productDto));
    }

    @Test
    void testEditProductNull() {
        assertThrows(IllegalArgumentException.class, () -> productService.edit(null));
    }

    @Test
    void testDeleteProductById() {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        when(productMapper.convertToEntity(productDto)).thenReturn(product);
        when(productRepository.saveProduct(product)).thenReturn(product);
        when(productMapper.convertToDto(product)).thenReturn(productDto);

        ProductDto savedProductDto = productService.create(productDto);
        String productId = savedProductDto.getId();

        assertNotNull(productId, "Product ID should not be null");
        when(productRepository.findProductById(productId)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteProductById(productId);

        productService.deleteById(productId);
        verify(productRepository, times(1)).deleteProductById(productId);
    }

    @Test
    void testDeleteProductByIdNotFound() {
        when(productRepository.findProductById("eb558e9f-1c39-460e-8860-71af6af63bd6")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> productService.deleteById("eb558e9f-1c39-460e-8860-71af6af63bd6"));
    }

    @Test
    void testDeleteProductByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> productService.deleteById(null));
    }

    @Test
    void testDeleteProductByIdEmpty() {
        assertThrows(IllegalArgumentException.class, () -> productService.deleteById(""));
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product.ProductBuilder("Product 1", 5.0, 2).build();
        Product product2 = new Product.ProductBuilder("Product 2", 1.99, 3).build();
        ProductDto productDto1 = new ProductDto();
        ProductDto productDto2 = new ProductDto();
        List<Product> products = Arrays.asList(product1, product2);
        List<ProductDto> productDtos = Arrays.asList(productDto1, productDto2);
        when(productRepository.getAllProducts()).thenReturn(products);
        when(productMapper.convertToDto(product1)).thenReturn(productDto1);
        when(productMapper.convertToDto(product2)).thenReturn(productDto2);

        List<ProductDto> returnedProductDtos = productService.getAllProducts();

        assertEquals(productDtos, returnedProductDtos);
        verify(productRepository, times(1)).getAllProducts();
    }
}