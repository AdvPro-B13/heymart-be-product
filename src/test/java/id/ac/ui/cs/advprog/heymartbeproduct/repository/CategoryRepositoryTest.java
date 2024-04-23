package id.ac.ui.cs.advprog.heymartbeproduct.repository;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryRepositoryTest {
    private CategoryRepository categoryRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Category> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryRepository = CategoryRepository.getInstance(entityManager);
    }

    @Test
    void testFindCategoryByName() {
        Category category = new Category.CategoryBuilder("Electronics").build();
        when(entityManager.find(Category.class, "Electronics")).thenReturn(category);

        Optional<Category> result = categoryRepository.findCategoryByName("Electronics");

        assertTrue(result.isPresent());
        assertEquals(category, result.get());
    }

    @Test
    void testSaveCategory() {
        Category category = new Category.CategoryBuilder("Electronics").build();
        when(entityManager.merge(category)).thenReturn(category);

        Category result = categoryRepository.saveCategory(category);

        assertEquals(category, result);
        verify(entityManager).persist(category);
    }

    @Test
    void testDeleteCategoryByName() {
        Category category = new Category.CategoryBuilder("Electronics").build();
        when(entityManager.find(Category.class, "Electronics")).thenReturn(category);

        categoryRepository.deleteCategoryByName("Electronics");

        verify(entityManager).remove(category);
    }

    @Test
    void testGetAllCategories() {
        List<Category> categories = Arrays.asList(
                new Category.CategoryBuilder("Electronics").build(),
                new Category.CategoryBuilder("Books").build());
        when(entityManager.createQuery("SELECT c FROM Category c", Category.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(categories);

        List<Category> result = categoryRepository.getAllCategories();

        assertEquals(categories, result);
    }
}