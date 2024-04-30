package id.ac.ui.cs.advprog.heymartbeproduct.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "product")
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

    @Column(name = "image")
    private String image;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "product_category", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_name"))
    private Set<Category> categories = new HashSet<>();

    @Transient
    private Set<String> categoryNames = new HashSet<>();

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