package id.ac.ui.cs.advprog.heymartbeproduct.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    public Category() {
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