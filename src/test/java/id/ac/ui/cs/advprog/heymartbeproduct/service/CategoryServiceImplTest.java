package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.CategoryMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductResponseDto;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.CategoryRepository;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    Category category;
    CategoryDto categoryDto;

    Product product1;
    Product product2;
    Product product3;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        category = new Category.CategoryBuilder("Electronics").build();
        categoryDto = new CategoryDto();
        categoryDto.setName("Electronics");

        product1 = new Product.ProductBuilder("TV", 100.0, 10).build();
        product1.setId("1");

        product2 = new Product.ProductBuilder("Radio", 50.0, 5).build();
        product2.setId("2");

        product3 = new Product.ProductBuilder("Computer", 200.0, 20).build();
        product3.setId("3");

        category.addProduct(product1);
        category.addProduct(product2);
    }

    @Test
    void testCreate() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.saveCategory(any(Category.class))).thenReturn(category);
        when(categoryMapper.convertToEntity(any(CategoryDto.class))).thenReturn(category);
        when(categoryMapper.convertToDto(any(Category.class))).thenReturn(categoryDto);

        CategoryDto result = categoryService.create(categoryDto);

        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).saveCategory(category);
    }

    @Test
    void testFindByName() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));
        when(categoryMapper.convertToDto(any(Category.class))).thenReturn(categoryDto);

        CategoryDto result = categoryService.findByName("Electronics");

        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).findCategoryByName("Electronics");
    }

    @Test
    void testGetAllCategories() {
        Category category2 = new Category.CategoryBuilder("Books").build();
        List<Category> categories = Arrays.asList(category, category2);
        when(categoryRepository.getAllCategories()).thenReturn(categories);

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setName("Books");
        List<CategoryDto> categoryDtos = Arrays.asList(categoryDto, categoryDto2);
        when(categoryMapper.convertToDto(category)).thenReturn(categoryDto);
        when(categoryMapper.convertToDto(category2)).thenReturn(categoryDto2);

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(categoryDtos.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(categoryDtos.get(i).getName(), result.get(i).getName());
        }
        verify(categoryRepository, times(1)).getAllCategories();
    }

    @Test
    void testAddProductToCategory() {
        // Arrange
        String productName = "TV";
        double productPrice = 100.0;
        int productQuantity = 10;
        String categoryName = "Electronics";
        Product product = new Product.ProductBuilder(productName, productPrice, productQuantity).build();
        Category category = new Category();
        category.setName(categoryName);

        when(productRepository.findProductById(anyString())).thenReturn(Optional.of(product));
        when(categoryRepository.findCategoryByName(categoryName)).thenReturn(Optional.of(category));

        // Act
        categoryService.addProductToCategory(categoryName, product.getId());

        // Assert
        verify(productRepository, times(1)).findProductById(product.getId());
        verify(categoryRepository, times(1)).findCategoryByName(categoryName);
        assertTrue(category.getProducts().contains(product));
    }

    @Test
    void testCreateWithNullCategory() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.create(null));
    }

    @Test
    void testCreateWithExistingCategory() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));
        when(categoryMapper.convertToEntity(any(CategoryDto.class))).thenReturn(category);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> categoryService.create(categoryDto));
        assertEquals("Category with this name already exists", exception.getMessage());
    }

    @Test
    void testFindByNameWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.findByName(null));
    }

    @Test
    void testFindByNameWithNonExistingCategory() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.findByName("Electronics"));
    }

    @Test
    void testFindByIdWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.findById(null));
    }

    @Test
    void testFindByIdWithNonExistingCategory() {
        when(categoryRepository.findCategoryById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.findById(1L));
    }

    @Test
    void testDeleteByIdWithNullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> categoryService.deleteById(null));
        assertEquals("Id cannot be null", exception.getMessage());
    }

    @Test
    void testAddProductToCategoryWithNullCategoryName() {
        String productId = "1";
        assertThrows(IllegalArgumentException.class, () -> categoryService.addProductToCategory(null, productId));
    }

    @Test
    void testAddProductToCategoryWithNonExistingCategory() {
        String productId = "1";
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.addProductToCategory("Electronics", productId));
    }

    @Test
    void testRemoveProductFromCategoryWithNullCategoryName() {
        String productId = "1";
        assertThrows(IllegalArgumentException.class, () -> categoryService.removeProductFromCategory(null, productId));
    }

    @Test
    void testRemoveProductFromCategoryWithNonExistingCategory() {
        String productId = "1";
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.removeProductFromCategory("Electronics", productId));
    }

    @Test
    void testFindById() {
        Long id = 1L;
        when(categoryRepository.findCategoryById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.convertToDto(any(Category.class))).thenReturn(categoryDto);

        CategoryDto result = categoryService.findById(id);

        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).findCategoryById(id);
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        when(categoryRepository.findCategoryById(id)).thenReturn(Optional.of(category));

        categoryService.deleteById(id);
        verify(categoryRepository, times(1)).deleteCategoryById(id);
    }

    @Test
    void testRemoveProductFromCategory() {
        // Arrange
        Product product = new Product();
        Category category1 = new Category();
        category1.getProducts().add(product);
        product.getCategories().add(category1);
        when(productRepository.findProductById(anyString())).thenReturn(Optional.of(product));
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category1));

        // Act
        categoryService.removeProductFromCategory("categoryName", "productId");

        // Assert
        assertFalse(category1.getProducts().contains(product));
        assertFalse(product.getCategories().contains(category));
        verify(categoryRepository).saveCategory(category1);
        verify(productRepository).saveProduct(product);
    }

    @Test
    void testRemoveProductFromCategoryWithNullProductDto() {
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.removeProductFromCategory("Electronics", null));
    }

    @Test
    void testRemoveProductFromCategoryWithNonExistingProduct() {
        ProductResponseDto productDto = new ProductResponseDto();
        productDto.setId("1");
        productDto.setName("TV");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);

        when(productRepository.findProductById(productDto.getId())).thenReturn(Optional.empty());

        Executable executable = () -> categoryService.removeProductFromCategory("Electronics", productDto.getId());

        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void testAddProductToCategoryWithNullProductDto() {
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.addProductToCategory("Electronics", null));
    }

    @Test
    void testRemoveProductFromCategoryWithNonExistingCategory2() {
        String productId = "zczc";
        String categoryName = "NonExistingCategory";

        when(productRepository.findProductById(productId)).thenReturn(Optional.of(new Product()));
        when(categoryRepository.findCategoryByName(categoryName)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.removeProductFromCategory(categoryName, productId));
    }

    @Test
    void testAddProductToCategory_CategoryNotFound() {
        when(productRepository.findProductById("1")).thenReturn(Optional.of(product1));
        when(categoryRepository.findCategoryByName("Electronics")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.addProductToCategory("Electronics", "1");
        });

        assertEquals("Category isn't found", exception.getMessage());
        verify(categoryRepository, never()).saveCategory(any(Category.class));
        verify(productRepository, never()).saveProduct(any(Product.class));
    }
}