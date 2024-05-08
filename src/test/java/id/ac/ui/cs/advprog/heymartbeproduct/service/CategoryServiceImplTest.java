package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.CategoryMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.CategoryRepository;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
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

    @BeforeEach
    void setup() {
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
    void testEditName() {
        Long oldId = 1L;
        String newName = "NewCategory";

        CategoryDto newCategoryDto = new CategoryDto();
        newCategoryDto.setName(newName);
        newCategoryDto.setProductIds(new HashSet<>(Arrays.asList("newProduct")));

        Category oldCategory = new Category();
        oldCategory.setId(oldId);
        oldCategory.setName("OldCategory");

        Product oldProduct = new Product();
        oldProduct.setId("oldProduct");
        oldCategory.getProducts().add(oldProduct);

        Product newProduct = new Product();
        newProduct.setId("newProduct");

        when(categoryRepository.findCategoryById(oldId)).thenReturn(Optional.of(oldCategory));
        when(productRepository.findProductById(oldProduct.getId())).thenReturn(Optional.of(oldProduct));
        when(productRepository.findProductById(newProduct.getId())).thenReturn(Optional.of(newProduct));
        when(categoryRepository.saveCategory(any(Category.class))).thenReturn(oldCategory);
        when(productRepository.saveProduct(any(Product.class))).thenReturn(newProduct);
        when(categoryMapper.convertToDto(any(Category.class))).thenReturn(newCategoryDto);

        CategoryDto result = categoryService.edit(oldId, newCategoryDto);

        assertEquals(newName, result.getName());
        assertTrue(result.getProductIds().contains(newProduct.getId()));
        assertFalse(result.getProductIds().contains(oldProduct.getId()));
        verify(categoryRepository, times(1)).saveCategory(any(Category.class));
        verify(productRepository, times(2)).saveProduct(any(Product.class));
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
        ProductDto productDto = new ProductDto();
        productDto.setName("TV");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);
        Product product = new Product.ProductBuilder("TV", 100.0, 10).build();
        Category category = new Category();
        category.setName("Electronics");

        when(productMapper.convertToEntity(productDto)).thenReturn(product);
        when(categoryRepository.findCategoryByName("Electronics")).thenReturn(Optional.of(category));

        categoryService.addProductToCategory("Electronics", productDto);
        verify(productMapper, times(1)).convertToEntity(productDto);
        verify(categoryRepository, times(1)).findCategoryByName("Electronics");
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
    void testEditWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> categoryService.edit(null, categoryDto));
    }

    @Test
    void testEditWithNonExistingCategory() {
        when(categoryRepository.findCategoryById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> categoryService.edit(1L, categoryDto));
    }

    @Test
    void testDeleteByIdWithNullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> categoryService.deleteById(null));
        assertEquals("Id cannot be null", exception.getMessage());
    }

    @Test
    void testAddProductToCategoryWithNullCategoryName() {
        ProductDto productDto = new ProductDto();
        productDto.setName("TV");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);
        assertThrows(IllegalArgumentException.class, () -> categoryService.addProductToCategory(null, productDto));
    }

    @Test
    void testAddProductToCategoryWithNonExistingCategory() {
        ProductDto productDto = new ProductDto();
        productDto.setName("TV");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.addProductToCategory("Electronics", productDto));
    }

    @Test
    void testRemoveProductFromCategoryWithNullCategoryName() {
        ProductDto productDto = new ProductDto();
        productDto.setName("TV");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);
        assertThrows(IllegalArgumentException.class, () -> categoryService.removeProductFromCategory(null, productDto));
    }

    @Test
    void testRemoveProductFromCategoryWithNonExistingCategory() {
        ProductDto productDto = new ProductDto();
        productDto.setName("TV");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);
        when(categoryRepository.findCategoryByName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.removeProductFromCategory("Electronics", productDto));
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
        ProductDto productDto = new ProductDto();
        productDto.setName("TV");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);

        Product product = new Product.ProductBuilder("TV", 100.0, 10).build();

        Category category = new Category.CategoryBuilder("Electronics").build();
        category.addProduct(product);

        when(productRepository.findProductById(productDto.getId())).thenReturn(Optional.of(product));
        when(categoryRepository.findCategoryByName("Electronics")).thenReturn(Optional.of(category));

        categoryService.removeProductFromCategory("Electronics", productDto);

        assertFalse(category.getProducts().contains(product));
        verify(categoryRepository, times(1)).saveCategory(category);
    }

    @Test
    void testRemoveProductFromCategoryWithNullProductDto() {
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.removeProductFromCategory("Electronics", null));
    }

    @Test
    void testRemoveProductFromCategoryWithNonExistingProduct() {
        ProductDto productDto = new ProductDto();
        productDto.setName("TV");
        productDto.setPrice(100.0);
        productDto.setQuantity(10);

        when(productRepository.findProductById(productDto.getId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.removeProductFromCategory("Electronics", productDto));
    }

    @Test
    void testAddProductToCategoryWithNullProductDto() {
        assertThrows(IllegalArgumentException.class,
                () -> categoryService.addProductToCategory("Electronics", null));
    }

    @Test
    void testRemoveProductFromCategoryWithNonExistingCategory2() {
        ProductDto productDto = new ProductDto();
        productDto.setId("zczc");

        when(productRepository.findProductById(productDto.getId())).thenReturn(Optional.of(new Product()));
        when(categoryRepository.findCategoryByName("NonExistingCategory")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> categoryService.removeProductFromCategory("NonExistingCategory", productDto));
    }

    @Test
    void testEditProductRemovedFromCategory() {
        Long categoryId = 1L;
        CategoryDto newCategoryDto = new CategoryDto();
        newCategoryDto.setName("NewCategory");
        newCategoryDto.setProductIds(new HashSet<>(Arrays.asList("newProduct")));

        Category category = new Category();
        category.setId(categoryId);
        category.setName("OldCategory");

        Product oldProduct = new Product();
        oldProduct.setId("oldProduct");
        oldProduct.getCategories().add(category);
        category.getProducts().add(oldProduct);

        Product newProduct = new Product();
        newProduct.setId("newProduct");

        CategoryDto returnedCategoryDto = new CategoryDto();
        returnedCategoryDto.setName("NewCategory");
        returnedCategoryDto.setProductIds(new HashSet<>(Arrays.asList("newProduct")));

        when(categoryRepository.findCategoryById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findProductById(oldProduct.getId())).thenReturn(Optional.of(oldProduct));
        when(productRepository.findProductById("newProduct")).thenReturn(Optional.of(newProduct));
        when(categoryMapper.convertToDto(any(Category.class))).thenReturn(returnedCategoryDto);

        CategoryDto result = categoryService.edit(categoryId, newCategoryDto);

        assertNotNull(result);
        assertFalse(result.getProductIds().contains(oldProduct.getId()));
        verify(productRepository, times(1)).saveProduct(oldProduct);
        verify(productRepository, times(1)).saveProduct(newProduct);
    }

    @Test
    void testEditProductAddedToCategory() {
        Long categoryId = 1L;
        CategoryDto newCategoryDto = new CategoryDto();
        newCategoryDto.setName("NewCategory");
        newCategoryDto.setProductIds(new HashSet<>(Arrays.asList("newProduct")));

        Category category = new Category();
        category.setId(categoryId);
        category.setName("OldCategory");

        Product newProduct = new Product();
        newProduct.setId("newProduct");

        CategoryDto returnedCategoryDto = new CategoryDto();
        returnedCategoryDto.setName("NewCategory");
        returnedCategoryDto.setProductIds(new HashSet<>(Arrays.asList("newProduct")));

        when(categoryRepository.findCategoryById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findProductById("newProduct")).thenReturn(Optional.of(newProduct));
        when(categoryMapper.convertToDto(any(Category.class))).thenReturn(returnedCategoryDto);

        CategoryDto result = categoryService.edit(categoryId, newCategoryDto);

        assertNotNull(result);
        assertTrue(result.getProductIds().contains(newProduct.getId()));
        verify(productRepository, times(1)).saveProduct(newProduct);
    }

    @Test
    void testEditProductNotFound() {
        Long categoryId = 1L;
        CategoryDto newCategoryDto = new CategoryDto();
        newCategoryDto.setName("NewCategory");
        newCategoryDto.setProductIds(new HashSet<>(Arrays.asList("nonExistentProduct")));

        Category category = new Category();
        category.setId(categoryId);
        category.setName("OldCategory");

        when(categoryRepository.findCategoryById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findProductById("nonExistentProduct")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.edit(categoryId, newCategoryDto);
        });
    }
}