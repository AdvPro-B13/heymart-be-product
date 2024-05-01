package id.ac.ui.cs.advprog.heymartbeproduct.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private double price;
    private String description;
    private int quantity;
    private String image;
    private Set<String> categoryNames;
}