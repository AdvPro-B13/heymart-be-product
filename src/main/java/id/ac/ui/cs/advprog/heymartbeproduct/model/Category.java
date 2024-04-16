package id.ac.ui.cs.advprog.heymartbeproduct.model;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products;

    public Category(String name) {
        this.name = name;
        this.products = new HashSet<>();
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new NullPointerException();
        }
        this.products.add(product);
        product.getCategories().add(this);
    }

    public void removeProduct(Product product) {
        if (product == null) {
            throw new NullPointerException();
        }
        if (this.products.contains(product)) {
            this.products.remove(product);
            product.getCategories().remove(this);
        }
    }
}
