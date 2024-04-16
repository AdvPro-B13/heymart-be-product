package id.ac.ui.cs.advprog.heymartbeproduct.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

class ProductTest {
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        this.product = new Product();
        this.category = new Category("Category1");
        this.product.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        this.product.setName("Product1");
        this.product.setPrice(4.99);
        this.product.setDescription("This is Product1");
        this.product.setQuantity(10);
        this.product.setImage("image.jpg");
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
        Category category2 = new Category("Category2");
        product.addCategory(category);
        product.removeCategory(category2);
        assertTrue(product.getCategories().contains(category));
    }

    @Test
    void testSetNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> product.setPrice(-1.0));
    }

    @Test
    void testSetNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> product.setQuantity(-1));
    }
}