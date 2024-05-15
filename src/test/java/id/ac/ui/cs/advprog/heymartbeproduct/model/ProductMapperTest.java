package id.ac.ui.cs.advprog.heymartbeproduct.model;

import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ProductMapperTest {

    @InjectMocks
    private ProductMapper productMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConvertToDto() {
        Product product = new Product.ProductBuilder("Product 1", 100.0, 10)
                .setDescription("Description 1")
                .setImage("Image 1")
                .build();
        Set<Category> categories = new HashSet<>();
        categories.add(new Category.CategoryBuilder("Category 1").build());
        product.setCategories(categories);

        ProductDto productDto = productMapper.convertToDto(product);

        assertEquals(product.getId(), productDto.getId());
        assertEquals(product.getName(), productDto.getName());
        assertEquals(product.getPrice(), productDto.getPrice());
        assertEquals(product.getDescription(), productDto.getDescription());
        assertEquals(product.getQuantity(), productDto.getQuantity());
        assertEquals(product.getImage(), productDto.getImage());
        assertEquals(product.getCategories().size(), productDto.getCategoryNames().size());
    }

    @Test
    void testConvertToEntity() {
        ProductDto productDto = new ProductDto();
        productDto.setId("1");
        productDto.setName("Product 1");
        productDto.setPrice(100.0);
        productDto.setDescription("Description 1");
        productDto.setQuantity(10);
        productDto.setImage("Image 1");
        Set<String> categoryNames = new HashSet<>();
        categoryNames.add("Category 1");
        productDto.setCategoryNames(categoryNames);

        Category category = new Category.CategoryBuilder("Category 1").build();
        when(categoryRepository.findCategoryByName("Category 1")).thenReturn(Optional.of(category));

        Product product = productMapper.convertToEntity(productDto);

        assertEquals(productDto.getName(), product.getName());
        assertEquals(productDto.getPrice(), product.getPrice());
        assertEquals(productDto.getDescription(), product.getDescription());
        assertEquals(productDto.getQuantity(), product.getQuantity());
        assertEquals(productDto.getImage(), product.getImage());
        assertEquals(productDto.getCategoryNames().size(), product.getCategories().size());
    }
}