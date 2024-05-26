package id.ac.ui.cs.advprog.heymartbeproduct.dto;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

@Component
public class ProductMapper {

    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ProductResponseDto convertToDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setQuantity(product.getQuantity());
        productResponseDto.setImage(product.getImage());
        productResponseDto.setSupermarketId(product.getSupermarketId());
        productResponseDto.setCategoryNames(product.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toSet()));
        return productResponseDto;
    }

    public Product convertToEntity(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());
        product.setDescription(productRequestDto.getDescription());
        product.setQuantity(productRequestDto.getQuantity());
        product.setImage(productRequestDto.getImage());
        product.setSupermarketId(productRequestDto.getSupermarketId());
        Set<Category> categories = productRequestDto.getCategoryNames().stream()
                .map(categoryRepository::findCategoryByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        product.setCategories(categories);
        return product;
    }
}