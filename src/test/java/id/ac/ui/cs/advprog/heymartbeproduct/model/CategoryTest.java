package id.ac.ui.cs.advprog.heymartbeproduct.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category.CategoryBuilder("Electronics").build();
    }

    @Test
    void testGetName() {
        assertEquals("Electronics", category.getName());
    }

    @Test
    void testAddProduct() {
        Product product = new Product.ProductBuilder("Product1", 4.99, 10).build();
        category.addProduct(product);
        assertTrue(category.getProducts().contains(product));
    }

    @Test
    void testAddNullProduct() {
        assertThrows(NullPointerException.class, () -> category.addProduct(null));
    }

    @Test
    void testRemoveProductNotInCategory() {
        Product product = new Product.ProductBuilder("Product1", 4.99, 10).build();
        Product product2 = new Product.ProductBuilder("Product2", 9.99, 2).build();
        category.addProduct(product);
        category.removeProduct(product2);
        assertTrue(category.getProducts().contains(product));
    }
}