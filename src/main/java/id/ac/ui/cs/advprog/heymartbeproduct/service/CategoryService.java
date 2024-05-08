package id.ac.ui.cs.advprog.heymartbeproduct.service;

import java.util.List;

import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductDto;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto findById(Long id);

    CategoryDto findByName(String name);

    CategoryDto edit(Long id, CategoryDto categoryDto);

    void deleteById(Long id);

    List<CategoryDto> getAllCategories();

    void addProductToCategory(String name, ProductDto productDto);

    void removeProductFromCategory(String name, ProductDto productDto);
}