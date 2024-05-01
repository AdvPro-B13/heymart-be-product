package id.ac.ui.cs.advprog.heymartbeproduct.Dto;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

@Component
public class CategoryMapper {

    private final ProductRepository productRepository;

    @Autowired
    public CategoryMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public CategoryDto convertToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(category.getName());
        categoryDto.setProductIds(category.getProducts().stream()
                .map(product -> product.getId())
                .collect(Collectors.toSet()));
        return categoryDto;
    }

    public Category convertToEntity(CategoryDto categoryDto) {
        Category category = new Category.CategoryBuilder(categoryDto.getName()).build();
        Set<Product> products = categoryDto.getProductIds().stream()
                .map(productRepository::findProductById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        products.forEach(category::addProduct);
        return category;
    }
}