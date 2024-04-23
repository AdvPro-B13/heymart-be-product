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

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
        when(entityManager.find(Category.class, "Electronics")).thenReturn(category);

        Optional<Category> result = categoryRepository.findCategoryByName("Electronics");

        verify(entityManager, times(1)).find(Category.class, "Electronics");
        assert (result.isPresent());
        assert (result.get().getName().equals("Electronics"));
    }

    @Test
    public void testSaveCategory() {
        doNothing().when(entityManager).persist(category);

        Category result = categoryRepository.saveCategory(category);

        verify(entityManager, times(1)).persist(category);
        assert (result.getName().equals("Electronics"));
    }

    @Test
    public void testDeleteCategoryByName() {
        when(entityManager.find(Category.class, "Electronics")).thenReturn(category);

        categoryRepository.deleteCategoryByName("Electronics");

        verify(entityManager, times(1)).remove(category);
    }

    @Test
    public void testGetAllCategories() {
        Category category2 = new Category.CategoryBuilder("Books").build();

        List<Category> categories = Arrays.asList(category, category2);

        when(entityManager.createQuery("SELECT c FROM Category c", Category.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(categories);

        List<Category> result = categoryRepository.getAllCategories();

        verify(query, times(1)).getResultList();
        assert (result.size() == 2);
    }

    @Test
    public void testAddProductToCategory() {
        Product product = new Product.ProductBuilder("Product1", 100.0, 10).build();

        when(entityManager.find(Category.class, "Electronics")).thenReturn(category);

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

        when(entityManager.find(Category.class, "Drinks")).thenReturn(category3);

        category3.removeProduct(product);
        categoryRepository.removeProductFromCategory("Drinks", product);

        verify(entityManager, times(1)).merge(category3);
        assert (!category3.getProducts().contains(product));
    }
}