package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.service.CategoryService;
import id.ac.ui.cs.advprog.heymartbeproduct.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "CreateProduct";
        }

        Set<Category> categories = product.getCategoryNames().stream()
                .map(name -> categoryService.findByName(name))
                .collect(Collectors.toSet());

        product.setCategories(categories);

        productService.create(product);

        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "ListProducts";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable String id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "EditProduct";
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute Product product, @RequestParam List<String> categoryNames,
            BindingResult result) {
        if (result.hasErrors()) {
            return "EditProduct";
        }

        Set<Category> categories = categoryNames.stream()
                .map(categoryService::findByName)
                .collect(Collectors.toSet());
        product.setCategories(categories);
        productService.edit(product);
        product.getCategories()
                .forEach(category -> categoryService.removeProductFromCategory(category.getName(), product));
        categoryNames.forEach(categoryName -> categoryService.addProductToCategory(categoryName, product));
        return "redirect:/product/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id) {
        Product product = productService.findById(id);
        product.getCategories()
                .forEach(category -> categoryService.removeProductFromCategory(category.getName(), product));
        productService.deleteById(id);
        return "redirect:/product/list";
    }

    @GetMapping("/createCategory")
    public String createCategoryPage(Model model) {
        model.addAttribute("category", new Category());
        return "CreateCategory";
    }

    @PostMapping("/createCategory")
    public String createCategory(@ModelAttribute Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "CreateCategory";
        }

        categoryService.create(category);

        return "redirect:/product/create";
    }
}