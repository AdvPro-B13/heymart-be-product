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

    public Product(String name, double price, String description, int quantity, String image) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
        this.image = image;
    }

    public Product() {
        this.id = UUID.randomUUID().toString();
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
}
