package id.ac.ui.cs.advprog.heymartbeproduct.repository;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Optional<Product> findProductById(String id) {
        Product product = entityManager.find(Product.class, id);
        return Optional.ofNullable(product);
    }

    @Transactional
    public List<Product> findProductsBySupermarketId(Long supermarketId) {
        return entityManager
                .createQuery("SELECT p FROM Product p WHERE p.supermarketId = :supermarketId", Product.class)
                .setParameter("supermarketId", supermarketId)
                .getResultList();
    }

    @Transactional
    public Optional<Product> findProductByIdAndSupermarketId(String id, Long supermarketId) {
        try {
            TypedQuery<Product> query = entityManager.createQuery(
                    "SELECT p FROM Product p WHERE p.id = :id AND p.supermarketId = :supermarketId", Product.class);
            query.setParameter("id", id);
            query.setParameter("supermarketId", supermarketId);
            Product product = query.getSingleResult();
            return Optional.of(product);
        } catch (NoResultException e) {
            return Optional.empty();
        }
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