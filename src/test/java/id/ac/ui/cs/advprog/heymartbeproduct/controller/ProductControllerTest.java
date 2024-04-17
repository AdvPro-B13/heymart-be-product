package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProductPage() {
        when(model.addAttribute(eq("product"), any(Product.class))).thenReturn(model);

        String view = productController.createProductPage(model);

        assertEquals("CreateProduct", view);
        verify(model, times(1)).addAttribute(eq("product"), any(Product.class));
    }

    @Test
    void testCreateProductPost() {
        Product product = new Product();
        when(productService.create(any(Product.class))).thenReturn(product);

        String view = productController.createProductPost(product, model);

        assertEquals("redirect:/product/list", view);
        verify(productService, times(1)).create(any(Product.class));
    }

    @Test
    void testProductListPage() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productService.getAllProducts()).thenReturn(products);
        when(model.addAttribute("products", products)).thenReturn(model);

        String view = productController.productListPage(model);

        assertEquals("ListProduct", view);
        verify(productService, times(1)).getAllProducts();
        verify(model, times(1)).addAttribute("products", products);
    }

    @Test
    void testDeleteProductPost() {
        doNothing().when(productService).deleteById(anyString());

        String view = productController.deleteProductPost("1");

        assertEquals("redirect:/product/list", view);
        verify(productService, times(1)).deleteById(anyString());
    }

    @Test
    void testEditProductPage() {
        Product product = new Product();
        when(productService.findById(anyString())).thenReturn(product);
        when(model.addAttribute("product", product)).thenReturn(model);

        String view = productController.editProductPage("1", model);

        assertEquals("EditProduct", view);
        verify(productService, times(1)).findById(anyString());
        verify(model, times(1)).addAttribute("product", product);
    }

    @Test
    void testEditProductPost() {
        Product product = new Product();
        when(productService.edit(any(Product.class))).thenReturn(product);

        String view = productController.editProductPost(product, model);

        assertEquals("redirect:/product/list", view);
        verify(productService, times(1)).edit(any(Product.class));
    }
}
