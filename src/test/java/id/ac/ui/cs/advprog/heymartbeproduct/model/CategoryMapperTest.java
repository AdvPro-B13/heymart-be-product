package id.ac.ui.cs.advprog.heymartbeproduct.model;

import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.CategoryMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CategoryMapperTest {

    @Mock
    private ProductRepository productRepository;

    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryMapper = new CategoryMapper(productRepository);
    }

    @Test
    void testConvertToDto() {
        Category category = new Category.CategoryBuilder("Category 1").build();
        category.setId(1L);
        Set<Product> products = new HashSet<>();
        products.add(new Product.ProductBuilder("Product 1", 100.0, 10).build());
        category.setProducts(products);

        CategoryDto categoryDto = categoryMapper.convertToDto(category);

        assertEquals(category.getId(), categoryDto.getId());
        assertEquals(category.getName(), categoryDto.getName());
        assertEquals(category.getProducts().size(), categoryDto.getProductIds().size());
    }

    @Test
    void testConvertToEntity() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Category 1");
        Set<String> productIds = new HashSet<>();
        productIds.add("1");
        categoryDto.setProductIds(productIds);

        Product product = new Product.ProductBuilder("Product 1", 100.0, 10).build();
        when(productRepository.findProductById("1")).thenReturn(Optional.of(product));

        Category category = categoryMapper.convertToEntity(categoryDto);

        assertEquals(categoryDto.getId(), category.getId());
        assertEquals(categoryDto.getName(), category.getName());
        assertEquals(categoryDto.getProductIds().size(), category.getProducts().size());
    }
}