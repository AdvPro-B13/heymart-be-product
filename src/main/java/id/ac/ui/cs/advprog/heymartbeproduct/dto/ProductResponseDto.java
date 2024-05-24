package id.ac.ui.cs.advprog.heymartbeproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private String id;
    private String name;
    private double price;
    private String description;
    private int quantity;
    private String image;
    private Set<String> categoryNames;
}