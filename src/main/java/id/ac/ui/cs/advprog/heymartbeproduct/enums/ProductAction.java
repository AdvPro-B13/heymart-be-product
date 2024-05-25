package id.ac.ui.cs.advprog.heymartbeproduct.enums;

import lombok.Getter;

@Getter
public enum ProductAction {
    CREATE("product:create"),
    READ("product:read"),
    EDIT("product:edit"),
    DELETE("product:delete");

    private final String value;

    private ProductAction(String value) {
        this.value = value;
    }
}