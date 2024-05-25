package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.create(categoryDto), HttpStatus.CREATED);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryDto> findByName(@PathVariable String name) {
        return new ResponseEntity<>(categoryService.findByName(name), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<CategoryDto> edit(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return new ResponseEntity<>(categoryService.edit(id, categoryDto), HttpStatus.OK);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return new ResponseEntity<>("DELETE SUCCESS", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }
}