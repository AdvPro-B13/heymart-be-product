package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.Dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.Dto.CategoryMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.Dto.ProductDto;
import id.ac.ui.cs.advprog.heymartbeproduct.Dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.CategoryRepository;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CategoryServiceImplTest {

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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        category = new Category.CategoryBuilder("Electronics").build();
        categoryDto = new CategoryDto();
        categoryDto.setName("Electronics");

        Product product1 = new Product.ProductBuilder("TV", 100.0, 10).build();
        Product product2 = new Product.ProductBuilder("Radio", 50.0, 5).build();
        category.addProduct(product1);
        category.addProduct(product2);
    }

    @Test
    public void testCreate() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepository.saveCategory(any(Category.class))).thenReturn(category);
        when(categoryMapper.convertToEntity(any(CategoryDto.class))).thenReturn(category);
        when(categoryMapper.convertToDto(any(Category.class))).thenReturn(categoryDto);

        CategoryDto result = categoryService.create(categoryDto);

        assertEquals(categoryDto, result);
        verify(categoryRepository, times(1)).saveCategory(category);
    }

    @Test
    public void testFindByName() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));
        when(categoryMapper.convertToDto(any(Category.class))).thenReturn(categoryDto);

        CategoryDto result = categoryService.findByName("Electronics");

        assertEquals("Electronics", result.getName());
        verify(categoryRepository, times(1)).findCategoryByName("Electronics");
    }

    @Test
    public void testEdit() {
        String oldName = "OldCategory";
        String newName = "NewCategory";

        CategoryDto newCategoryDto = new CategoryDto();
        newCategoryDto.setName(newName);

        Category oldCategory = new Category();
        oldCategory.setName(oldName);

        Category newCategory = new Category();
        newCategory.setName(newName);

        Product product = new Product();
        oldCategory.getProducts().add(product);
        newCategoryDto.getProductIds().add(product.getId());
        categoryRepository.saveCategory(oldCategory);

        when(categoryRepository.findCategoryByName(oldName)).thenReturn(Optional.of(oldCategory));
        when(productRepository.findProductById(any())).thenReturn(Optional.of(product));
        when(categoryRepository.saveCategory(any(Category.class))).thenReturn(newCategory);
        when(productRepository.saveProduct(any(Product.class))).thenReturn(product);
        when(categoryMapper.convertToDto(any(Category.class))).thenReturn(newCategoryDto);

        CategoryDto result = categoryService.edit(oldName, newCategoryDto);

        assertEquals(newName, result.getName());
        verify(categoryRepository, times(2)).saveCategory(any(Category.class));
        verify(productRepository, times(oldCategory.getProducts().size() * 2)).saveProduct(any(Product.class));
        verify(categoryRepository, times(1)).deleteCategoryByName(oldName);
    }

    @Test
    public void testDeleteByName() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));

        categoryService.deleteByName("Electronics");

        verify(categoryRepository, times(1)).deleteCategoryByName("Electronics");
    }

    @Test
    public void testGetAllCategories() {
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
    public void testAddProductToCategory() {
        ProductDto productDto = new ProductDto();
        productDto.setName("TV");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);
        Product product = new Product.ProductBuilder("TV", 100.0, 10).build();
        when(productMapper.convertToEntity(any(ProductDto.class))).thenReturn(product);
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));

        categoryService.addProductToCategory("Electronics", productDto);

        verify(productMapper, times(1)).convertToEntity(productDto);
        verify(categoryRepository, times(1)).addProductToCategory("Electronics", product);
    }

    @Test
    public void testCreateWithNullCategory() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.create(null));
    }

    @Test
    public void testCreateWithExistingCategory() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.of(category));
        when(categoryMapper.convertToEntity(any(CategoryDto.class))).thenReturn(category);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> categoryService.create(categoryDto));
        assertEquals("Category with this name already exists", exception.getMessage());
    }

    @Test
    public void testFindByNameWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.findByName(null));
    }

    @Test
    public void testFindByNameWithNonExistingCategory() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.findByName("Electronics"));
    }

    @Test
    public void testEditWithNonExistingCategory() {
        String oldName = "OldCategory";
        CategoryDto newCategoryDto = new CategoryDto();
        newCategoryDto.setName("NewCategory");

        when(categoryRepository.findCategoryByName(oldName)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.edit(oldName, newCategoryDto));
    }

    @Test
    public void testDeleteByNameWithNonExistingCategory() {
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteByName("Electronics"));
    }

    @Test
    public void testAddProductToCategoryWithEmptyCategoryName() {
        ProductDto productDto = new ProductDto();
        productDto.setName("TV");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);
        assertThrows(IllegalArgumentException.class, () -> categoryService.addProductToCategory("", productDto));
    }

    @Test
    public void testAddProductToCategoryWithNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.addProductToCategory("Electronics", null));
    }

    @Test
    public void testRemoveProductFromCategoryWithEmptyCategoryName() {
        ProductDto productDto = new ProductDto();
        productDto.setName("TV");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);
        assertThrows(IllegalArgumentException.class, () -> categoryService.removeProductFromCategory("", productDto));
    }

    @Test
    public void testRemoveProductFromCategoryWithNullProduct() {
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.removeProductFromCategory("Electronics", null));
    }
}