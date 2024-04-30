package id.ac.ui.cs.advprog.heymartbeproduct.model.Builder;

import java.util.Set;
import java.util.HashSet;

import id.ac.ui.cs.advprog.heymartbeproduct.model.Category;
import id.ac.ui.cs.advprog.heymartbeproduct.model.Product;

public class ProductBuilder {
    // Required parameters
    private String name;
    private double price;
    private int quantity;

    // Optional parameters
    private String description;
    private String image;
    private Set<Category> categories = new HashSet<>();
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

    public ProductBuilder setCategories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public ProductBuilder setCategoryNames(Set<String> categoryNames) {
        this.categoryNames = categoryNames;
        return this;
    }

    public Product build() {
        Product product = new Product();
        product.setName(this.name);
        product.setPrice(this.price);
        product.setQuantity(this.quantity);
        product.setDescription(this.description);
        product.setImage(this.image);
        product.setCategories(this.categories);
        product.setCategoryNames(this.categoryNames);
        return product;
    }
}