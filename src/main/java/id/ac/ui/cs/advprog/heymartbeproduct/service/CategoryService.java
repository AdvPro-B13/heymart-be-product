package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.Dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.Dto.ProductDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto categoryDto);

    CategoryDto findByName(String name);

    CategoryDto edit(String name, CategoryDto categoryDto);

    void deleteByName(String name);

    List<CategoryDto> getAllCategories();

    void addProductToCategory(String categoryName, ProductDto productDto);

    void removeProductFromCategory(String categoryName, ProductDto productDto);
}