package id.ac.ui.cs.advprog.heymartbeproduct.repository;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.Set;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private static final String CATEGORY_NOT_FOUND = "Category not found";

    @Transactional
    public Optional<Category> findCategoryById(Long id) {
        TypedQuery<Category> query = entityManager.createQuery("SELECT c FROM Category c WHERE c.id = :id",
                Category.class);
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Modifying
    @Transactional
    public void deleteCategoryById(Long id) {
        Query query = entityManager.createQuery("DELETE FROM Category c WHERE c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Transactional
    public Optional<Category> findCategoryByName(String name) {
        List<Category> categories = entityManager
                .createQuery("SELECT c FROM Category c WHERE c.name = :name", Category.class)
                .setParameter("name", name)
                .getResultList();
        return categories.isEmpty() ? Optional.empty() : Optional.of(categories.get(0));
    }

    @Transactional
    public Category saveCategory(Category category) {
        if (category.getName() == null || findCategoryByName(category.getName()).isEmpty()) {
            entityManager.persist(category);
        } else {
            category = entityManager.merge(category);
        }
        return category;
    }

    @Transactional
    public void deleteCategoryByName(String name) {
        Category category = findCategoryByName(name)
                .orElseThrow(() -> new IllegalArgumentException(CategoryRepository.CATEGORY_NOT_FOUND));

        Set<Product> products = category.getProducts();

        for (Product product : products) {
            product.getCategories().remove(category);
            entityManager.merge(product);
        }
        entityManager.remove(category);
    }

    @Transactional
    public List<Category> getAllCategories() {
        return entityManager.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    @Transactional
    public void addProductToCategory(String categoryName, Product product) {
        Category category = findCategoryByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException(CategoryRepository.CATEGORY_NOT_FOUND));
        category.addProduct(product);
        entityManager.merge(category);
    }

    @Transactional
    public void removeProductFromCategory(String categoryName, Product product) {
        Category category = findCategoryByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException(CategoryRepository.CATEGORY_NOT_FOUND));
        category.removeProduct(product);
        entityManager.merge(category);
    }
}