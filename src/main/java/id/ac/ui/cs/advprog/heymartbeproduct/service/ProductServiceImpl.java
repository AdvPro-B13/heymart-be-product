package id.ac.ui.cs.advprog.heymartbeproduct.service;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductResponseDto;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductMapper;
import id.ac.ui.cs.advprog.heymartbeproduct.dto.ProductRequestDto;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import id.ac.ui.cs.advprog.heymartbeproduct.repository.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
    public CompletableFuture<ProductResponseDto> create(ProductRequestDto productRequestDto) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Creating product: {}", productRequestDto);
            Product product = productMapper.convertToEntity(productRequestDto);
            product.setId(UUID.randomUUID().toString());
            logger.info("Created product with ID: {}", product.getId());
            return productMapper.convertToDto(productRepository.saveProduct(product));
        }, taskExecutor);
    }

    @Override
    public ProductResponseDto findById(String id) {
        logger.info("Finding product with id: {} on thread: {}", id, Thread.currentThread().getName());
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product isn't found"));
        logger.info("Found product with ID: {}", id);
        return productMapper.convertToDto(product);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<ProductResponseDto> edit(String id, ProductRequestDto productRequestDto) {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Editing product with ID: {}", id);
            Product product = productMapper.convertToEntity(productRequestDto);
            if (product == null) {
                throw new IllegalArgumentException("Product cannot be null");
            }
            if (!productRepository.findProductById(id).isPresent()) {
                throw new IllegalArgumentException("Product not found");
            }
            product.setId(id);
            logger.info("Edited product with ID: {}", id);
            return productMapper.convertToDto(productRepository.saveProduct(product));
        }, taskExecutor);
    }

    @Override
    public void deleteById(String id) {
        logger.info("Deleting product with ID: {}", id);
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (!productRepository.findProductById(id).isPresent()) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteProductById(id);
        logger.info("Deleted product with ID: {}", id);
    }

    @Override
    @Async("taskExecutor")
    public CompletableFuture<List<ProductResponseDto>> getAllProducts() {
        return CompletableFuture.supplyAsync(() -> {
            logger.info("Getting all products");
            List<ProductResponseDto> products = productRepository.getAllProducts().stream()
                    .map(productMapper::convertToDto)
                    .toList();
            logger.info("Found {} products", products.size());
            return products;
        }, taskExecutor);
    }
}