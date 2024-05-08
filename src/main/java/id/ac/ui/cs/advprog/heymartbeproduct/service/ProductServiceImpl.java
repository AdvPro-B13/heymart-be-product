package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = productMapper.convertToEntity(productDto);
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (productRepository.findProductById(product.getId()).isPresent()) {
            throw new IllegalArgumentException("Product with this ID already exists");
        }
        return productMapper.convertToDto(productRepository.saveProduct(product));
    }

    @Override
    public ProductDto findById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product isn't found"));
        return productMapper.convertToDto(product);
    }

    @Override
    public ProductDto edit(ProductDto productDto) {
        Product product = productMapper.convertToEntity(productDto);
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!productRepository.findProductById(product.getId()).isPresent()) {
            throw new IllegalArgumentException("Product not found");
        }
        return productMapper.convertToDto(productRepository.saveProduct(product));
    }

    @Override
    public void deleteById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (!productRepository.findProductById(id).isPresent()) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteProductById(id);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.getAllProducts().stream()
                .map(productMapper::convertToDto)
                .collect(Collectors.toList());
    }
}