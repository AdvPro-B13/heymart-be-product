package id.ac.ui.cs.advprog.heymartbeproduct.model.Builder;

import java.util.HashSet;
import java.util.Set;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;

public class CategoryBuilder {
    private String name;
    private Set<Product> products = new HashSet<>();

    public CategoryBuilder(String name) {
        this.name = name;
    }

    public CategoryBuilder setProducts(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Category build() {
        Category category = new Category();
        category.setName(this.name);
        if (this.products != null) {
            category.setProducts(new HashSet<>(this.products));
        }
        return category;
    }
}