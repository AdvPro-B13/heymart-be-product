package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.Dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.Dto.CategoryMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.Dto.ProductDto;
import id.ac.ui.cs.advprog.heymartbeproduct.Dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.CategoryRepository;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Optional;
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
            throw new IllegalArgumentException("CategoryDto cannot be null");
        }
        Category category = categoryMapper.convertToEntity(categoryDto);
        if (categoryRepository.findCategoryByName(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Category with this name already exists");
        }
        category = categoryRepository.saveCategory(category);
        return categoryMapper.convertToDto(category);
    }

    @Override
    public CategoryDto findByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        Category category = categoryRepository.findCategoryByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Category isn't found"));
        return categoryMapper.convertToDto(category);
    }

    @Override
    public CategoryDto edit(String name, CategoryDto categoryDto) {
        Optional<Category> oldCategoryOpt = categoryRepository.findCategoryByName(name);
        if (!oldCategoryOpt.isPresent()) {
            throw new IllegalArgumentException("Category not found");
        }
        Category oldCategory = oldCategoryOpt.get();
        String oldName = oldCategory.getName();
        oldCategory.setName(categoryDto.getName());

        Set<Product> oldProducts = new HashSet<>(oldCategory.getProducts());
        oldCategory.getProducts().clear();

        categoryDto.getProductIds().forEach(productId -> {
            Product product = productRepository.findProductById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            oldCategory.getProducts().add(product);
        });

        categoryRepository.saveCategory(oldCategory);

        oldProducts.forEach(product -> {
            product.getCategories().remove(oldCategory);
            productRepository.saveProduct(product);
        });

        oldCategory.getProducts().forEach(product -> {
            product.getCategories().add(oldCategory);
            productRepository.saveProduct(product);
        });

        if (!oldName.equals(categoryDto.getName())) {
            deleteByName(oldName);
        }
        return categoryMapper.convertToDto(oldCategory);
    }

    @Override
    public void deleteByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (!categoryRepository.findCategoryByName(name).isPresent()) {
            throw new IllegalArgumentException("Category not found");
        }
        categoryRepository.deleteCategoryByName(name);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.getAllCategories().stream()
                .map(categoryMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void addProductToCategory(String categoryName, ProductDto productDto) {
        if (categoryName == null || categoryName.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (productDto == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        Product product = productMapper.convertToEntity(productDto);
        categoryRepository.addProductToCategory(categoryName, product);
    }

    @Override
    @Transactional
    public void removeProductFromCategory(String categoryName, ProductDto productDto) {
        if (categoryName == null || categoryName.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (productDto == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        Product product = productMapper.convertToEntity(productDto);
        Category category = categoryRepository.findCategoryByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category isn't found"));
        category.getProducts().remove(product);
        categoryRepository.saveCategory(category);
    }

}