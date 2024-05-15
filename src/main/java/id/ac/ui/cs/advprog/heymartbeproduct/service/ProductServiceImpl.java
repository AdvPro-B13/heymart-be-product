package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final Executor taskExecutor;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
            @Qualifier("taskExecutor") Executor taskExecutor) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.taskExecutor = taskExecutor;
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<ProductDto> create(ProductDto productDto) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Creating product: {}", productDto);
            Product product = productMapper.convertToEntity(productDto);
            if (product == null) {
                throw new IllegalArgumentException("Product cannot be null");
            }

            return productMapper.convertToDto(productRepository.saveProduct(product));
        }, taskExecutor);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<ProductDto> findById(String id) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Finding product with id: {} on thread: {}", id, Thread.currentThread().getName());
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("ID cannot be null or empty");
            }
            Product product = productRepository.findProductById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Product isn't found"));
            logger.info("Found product with ID: {}", id);
            return productMapper.convertToDto(product);
        }, taskExecutor);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<ProductDto> edit(String id, ProductDto productDto) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Editing product with ID: {}", id);
            Product existingProduct = productRepository.findProductById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            Product productToUpdate = productMapper.convertToEntity(productDto);
            productToUpdate.setId(existingProduct.getId());
            logger.info("Edited product with ID: {}", id);
            return productMapper.convertToDto(productRepository.saveProduct(productToUpdate));
        }, taskExecutor);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<Void> deleteById(String id) {
        return CompletableFuture.runAsync(() -> {
            logger.info("Deleting product with ID: {}", id);
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("ID cannot be null or empty");
            }
            if (!productRepository.findProductById(id).isPresent()) {
                throw new IllegalArgumentException("Product not found");
            }
            productRepository.deleteProductById(id);
            logger.info("Deleted product with ID: {}", id);
        }, taskExecutor);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<ProductDto>> getAllProducts() {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Getting all products");
            List<ProductDto> products = productRepository.getAllProducts().stream()
                    .map(productMapper::convertToDto)
                    .collect(Collectors.toList());
            logger.info("Found {} products", products.size());
            return products;
        }, taskExecutor);
    }
}