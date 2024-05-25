package id.ac.ui.cs.advprog.heymartbeproduct.enums;

import lombok.Getter;

@Getter
public enum ErrorResponse {
    UNAUTHORIZED("Unauthorized"),
    NOT_FOUND("Not Found");

    private final String value;

    private ErrorResponse(String value) {
        this.value = value;
    }
}