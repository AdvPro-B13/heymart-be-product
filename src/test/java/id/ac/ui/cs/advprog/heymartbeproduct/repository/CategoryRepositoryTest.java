package id.ac.ui.cs.advprog.heymartbeproduct.repository;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.ArrayList;

public class CategoryRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Category> query;

    @InjectMocks
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        category = new Category.CategoryBuilder("Electronics").build();
    }

    @Test
    public void testFindCategoryByName() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category));

        Optional<Category> result = categoryRepository.findCategoryByName("Electronics");

        verify(entityManager, times(1)).createQuery(anyString(), eq(Category.class));
        assert (result.isPresent());
        assert (result.get().getName().equals("Electronics"));
    }

    @Test
    public void testSaveCategory() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        Category result = categoryRepository.saveCategory(category);

        verify(entityManager, times(1)).persist(category);
        assertEquals("Electronics", result.getName());
    }

    @Test
    public void testDeleteCategoryByName() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category));

        categoryRepository.deleteCategoryByName("Electronics");

        verify(entityManager, times(1)).remove(category);
    }

    @Test
    public void testAddProductToCategory() {
        Product product = new Product.ProductBuilder("Product1", 100.0, 10).build();

        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category));

        category.addProduct(product);
        categoryRepository.addProductToCategory("Electronics", product);

        verify(entityManager, times(1)).merge(category);
        assert (category.getProducts().contains(product));
    }

    @Test
    public void testRemoveProductFromCategory() {
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
        assert (!category3.getProducts().contains(product));
    }

    @Test
    public void testFindCategoryByNameNotFound() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        Optional<Category> result = categoryRepository.findCategoryByName("NonExistent");

        verify(entityManager, times(1)).createQuery(anyString(), eq(Category.class));
        assertFalse(result.isPresent());
    }

    @Test
    public void testSaveCategoryAlreadyExists() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(category));
        when(entityManager.merge(category)).thenReturn(category);

        Category result = categoryRepository.saveCategory(category);

        verify(entityManager, times(1)).merge(category);
        assertEquals("Electronics", result.getName());
    }

    @Test
    public void testDeleteCategoryByNameNotFound() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> {
            categoryRepository.deleteCategoryByName("NonExistent");
        });
    }

    @Test
    public void testAddProductToCategoryNotFound() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());
        Product product = new Product.ProductBuilder("Product1", 100.0, 10).build();

        assertThrows(IllegalArgumentException.class, () -> {
            categoryRepository.addProductToCategory("NonExistent", product);
        });
    }

    @Test
    public void testRemoveProductFromCategoryNotFound() {
        when(entityManager.createQuery(anyString(), eq(Category.class))).thenReturn(query);
        when(query.setParameter(anyString(), anyString())).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());
        Product product = new Product.ProductBuilder("Product1", 100.0, 10).build();

        assertThrows(IllegalArgumentException.class, () -> {
            categoryRepository.removeProductFromCategory("NonExistent", product);
        });
    }
}