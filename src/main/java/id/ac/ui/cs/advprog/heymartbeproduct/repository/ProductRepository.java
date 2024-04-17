package id.ac.ui.cs.advprog.heymartbeproduct.repository;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static ProductRepository instance;

    private ProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static ProductRepository getInstance(EntityManager entityManager) {
        if (instance == null) {
            instance = new ProductRepository(entityManager);
        }
        return instance;
    }

    @Transactional
    public Optional<Product> findProductById(String id) {
        Product product = entityManager.find(Product.class, id);
        return Optional.ofNullable(product);
    }

    @Transactional
    public Product saveProduct(Product product) {
        if (product.getId() == null || findProductById(product.getId()).isEmpty()) {
            entityManager.persist(product);
        } else {
            product = entityManager.merge(product);
        }
        return product;
    }

    @Transactional
    public void deleteProductById(String id) {
        Product product = findProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        entityManager.remove(product);
    }

    @Transactional
    public List<Product> getAllProducts() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }
}