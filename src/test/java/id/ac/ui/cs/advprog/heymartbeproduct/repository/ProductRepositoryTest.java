package id.ac.ui.cs.advprog.heymartbeproduct.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class ProductRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Product> typedQuery;

    @InjectMocks
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindProductByIdAndSupermarketId_ProductExists() {
        Product product = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .setCategoryNames(new HashSet<>(Arrays.asList("Category1", "Category2")))
                .build();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");

        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("id", "1")).thenReturn(typedQuery);
        when(typedQuery.setParameter("supermarketId", 1L)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(product);

        Optional<Product> result = productRepository.findProductByIdAndSupermarketId("1", 1L);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());

        verify(entityManager).createQuery(anyString(), eq(Product.class));
        verify(typedQuery).setParameter("id", "1");
        verify(typedQuery).setParameter("supermarketId", 1L);
        verify(typedQuery).getSingleResult();
    }

    @Test
    void testFindProductByIdAndSupermarketId_ProductDoesNotExist() {
        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("id", "1")).thenReturn(typedQuery);
        when(typedQuery.setParameter("supermarketId", 1L)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(new NoResultException());

        Optional<Product> result = productRepository.findProductByIdAndSupermarketId("1", 1L);

        assertTrue(result.isEmpty());

        verify(entityManager).createQuery(anyString(), eq(Product.class));
        verify(typedQuery).setParameter("id", "1");
        verify(typedQuery).setParameter("supermarketId", 1L);
        verify(typedQuery).getSingleResult();
    }

    @Test
    void testFindProductsBySupermarketId() {
        Product product = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .setCategoryNames(new HashSet<>(Arrays.asList("Category1", "Category2")))
                .build();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");

        when(entityManager.createQuery(anyString(), eq(Product.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("supermarketId", 1L)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(product));

        List<Product> result = productRepository.findProductsBySupermarketId(1L);

        assertEquals(1, result.size());
        assertEquals(product, result.get(0));

        verify(entityManager).createQuery(anyString(), eq(Product.class));
        verify(typedQuery).setParameter("supermarketId", 1L);
        verify(typedQuery).getResultList();
    }

    @Test
    void testFindProductById() {
        Product product = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .setCategoryNames(new HashSet<>(Arrays.asList("Category1", "Category2")))
                .build();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");

        when(entityManager.find(Product.class, "eb558e9f-1c39-460e-8860-71af6af63bd6")).thenReturn(product);

        Optional<Product> result = productRepository.findProductById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        assertTrue(result.isPresent());
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", result.get().getId());
        verify(entityManager, times(1)).find(Product.class, "eb558e9f-1c39-460e-8860-71af6af63bd6");
    }

    @Test
    void testSaveProduct() {
        Product product = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .build();

        when(entityManager.find(Product.class, product.getId())).thenReturn(null);

        productRepository.saveProduct(product);

        verify(entityManager, times(1)).persist(product);
        verify(entityManager, times(0)).merge(product);
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .build();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setName("Old Product Name");

        when(entityManager.find(Product.class, product.getId())).thenReturn(product);

        Product updatedProduct = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .build();
        updatedProduct.setId(product.getId());
        updatedProduct.setName("New Product Name");

        when(entityManager.merge(updatedProduct)).thenReturn(updatedProduct);

        Product result = productRepository.saveProduct(updatedProduct);

        assertEquals("New Product Name", result.getName());
        verify(entityManager, times(0)).persist(updatedProduct);
        verify(entityManager, times(1)).merge(updatedProduct);
    }

    @Test
    void testDeleteProductById() {
        Product product = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .build();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");

        when(entityManager.find(Product.class, "eb558e9f-1c39-460e-8860-71af6af63bd6")).thenReturn(product);

        productRepository.deleteProductById("eb558e9f-1c39-460e-8860-71af6af63bd6");

        verify(entityManager, times(1)).remove(product);
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .build();
        product1.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");

        Product product2 = new Product.ProductBuilder("Product2", 4.99, 10)
                .setDescription("This is Product2")
                .build();
        product2.setId("gb128e9b-4a31-111e-7277-71af1cq63bd2");

        List<Product> products = Arrays.asList(product1, product2);

        @SuppressWarnings("unchecked")
        TypedQuery<Product> query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(products);
        when(entityManager.createQuery("SELECT p FROM Product p", Product.class)).thenReturn(query);

        List<Product> result = productRepository.getAllProducts();

        assertEquals(2, result.size());
        verify(entityManager, times(1)).createQuery("SELECT p FROM Product p", Product.class);
    }

    @Test
    void testGetAllProductsWhenEmpty() {
        @SuppressWarnings("unchecked")
        TypedQuery<Product> query = mock(TypedQuery.class);
        when(query.getResultList()).thenReturn(Collections.emptyList());
        when(entityManager.createQuery("SELECT p FROM Product p", Product.class)).thenReturn(query);

        List<Product> result = productRepository.getAllProducts();

        assertTrue(result.isEmpty());
        verify(entityManager, times(1)).createQuery("SELECT p FROM Product p", Product.class);
    }

    @Test
    void testDeleteProductByIdWhenProductExists() {
        Product product = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .build();
        product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");

        when(entityManager.find(Product.class, product.getId())).thenReturn(product);

        productRepository.deleteProductById(product.getId());

        verify(entityManager, times(1)).remove(product);
    }

    @Test
    void testDeleteProductByIdWhenProductDoesNotExist() {
        String id = "nonexistentId";
        when(entityManager.find(Product.class, id)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> productRepository.deleteProductById(id));
        verify(entityManager, times(0)).remove(any());
    }

    @Test
    void testSaveProductWhenProductExists() {
        Product product = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .build();
        product.setId("prod-123");

        when(entityManager.find(Product.class, product.getId())).thenReturn(product);
        when(entityManager.merge(product)).thenReturn(product);

        Product result = productRepository.saveProduct(product);

        verify(entityManager, times(0)).persist(product);
        verify(entityManager, times(1)).merge(product);
        assertEquals(product, result);
    }

    @ParameterizedTest
    @MethodSource("provideProductsForSaveTest")
    void testSaveProduct(Product product, Product existingProduct, Product expectedProduct) {
        when(entityManager.find(Product.class, product.getId())).thenReturn(existingProduct);

        Product result = productRepository.saveProduct(product);

        verify(entityManager, times(1)).persist(product);
        verify(entityManager, times(0)).merge(product);
        assertEquals(expectedProduct, result);
    }

    private static Stream<Arguments> provideProductsForSaveTest() {
        Product product1 = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .build();
        product1.setId("prod-123");

        Product product2 = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .build();
        product2.setId(null);

        Product product3 = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .build();
        product3.setId("prod-123");

        return Stream.of(
                Arguments.of(product1, null, product1),
                Arguments.of(product2, null, product2),
                Arguments.of(product3, null, product3));
    }
}