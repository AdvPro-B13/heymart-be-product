package id.ac.ui.cs.advprog.heymartbeproduct.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private double price;
    private String description;
    private int quantity;
    private String image;

    @ManyToMany
    @JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "name"))
    private Set<Category> categories;

    private Product(ProductBuilder builder) {
        this.id = UUID.randomUUID().toString();
        this.name = builder.name;
        this.price = builder.price;
        this.description = builder.description;
        this.quantity = builder.quantity;
        this.image = builder.image;
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
        if (this.categories == null) {
            this.categories = new HashSet<>();
        }
        this.categories.add(category);
        category.getProducts().add(this);
    }

    public void removeCategory(Category category) {
        if (category == null) {
            throw new NullPointerException();
        }
        if (categories != null) {
            categories.remove(category);
        }
        category.getProducts().remove(this);
    }

    public static class ProductBuilder {
        // Required parameters
        private String name;
        private double price;
        private int quantity;

        // Optional parameters
        private String description;
        private String image;
        private Set<Category> categories;

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

        public ProductBuilder setCategories(Set<Category> categories) {
            this.categories = categories;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
