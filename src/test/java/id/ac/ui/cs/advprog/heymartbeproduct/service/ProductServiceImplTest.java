package id.ac.ui.cs.advprog.heymartbeproduct.service;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        when(productRepository.saveProduct(product)).thenReturn(product);

        Product createdProduct = productService.create(product);

        assertEquals(product, createdProduct);
        verify(productRepository, times(1)).saveProduct(product);
    }

    @Test
    void testFindProductById() {
        Product product = new Product();
        when(productRepository.findProductById("eb558e9f-1c39-460e-8860-71af6af63bd6")).thenReturn(Optional.of(product));

        Product foundProduct = productService.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        assertEquals(product, foundProduct);
        verify(productRepository, times(1)).findProductById("eb558e9f-1c39-460e-8860-71af6af63bd6");
    }

    @Test
    void testEditProduct() {
        Product product = new Product();
        when(productRepository.saveProduct(product)).thenReturn(product);

        Product editedProduct = productService.edit(product);

        assertEquals(product, editedProduct);
        verify(productRepository, times(1)).saveProduct(product);
    }

    @Test
    void testDeleteProductById() {
        doNothing().when(productRepository).deleteProductById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        productService.deleteById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        verify(productRepository, times(1)).deleteProductById("eb558e9f-1c39-460e-8860-71af6af63bd6");
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.getAllProducts()).thenReturn(products);

        List<Product> returnedProducts = productService.getAllProducts();

        assertEquals(products, returnedProducts);
        verify(productRepository, times(1)).getAllProducts();
    }
}