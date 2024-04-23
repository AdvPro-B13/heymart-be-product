package id.ac.ui.cs.advprog.heymartbeproduct.repository;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class CategoryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Optional<Category> findCategoryByName(String name) {
        Category category = entityManager.find(Category.class, name);
        return Optional.ofNullable(category);
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
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        entityManager.remove(category);
    }

    @Transactional
    public List<Category> getAllCategories() {
        return entityManager.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    @Transactional
    public void addProductToCategory(String categoryName, Product product) {
        Category category = findCategoryByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.addProduct(product);
        entityManager.merge(category);
    }

    @Transactional
    public void removeProductFromCategory(String categoryName, Product product) {
        Category category = findCategoryByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.removeProduct(product);
        entityManager.merge(category);
    }
}