package id.ac.ui.cs.advprog.heymartbeproduct.service;

import java.util.List;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.CategoryDto;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto findById(Long id);

    CategoryDto findByName(String name);

    CategoryDto edit(Long id, CategoryDto categoryDto);

    void deleteById(Long id);

    List<CategoryDto> getAllCategories();

    void addProductToCategory(String categoryName, String productId);

    void removeProductFromCategory(String categoryName, String productId);
}