package id.ac.ui.cs.advprog.heymartbeproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private String id;
    private Long supermarketId;
    private String name;
    private double price;
    private String description;
    private int quantity;
    private String image;
    private Set<String> categoryNames;
}