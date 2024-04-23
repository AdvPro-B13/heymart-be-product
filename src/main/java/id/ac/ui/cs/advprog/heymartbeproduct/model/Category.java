package id.ac.ui.cs.advprog.heymartbeproduct.model;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products;

    private Category(CategoryBuilder builder) {
        this.name = builder.name;
        this.products = builder.products != null ? new HashSet<>(builder.products) : new HashSet<>();
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

    public static class CategoryBuilder {
        private String name;
        private Set<Product> products;

        public CategoryBuilder(String name) {
            this.name = name;
        }

        public CategoryBuilder setProducts(Set<Product> products) {
            this.products = products;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }
}
