package id.ac.ui.cs.advprog.heymartbeproduct.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class ProductTest {
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        this.category = new Category.CategoryBuilder("Category1")
                .setProducts(new HashSet<>())
                .build();
        this.product = new Product.ProductBuilder("Product1", 4.99, 10)
                .setDescription("This is Product1")
                .setImage("image.jpg")
                .setCategoryNames(new HashSet<>(Arrays.asList("Category1", "Category2")))
                .build();
        this.product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
    }

    @Test
    void testGetId() {
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", product.getId());
    }

    @Test
    void testGetName() {
        assertEquals("Product1", product.getName());
    }

    @Test
    void testPrice() {
        assertEquals(4.99, product.getPrice());
    }

    @Test
    void testDescription() {
        assertEquals("This is Product1", product.getDescription());
    }

    @Test
    void testQuantity() {
        assertEquals(10, product.getQuantity());
    }

    @Test
    void testImage() {
        assertEquals("image.jpg", product.getImage());
    }

    @Test
    void testCategories() {
        Set<Category> categories = new HashSet<>();
        product.setCategories(categories);
        assertEquals(categories, product.getCategories());
    }

    @Test
    void testAddCategory() {
        product.addCategory(category);
        assertTrue(product.getCategories().contains(category));
        assertTrue(category.getProducts().contains(product));
        assertTrue(product.getCategoryNames().contains(category.getName()));
    }

    @Test
    void testRemoveCategory() {
        product.addCategory(category);
        product.removeCategory(category);
        assertFalse(product.getCategories().contains(category));
        assertFalse(category.getProducts().contains(product));
        assertFalse(product.getCategoryNames().contains(category.getName()));
    }

    @Test
    void testRemoveCategoryNotInProduct() {
        Category category2 = new Category.CategoryBuilder("Category2")
                .setProducts(new HashSet<>())
                .build();
        product.addCategory(category);
        product.removeCategory(category2);
        assertTrue(product.getCategories().contains(category));
    }

    @Test
    void testSetNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> new Product.ProductBuilder("Product2", -1.0, 1).build());
    }

    @Test
    void testSetNegativeQuantity() {
        assertThrows(IllegalArgumentException.class,
                () -> new Product.ProductBuilder("Product2", 1.0, -1).build());
    }

    @Test
    void testUpdateCategory() {
        Product product1 = new Product();
        Category oldCategory = new Category();
        oldCategory.setName("OldCategory");
        Category newCategory = new Category();
        newCategory.setName("NewCategory");

        product1.addCategory(oldCategory);
        product1.updateCategory(oldCategory, newCategory);

        assertTrue(product1.getCategories().contains(newCategory));
        assertFalse(product1.getCategories().contains(oldCategory));
        assertTrue(product1.getCategoryNames().contains(newCategory.getName()));
        assertFalse(product1.getCategoryNames().contains(oldCategory.getName()));
    }

    @Test
    void testAddCategory2() {
        Product product1 = new Product();
        Category category = new Category();
        category.setName("NewCategory");

        product1.addCategory(category);

        assertTrue(product1.getCategories().contains(category));
    }

    @Test
    void testSetPrice() {
        Product product = new Product();

        assertThrows(IllegalArgumentException.class, () -> {
            product.setPrice(-1);
        });

        product.setPrice(100);

        assertEquals(100, product.getPrice());
    }

    @Test
    void testSetQuantity() {
        Product product1 = new Product();

        assertThrows(IllegalArgumentException.class, () -> {
            product1.setQuantity(-1);
        });

        product1.setQuantity(10);

        assertEquals(10, product1.getQuantity());
    }

    @Test
    void testRemoveCategory2() {
        Product product1 = new Product();
        Category category1 = new Category();
        category1.setName("NewCategory");

        product1.addCategory(category1);
        product1.removeCategory(category1);

        assertFalse(product1.getCategories().contains(category1));
    }

    @Test
    void testProductBuilderDefaultConstructor() {
        Product.ProductBuilder productBuilder = new Product.ProductBuilder();

        assertNotNull(productBuilder);
    }

    @Test
    void testProductBuilderSetCategories() {
        Set<Category> categories = new HashSet<>();
        Category category1 = new Category();
        category1.setName("Category1");
        categories.add(category1);

        Product.ProductBuilder productBuilder = new Product.ProductBuilder();
        productBuilder.setCategories(categories);

        Product product = productBuilder.build();

        assertTrue(product.getCategories().contains(category1));
    }

    @Test
    void testAddCategoryNull() {
        Product product1 = new Product();

        assertThrows(NullPointerException.class, () -> {
            product1.addCategory(null);
        });
    }

    @Test
    void testRemoveCategoryNull() {
        Product product1 = new Product();

        assertThrows(NullPointerException.class, () -> {
            product1.removeCategory(null);
        });
    }

    @Test
    void testUpdateCategoryNull() {
        Product product1 = new Product();

        assertThrows(NullPointerException.class, () -> {
            product1.updateCategory(null, null);
        });
    }

    @Test
    void testUpdateCategoryWhenCategoriesIsNull() {
        Product product1 = new Product();
        Category oldCategory = new Category();
        oldCategory.setName("OldCategory");
        Category newCategory = new Category();
        newCategory.setName("NewCategory");

        assertDoesNotThrow(() -> product1.updateCategory(oldCategory, newCategory));
    }

    @Test
    void testUpdateCategoryWhenOldCategoryNotPresent() {
        Product product1 = new Product();
        Category oldCategory = new Category();
        oldCategory.setName("OldCategory");
        Category newCategory = new Category();
        newCategory.setName("NewCategory");

        Category differentCategory = new Category();
        differentCategory.setName("DifferentCategory");
        product1.addCategory(differentCategory);

        assertDoesNotThrow(() -> product1.updateCategory(oldCategory, newCategory));
    }

    @Test
    void testUpdateCategoryWhenOldCategoryIsNull() {
        Product product1 = new Product();
        Category newCategory = new Category();
        newCategory.setName("NewCategory");

        assertThrows(NullPointerException.class, () -> product1.updateCategory(null, newCategory));
    }

    @Test
    void testUpdateCategoryWhenNewCategoryIsNull() {
        Product product1 = new Product();
        Category oldCategory = new Category();
        oldCategory.setName("OldCategory");

        assertThrows(NullPointerException.class, () -> product1.updateCategory(oldCategory, null));
    }

    @Test
    void testUpdateCategoryNotContained() {
        Product product1 = new Product();
        Category oldCategory = new Category();
        oldCategory.setName("OldCategory");
        Category newCategory = new Category();
        newCategory.setName("NewCategory");

        product1.updateCategory(oldCategory, newCategory);

        assertFalse(product.getCategories().contains(newCategory));
    }

    @Test
    void testGetCategoryNames() {
        Product product1 = new Product();
        Category category1 = new Category();
        category1.setName("Category1");
        product1.addCategory(category1);

        Set<String> categoryNames = product1.getCategoryNames();

        assertTrue(categoryNames.contains("Category1"));
    }
}