package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.CategoryMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.CategoryRepository;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;

    @Autowired
    public CategoryServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
            CategoryMapper categoryMapper,
            ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        Category category = categoryMapper.convertToEntity(categoryDto);
        if (categoryRepository.findCategoryByName(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Category with this name already exists");
        }
        category = categoryRepository.saveCategory(category);
        return categoryMapper.convertToDto(category);
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category id not found"));
        return categoryMapper.convertToDto(category);
    }

    @Override
    public CategoryDto findByName(String name) {
        Category category = categoryRepository.findCategoryByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Category name not found"));
        return categoryMapper.convertToDto(category);
    }

    @Override
    public CategoryDto edit(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.setName(categoryDto.getName());

        Set<Product> newProducts = categoryDto.getProductIds().stream()
                .map(productId -> productRepository.findProductById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Product not found")))
                .collect(Collectors.toSet());

        category.getProducts().stream()
                .filter(product -> !newProducts.contains(product))
                .forEach(product -> {
                    product.getCategories().remove(category);
                    productRepository.saveProduct(product);
                });

        newProducts.stream()
                .filter(product -> !category.getProducts().contains(product))
                .forEach(product -> {
                    product.getCategories().add(category);
                    productRepository.saveProduct(product);
                });

        category.setProducts(newProducts);
        categoryRepository.saveCategory(category);
        return categoryMapper.convertToDto(category);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        categoryRepository.deleteCategoryById(id);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.getAllCategories().stream()
                .map(categoryMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void addProductToCategory(String categoryName, ProductDto productDto) {
        if (productDto == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        Product product = productMapper.convertToEntity(productDto);
        Category category = categoryRepository.findCategoryByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category isn't found"));
        category.getProducts().add(product);
        categoryRepository.saveCategory(category);
    }

    @Override
    public void removeProductFromCategory(String categoryName, ProductDto productDto) {
        if (productDto == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        Product product = productRepository.findProductById(productDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product isn't found"));
        Category category = categoryRepository.findCategoryByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category isn't found"));
        category.getProducts().remove(product);
        categoryRepository.saveCategory(category);
    }
}