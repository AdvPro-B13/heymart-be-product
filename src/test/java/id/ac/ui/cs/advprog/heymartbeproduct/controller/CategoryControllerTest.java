package id.ac.ui.cs.advprog.heymartbeproduct.controller;

import id.ac.ui.cs.advprog.heymartbeproduct.dto.CategoryDto;
import id.ac.ui.cs.advprog.heymartbeproduct.service.AuthServiceClient;
import id.ac.ui.cs.advprog.heymartbeproduct.service.CategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private AuthServiceClient authServiceClient;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCategory() {
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.create(any())).thenReturn(categoryDto);
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);

        ResponseEntity<Object> responseEntity = categoryController.create(categoryDto, "Bearer token");

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(categoryDto, responseEntity.getBody());
    }

    @Test
    void testCreateCategoryUnauthorized() {
        CategoryDto categoryDto = new CategoryDto();
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = categoryController.create(categoryDto, "Bearer token");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Unauthorized", responseEntity.getBody());
    }

    @Test
    void testFindCategoryByName() {
        String name = "Test";
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.findByName(name)).thenReturn(categoryDto);
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);

        ResponseEntity<Object> responseEntity = categoryController.findByName(name, "Bearer token");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categoryDto, responseEntity.getBody());
    }

    @Test
    void testFindCategoryByNameUnauthorized() {
        String name = "Test";
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = categoryController.findByName(name, "Bearer token");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Unauthorized", responseEntity.getBody());
    }

    @Test
    void testFindCategoryById() {
        Long id = 1L;
        CategoryDto categoryDto = new CategoryDto();
        when(categoryService.findById(id)).thenReturn(categoryDto);
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);

        ResponseEntity<Object> responseEntity = categoryController.findById(id, "Bearer token");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categoryDto, responseEntity.getBody());
    }

    @Test
    void testFindCategoryByIdUnauthorized() {
        Long id = 1L;
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = categoryController.findById(id, "Bearer token");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Unauthorized", responseEntity.getBody());
    }

    @Test
    void testDeleteCategoryById() {
        Long id = 1L;
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);

        ResponseEntity<Object> responseEntity = categoryController.deleteById(id, "Bearer token");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("DELETE SUCCESS", responseEntity.getBody());
        verify(categoryService, times(1)).deleteById(id);
    }

    @Test
    void testDeleteCategoryByIdUnauthorized() {
        Long id = 1L;
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = categoryController.deleteById(id, "Bearer token");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Unauthorized", responseEntity.getBody());
        verify(categoryService, times(0)).deleteById(id);
    }

    @Test
    void testGetAllCategories() {
        List<CategoryDto> categoryDtoList = Collections.emptyList();
        when(categoryService.getAllCategories()).thenReturn(categoryDtoList);
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);

        ResponseEntity<Object> responseEntity = categoryController.getAllCategories("Bearer token");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categoryDtoList, responseEntity.getBody());
    }

    @Test
    void testGetAllCategoriesUnauthorized() {
        when(authServiceClient.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Object> responseEntity = categoryController.getAllCategories("Bearer token");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Unauthorized", responseEntity.getBody());
    }
}
