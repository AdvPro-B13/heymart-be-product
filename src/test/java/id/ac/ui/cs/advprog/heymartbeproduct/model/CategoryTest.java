package id.ac.ui.cs.advprog.heymartbeproduct.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {
    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category("Electronics");
    }

    @Test
    void testGetName() {
        assertEquals("Electronics", category.getName());
    }

    @Test
    void testAddProduct() {
        Product product = new Product();
        category.getProducts().add(product);
        assertTrue(category.getProducts().contains(product));
    }

    @Test
    void testAddNullProduct() {
        assertThrows(NullPointerException.class, () -> category.addProduct(null));
    }

    @Test
    void testRemoveProductNotInCategory() {
        Product product = new Product();
        Product product2 = new Product();
        category.getProducts().add(product);
        category.getProducts().remove(product2);
        assertTrue(category.getProducts().contains(product));
    }
}