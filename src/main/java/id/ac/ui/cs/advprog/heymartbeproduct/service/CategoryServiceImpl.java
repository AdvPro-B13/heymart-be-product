package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (categoryRepository.findCategoryByName(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Category with this name already exists");
        }
        return categoryRepository.saveCategory(category);
    }

    @Override
    public Category findByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return categoryRepository.findCategoryByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Category isn't found"));
    }

    @Override
    public Category edit(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (!categoryRepository.findCategoryByName(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Category not found");
        }
        return categoryRepository.saveCategory(category);
    }

    @Override
    public void deleteByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (!categoryRepository.findCategoryByName(name).isPresent()) {
            throw new IllegalArgumentException("Category not found");
        }
        categoryRepository.deleteCategoryByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    @Override
    public void addProductToCategory(String categoryName, Product product) {
        if (categoryName == null || categoryName.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        categoryRepository.addProductToCategory(categoryName, product);
    }

    @Override
    public void removeProductFromCategory(String categoryName, Product product) {
        if (categoryName == null || categoryName.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        categoryRepository.removeProductFromCategory(categoryName, product);
    }
}