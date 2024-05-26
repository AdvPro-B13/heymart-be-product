package id.ac.ui.cs.advprog.heymartbeproduct.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
public class Product {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "supermarket_id")
    private Long supermarketId;

    @Column(name = "image")
    private String image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "name"))
    private Set<Category> categories = new HashSet<>();

    @Transient
    private Set<String> categoryNames = new HashSet<>();

    public Product() {
        this.id = UUID.randomUUID().toString();
    }

    private Product(ProductBuilder builder) {
        this.id = UUID.randomUUID().toString();
        this.name = builder.name;
        this.price = builder.price;
        this.description = builder.description;
        this.quantity = builder.quantity;
        this.image = builder.image;
        this.supermarketId = builder.supermarketId;
        this.categories = builder.categories != null ? new HashSet<>(builder.categories) : new HashSet<>();
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public void addCategory(Category category) {
        if (category == null) {
            throw new NullPointerException();
        }
        this.categories.add(category);
        category.getProducts().add(this);
        this.categoryNames.add(category.getName());
    }

    public void removeCategory(Category category) {
        if (category == null) {
            throw new NullPointerException();
        }
        categories.remove(category);
        category.getProducts().remove(this);
        this.categoryNames.remove(category.getName());
    }

    public void updateCategory(Category oldCategory, Category newCategory) {
        if (oldCategory == null || newCategory == null) {
            throw new NullPointerException();
        }
        if (categories.contains(oldCategory)) {
            categories.remove(oldCategory);
            categories.add(newCategory);
            this.categoryNames.remove(oldCategory.getName());
            this.categoryNames.add(newCategory.getName());
        }

    }

    public static class ProductBuilder {
        // Required parameters
        private String name;
        private double price;
        private int quantity;

        // Optional parameters
        private String description;
        private String image;
        private Long supermarketId;
        private Set<Category> categories;
        private Set<String> categoryNames;

        public ProductBuilder(String name, double price, int quantity) {
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public ProductBuilder() {
        }

        public ProductBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder setImage(String image) {
            this.image = image;
            return this;
        }

        public ProductBuilder setSupermarketId(Long supermarketId) {
            this.supermarketId = supermarketId;
            return this;
        }

        public ProductBuilder setCategories(Set<Category> categories) {
            this.categories = categories;
            return this;
        }

        public ProductBuilder setCategoryNames(Set<String> categoryNames) {
            this.categoryNames = categoryNames;
            return this;
        }

        public Product build() {
            Product product = new Product(this);
            product.setCategoryNames(this.categoryNames);
            return product;
        }
    }
}
