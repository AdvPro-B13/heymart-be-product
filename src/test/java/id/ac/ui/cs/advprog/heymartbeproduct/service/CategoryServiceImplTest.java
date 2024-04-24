package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.CategoryRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    Category category;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        category = new Category.CategoryBuilder("Electronics").build();
    }

    @Test
    public void testCreate() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.saveCategory(any(Category.class))).thenReturn(category);

        Category result = categoryService.create(category);

        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).saveCategory(category);
    }

    @Test
    public void testFindByName() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));

        Category result = categoryService.findByName("Electronics");

        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).findCategoryByName("Electronics");
    }

    @Test
    public void testEdit() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));
        when(categoryRepository.saveCategory(any(Category.class))).thenReturn(category);

        Category result = categoryService.edit(category);

        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).saveCategory(category);
    }

    @Test
    public void testDeleteByName() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));

        categoryService.deleteByName("Electronics");

        verify(categoryRepository, times(1)).deleteCategoryByName("Electronics");
    }

    @Test
    public void testGetAllCategories() {
        Category category2 = new Category.CategoryBuilder("Books").build();
        List<Category> categories = Arrays.asList(category, category2);
        when(categoryRepository.getAllCategories()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).getAllCategories();
    }

    @Test
    public void testAddProductToCategory() {
        Product product = new Product.ProductBuilder("TV", 100.0, 10).build();
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));

        categoryService.addProductToCategory("Electronics", product);

        verify(categoryRepository, times(1)).addProductToCategory("Electronics", product);
    }

    @Test
    public void testCreateWithNullCategory() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.create(null));
    }

    @Test
    public void testCreateWithExistingCategory() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));
        assertThrows(IllegalArgumentException.class, () -> categoryService.create(category));
    }

    @Test
    public void testFindByNameWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.findByName(null));
    }

    @Test
    public void testFindByNameWithNonExistingCategory() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.findByName("Electronics"));
    }

    @Test
    public void testEditWithNonExistingCategory() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.edit(category));
    }

    @Test
    public void testDeleteByNameWithNonExistingCategory() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteByName("Electronics"));
    }

    @Test
    public void testAddProductToCategoryWithEmptyCategoryName() {
        Product product = new Product.ProductBuilder("TV", 100.0, 10).build();
        assertThrows(IllegalArgumentException.class, () -> categoryService.addProductToCategory("", product));
    }

    @Test
    public void testAddProductToCategoryWithNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.addProductToCategory("Electronics", null));
    }

    @Test
    public void testRemoveProductFromCategoryWithEmptyCategoryName() {
        Product product = new Product.ProductBuilder("TV", 100.0, 10).build();
        assertThrows(IllegalArgumentException.class, () -> categoryService.removeProductFromCategory("", product));
    }

    @Test
    public void testRemoveProductFromCategoryWithNullProduct() {
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.removeProductFromCategory("Electronics", null));
    }
}