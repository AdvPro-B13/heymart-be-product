package id.ac.ui.cs.advprog.heymartbeproduct.Dto;

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

    public ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setQuantity(product.getQuantity());
        productDto.setImage(product.getImage());
        productDto.setCategoryNames(product.getCategories().stream()
                .map(category -> category.getName())
                .collect(Collectors.toSet()));
        return productDto;
    }

    public Product convertToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());
        product.setImage(productDto.getImage());
        Set<Category> categories = productDto.getCategoryNames().stream()
                .map(categoryRepository::findCategoryByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        product.setCategories(categories);
        return product;
    }
}