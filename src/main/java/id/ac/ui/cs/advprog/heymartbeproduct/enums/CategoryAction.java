package id.ac.ui.cs.advprog.heymartbeproduct.enums;

import lombok.Getter;

@Getter
public enum CategoryAction {
    CREATE("category:create"),
    READ("category:read"),
    DELETE("category:delete");

    private final String value;

    private CategoryAction(String value) {
        this.value = value;
    }
}