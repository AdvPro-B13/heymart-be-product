package id.ac.ui.cs.advprog.heymartbeproduct.enums;

import lombok.Getter;

@Getter
public enum CategoryAction {
    CREATE("categories:create"),
    READ("categories:read"),
    DELETE("categories:delete");

    private final String value;

    private CategoryAction(String value) {
        this.value = value;
    }
}