package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;

import java.util.List;

public interface CategoryService {
    Category create(Category category);

    Category findByName(String name);

    Category edit(Category category);

    void deleteByName(String name);

    List<Category> getAllCategories();

    void addProductToCategory(String categoryName, Product product);

    void removeProductFromCategory(String categoryName, Product product);
}