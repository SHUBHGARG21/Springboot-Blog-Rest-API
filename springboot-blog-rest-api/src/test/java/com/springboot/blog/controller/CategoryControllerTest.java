package com.springboot.blog.controller;

import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    public void testAddCategory(){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Technology");
        categoryDto.setId(1L);

        when(categoryService.addCategory(categoryDto)).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.addCategory(categoryDto);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(categoryDto,response.getBody());
        verify(categoryService,times(1)).addCategory(categoryDto);
    }

    @Test
    public void testGetCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Technology");

        when(categoryService.getCategory(1L)).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.getCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryDto, response.getBody());
        verify(categoryService, times(1)).getCategory(1L);
    }

    @Test
    public void testGetAllCategories() {
        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setId(1L);
        categoryDto1.setName("Technology");

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setId(2L);
        categoryDto2.setName("Health");

        List<CategoryDto> categories = Arrays.asList(categoryDto1, categoryDto2);

        when(categoryService.getAllCategory()).thenReturn(categories);

        ResponseEntity<List<CategoryDto>> response = categoryController.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categories, response.getBody());
        verify(categoryService, times(1)).getAllCategory();
    }

    @Test
    public void testUpdateCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Updated Category");

        when(categoryService.updateCategory(anyLong(), any(CategoryDto.class))).thenReturn(categoryDto);

        ResponseEntity<CategoryDto> response = categoryController.updateCategory(1L, categoryDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryDto, response.getBody());
        verify(categoryService, times(1)).updateCategory(eq(1L), any(CategoryDto.class));
    }

    @Test
    public void testDeleteCategory() {
        when(categoryService.deleteCategory(1L)).thenReturn("Category Deleted Successfully! ");

        ResponseEntity<String> response = categoryController.deleteCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category Deleted Successfully! ", response.getBody());
        verify(categoryService, times(1)).deleteCategory(1L);
    }



}
