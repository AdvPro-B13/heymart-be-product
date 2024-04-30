package id.ac.ui.cs.advprog.heymartbeproduct.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Builder.CategoryBuilder;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Builder.ProductBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        this.category = new CategoryBuilder("Category1")
                .setProducts(new HashSet<>())
                .build();
        this.product = new ProductBuilder("Product1", 4.99, 10)
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
    }

    @Test
    void testRemoveCategory() {
        product.addCategory(category);
        product.removeCategory(category);
        assertFalse(product.getCategories().contains(category));
        assertFalse(category.getProducts().contains(product));
    }

    @Test
    void testRemoveCategoryNotInProduct() {
        Category category2 = new CategoryBuilder("Category2")
                .setProducts(new HashSet<>())
                .build();
        product.addCategory(category);
        product.removeCategory(category2);
        assertTrue(product.getCategories().contains(category));
    }

    @Test
    void testSetNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> new ProductBuilder("Product2", -1.0, 1).build());
    }

    @Test
    void testSetNegativeQuantity() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductBuilder("Product2", 1.0, -1).build());
    }
}