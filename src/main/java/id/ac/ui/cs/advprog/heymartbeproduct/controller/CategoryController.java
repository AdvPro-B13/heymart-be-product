package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.service.AuthServiceClient;
import id.ac.ui.cs.advprog.heymartbeproduct.service.CategoryService;
import id.ac.ui.cs.advprog.heymartbeproduct.enums.ErrorResponse;
import id.ac.ui.cs.advprog.heymartbeproduct.enums.CategoryAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthServiceClient authServiceClient;

    @Autowired
    public CategoryController(CategoryService categoryService, AuthServiceClient authServiceClient) {
        this.categoryService = categoryService;
        this.authServiceClient = authServiceClient;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody CategoryDto categoryDto,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!authServiceClient.verifyUserAuthorization(CategoryAction.CREATE.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.UNAUTHORIZED.getValue());
        }
        return new ResponseEntity<>(categoryService.create(categoryDto), HttpStatus.CREATED);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Object> findByName(@PathVariable String name,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!authServiceClient.verifyUserAuthorization(CategoryAction.READ.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.UNAUTHORIZED.getValue());
        }
        return new ResponseEntity<>(categoryService.findByName(name), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!authServiceClient.verifyUserAuthorization(CategoryAction.READ.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.UNAUTHORIZED.getValue());
        }
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!authServiceClient.verifyUserAuthorization(CategoryAction.DELETE.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.UNAUTHORIZED.getValue());
        }
        categoryService.deleteById(id);
        return new ResponseEntity<>("DELETE SUCCESS", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAllCategories(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        if (!authServiceClient.verifyUserAuthorization(CategoryAction.READ.getValue(), authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.UNAUTHORIZED.getValue());
        }
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }
}