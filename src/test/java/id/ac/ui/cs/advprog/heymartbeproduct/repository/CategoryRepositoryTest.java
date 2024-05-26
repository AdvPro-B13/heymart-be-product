package id.ac.ui.cs.advprog.heymartbeproduct.repository;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CategoryRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Category> query;

    @InjectMocks
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        category = new Category.CategoryBuilder("Electronics").build();
    }

    @Test
    void testFindCategoryById() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(category);

        Optional<Category> result = categoryRepository.findCategoryById(1L);

        verify(entityManager, times(1)).createQuery(anyString(), eq(Category.class));
        assertTrue(result.isPresent());
        assertEquals("Electronics", result.get().getName());
    }

    @Test
    void testDeleteCategoryById() {
        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);

        categoryRepository.deleteCategoryById(1L);

        verify(entityManager, times(1)).createQuery(anyString());
        verify(query, times(1)).setParameter(anyString(), anyLong());
        verify(query, times(1)).executeUpdate();
    }

    @Test
    void testFindCategoryByName() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category));

        Optional<Category> result = categoryRepository.findCategoryByName("Electronics");

        verify(entityManager, times(1)).createQuery(anyString(), eq(Category.class));
        assertTrue(result.isPresent());
        assertEquals("Electronics", result.get().getName());
    }

    @Test
    void testSaveCategory() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        Category result = categoryRepository.saveCategory(category);

        verify(entityManager, times(1)).persist(category);
        assertEquals("Electronics", result.getName());
    }

    @Test
    void testDeleteCategoryByName() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category));

        categoryRepository.deleteCategoryByName("Electronics");

        verify(entityManager, times(1)).remove(category);
    }

    @Test
    void testGetAllCategories() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category));

        List<Category> result = categoryRepository.getAllCategories();

        verify(entityManager, times(1)).createQuery(anyString(), eq(Category.class));
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }

    @Test
    void testAddProductToCategory() {
        Product product = new Product.ProductBuilder("Product1", 100.0, 10).build();

        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category));

        category.addProduct(product);
        categoryRepository.addProductToCategory("Electronics", product);

        verify(entityManager, times(1)).merge(category);
        assertTrue(category.getProducts().contains(product));
    }

    @Test
    void testRemoveProductFromCategory() {
        Product product = new Product.ProductBuilder("Product1", 100.0, 10).build();
        Category category3 = new Category.CategoryBuilder("Drinks")
                .setProducts(new HashSet<>(Arrays.asList(product)))
                .build();

        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category3));

        category3.removeProduct(product);
        categoryRepository.removeProductFromCategory("Drinks", product);

        verify(entityManager, times(1)).merge(category3);
        assertFalse(category3.getProducts().contains(product));
    }

    @Test
    void testFindCategoryByNameNotFound() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        Optional<Category> result = categoryRepository.findCategoryByName("NonExistent");

        verify(entityManager, times(1)).createQuery(anyString(), eq(Category.class));
        assertFalse(result.isPresent());
    }

    @Test
    void testSaveCategoryAlreadyExists() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category));
        when(entityManager.merge(category)).thenReturn(category);

        Category result = categoryRepository.saveCategory(category);

        verify(entityManager, times(1)).merge(category);
        assertEquals("Electronics", result.getName());
    }

    @Test
    void testDeleteCategoryByNameNotFound() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> {
            categoryRepository.deleteCategoryByName("NonExistent");
        });
    }

    @Test
    void testAddProductToCategoryNotFound() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());
        Product product = new Product.ProductBuilder("Product1", 100.0, 10).build();

        assertThrows(IllegalArgumentException.class, () -> {
            categoryRepository.addProductToCategory("NonExistent", product);
        });
    }

    @Test
    void testRemoveProductFromCategoryNotFound() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());
        Product product = new Product.ProductBuilder("Product1", 100.0, 10).build();

        assertThrows(IllegalArgumentException.class, () -> {
            categoryRepository.removeProductFromCategory("NonExistent", product);
        });
    }

    @Test
    void testFindCategoryByIdWhenCategoryExists() {
        Long id = 1L;
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(category);

        Optional<Category> result = categoryRepository.findCategoryById(id);

        verify(entityManager, times(1)).createQuery(anyString(), eq(Category.class));
        verify(query, times(1)).setParameter(anyString(), any());
        assertTrue(result.isPresent());
        assertEquals(category, result.get());
    }

    @Test
    void testFindCategoryByIdWhenCategoryDoesNotExist() {
        Long id = 1L;
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);

        Optional<Category> result = categoryRepository.findCategoryById(id);

        verify(entityManager, times(1)).createQuery(anyString(), eq(Category.class));
        verify(query, times(1)).setParameter(anyString(), any());
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteCategoryByNameWhenCategoryHasProducts() {
        String name = "Electronics";
        Product product1 = new Product.ProductBuilder("Product1", 100.0, 10).build();
        Product product2 = new Product.ProductBuilder("Product2", 200.0, 20).build();
        Set<Product> products = new HashSet<>(Arrays.asList(product1, product2));
        Category category1 = new Category.CategoryBuilder(name).setProducts(products).build();

        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category1));

        categoryRepository.deleteCategoryByName(name);

        verify(entityManager, times(1)).createQuery(anyString(), eq(Category.class));
        verify(query, times(1)).setParameter(anyString(), anyString());
        verify(entityManager, times(2)).merge(any(Product.class)); // Verify that merge is called for each product
        verify(entityManager, times(1)).remove(category1);
    }

    @Test
    void testDeleteCategoryByNameWhenCategoryHasNoProducts() {
        String name = "Electronics";
        Category category1 = new Category.CategoryBuilder(name).build();

        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category1));

        categoryRepository.deleteCategoryByName(name);

        verify(entityManager, times(1)).createQuery(anyString(), eq(Category.class));
        verify(query, times(1)).setParameter(anyString(), anyString());
        verify(entityManager, times(0)).merge(any(Product.class)); // Verify that merge is not called
        verify(entityManager, times(1)).remove(category1);
    }

    @Test
    void testSaveCategoryWhenCategoryDoesNotExist() {
        Category category1 = new Category.CategoryBuilder("Electronics").build();
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        Category result = categoryRepository.saveCategory(category1);

        verify(entityManager, times(1)).persist(category1);
        assertEquals("Electronics", result.getName());
    }

    @Test
    void testSaveCategoryWhenCategoryNameIsNull() {
        Category category1 = new Category.CategoryBuilder(null).build();

        Category result = categoryRepository.saveCategory(category1);

        verify(entityManager, times(1)).persist(category1);
        assertNull(result.getName());
    }
}