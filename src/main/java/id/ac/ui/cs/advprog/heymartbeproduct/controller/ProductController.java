package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        model.addAttribute("product", new Product.ProductBuilder());
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product.ProductBuilder productBuilder) {
        productService.create(productBuilder.build());
        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "listProducts";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable String id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute Product product) {
        productService.edit(product);
        return "redirect:/product/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        productService.deleteById(id);
        return "redirect:/product/list";
    }
}