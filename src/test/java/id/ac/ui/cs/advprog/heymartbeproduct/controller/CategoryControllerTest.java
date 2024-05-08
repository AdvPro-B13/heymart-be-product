package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import id.ac.ui.cs.advprog.heymartbeproduct.model.dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.service.CategoryService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    public CategoryControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory() {
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.create(any())).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> responseEntity = categoryController.create(categoryDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(categoryDto, responseEntity.getBody());
    }

    @Test
    void testFindCategoryByName() {
        String name = "Test";
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.findByName(name)).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> responseEntity = categoryController.findByName(name);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categoryDto, responseEntity.getBody());
    }

    @Test
    void testFindCategoryById() {
        Long id = 1L;
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.findById(id)).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> responseEntity = categoryController.findById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categoryDto, responseEntity.getBody());
    }

    @Test
    void testEditCategory() {
        Long id = 1L;
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.edit(any(), any())).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> responseEntity = categoryController.edit(id, categoryDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categoryDto, responseEntity.getBody());
    }

    @Test
    void testDeleteCategoryById() {
        Long id = 1L;

        ResponseEntity<String> responseEntity = categoryController.deleteById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("DELETE SUCCESS", responseEntity.getBody());
        verify(categoryService, times(1)).deleteById(id);
    }

    @Test
    void testGetAllCategories() {
        List<CategoryDto> categoryDtoList = Collections.emptyList();
        when(categoryService.getAllCategories()).thenReturn(categoryDtoList);

        ResponseEntity<List<CategoryDto>> responseEntity = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categoryDtoList, responseEntity.getBody());
    }
}