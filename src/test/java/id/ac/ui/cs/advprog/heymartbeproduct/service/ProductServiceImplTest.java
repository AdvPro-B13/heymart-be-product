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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.empty());
        when(productRepository.saveProduct(product)).thenReturn(product);

        Product createdProduct = productService.create(product);

        assertEquals(product, createdProduct);
        verify(productRepository, times(1)).saveProduct(product);
    }

    @Test
    void testCreateProductWithExistingId() {
        Product product = new Product();
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () -> productService.create(product));
    }

    @Test
    void testCreateProductWithNull() {
        assertThrows(IllegalArgumentException.class, () -> productService.create(null));
    }

    @Test
    void testFindProductById() {
        Product product = new Product();
        when(productRepository.findProductById("eb558e9f-1c39-460e-8860-71af6af63bd6"))
                .thenReturn(Optional.of(product));

        Product foundProduct = productService.findById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        assertEquals(product, foundProduct);
        verify(productRepository, times(1)).findProductById("eb558e9f-1c39-460e-8860-71af6af63bd6");
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
        Product product = new Product();
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.of(product));
        when(productRepository.saveProduct(product)).thenReturn(product);

        Product editedProduct = productService.edit(product);

        assertEquals(product, editedProduct);
        verify(productRepository, times(1)).saveProduct(product);
    }

    @Test
    void testEditProductNotFound() {
        Product product = new Product();
        when(productRepository.findProductById(product.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productService.edit(product));
    }

    @Test
    void testEditProductNull() {
        assertThrows(IllegalArgumentException.class, () -> productService.edit(null));
    }

    @Test
    void testDeleteProductById() {
        when(productRepository.findProductById("eb558e9f-1c39-460e-8860-71af6af63bd6"))
                .thenReturn(Optional.of(new Product()));
        doNothing().when(productRepository).deleteProductById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        productService.deleteById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        verify(productRepository, times(1)).deleteProductById("eb558e9f-1c39-460e-8860-71af6af63bd6");
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
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.getAllProducts()).thenReturn(products);

        List<Product> returnedProducts = productService.getAllProducts();

        assertEquals(products, returnedProducts);
        verify(productRepository, times(1)).getAllProducts();
    }
}