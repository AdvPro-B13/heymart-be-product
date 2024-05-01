package id.ac.ui.cs.advprog.heymartbeproduct.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private String name;
    private Set<String> productIds = new HashSet<>();

    public void setProductIds(Set<String> productIds) {
        if (productIds != null) {
            this.productIds = productIds;
        }
    }
}