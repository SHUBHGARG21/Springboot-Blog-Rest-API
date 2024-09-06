package com.springboot.blog.service;

import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.impl.CategoryServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImpTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private CategoryServiceImp categoryServiceImp;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    public void setUp(){
        category = new Category(1L,"Category","Category Desc",null);
        categoryDto = new CategoryDto(1L,"Category","Category Desc");
    }

    @Test
    public void testAddCategory(){
        when(mapper.map(any(CategoryDto.class),eq(Category.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(mapper.map(any(Category.class),eq(CategoryDto.class))).thenReturn(categoryDto);

        CategoryDto savedCategory = categoryServiceImp.addCategory(categoryDto);

        assertNotNull(savedCategory);
        assertEquals("Category",category.getName());
        verify(categoryRepository,times(1)).save(category);
    }

    @Test
    public void testGetCategoryById(){
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        when(mapper.map(any(Category.class),eq(CategoryDto.class))).thenReturn(categoryDto);

        CategoryDto found= categoryServiceImp.getCategory(1L);

        assertNotNull(found);
        assertEquals("Category Desc",found.getDescription());
        verify(categoryRepository,times(1)).findById(1L);
    }

    @Test
    public void testGetAllCategory(){
        Category category1 = new Category();
        category1.setName("Category 2");
        category1.setDescription("Category Decs 2");
        category1.setId(2L);

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1,category));
        when(mapper.map(any(Category.class),eq(CategoryDto.class)))
                .thenReturn(categoryDto)
                .thenReturn(new CategoryDto(2L,"Category 2","Category Desc 2"));

        List<CategoryDto> categories=categoryServiceImp.getAllCategory();

        assertNotNull(categories);
        assertEquals(2,categories.size());
        assertEquals("Category 2",categories.get(1).getName());
        assertEquals("Category Desc 2", categories.get(1).getDescription());
        verify(categoryRepository,times(1)).findAll();
    }

    @Test
    public void testUpdateCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(mapper.map(any(Category.class), eq(CategoryDto.class))).thenReturn(categoryDto);

        categoryDto.setName("Tech Updated");
        CategoryDto updatedCategory = categoryServiceImp.updateCategory(1L, categoryDto);

        assertNotNull(updatedCategory);
        assertEquals("Tech Updated", updatedCategory.getName());

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testDeleteCategory(){
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        String delete=categoryServiceImp.deleteCategory(1L);

        assertEquals("Category Deleted Successfully! ",delete);
        verify(categoryRepository,times(1)).findById(1L);
        verify(categoryRepository,times(1)).delete(category);
    }

    @Test
    public void testGetCategoryByIdNotFound(){
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()->{
            categoryServiceImp.getCategory(2l);
        });
        verify(categoryRepository,times(1)).findById(2L);
    }

}
