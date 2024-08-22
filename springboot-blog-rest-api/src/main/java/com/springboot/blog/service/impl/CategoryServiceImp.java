package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {

    public CategoryServiceImp(CategoryRepository categoryRepository,ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper=modelMapper;
    }

    CategoryRepository categoryRepository;
    ModelMapper modelMapper;


    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto,Category.class);
        Category categorySave = categoryRepository.save(category);
        return modelMapper.map(categorySave,CategoryDto.class);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        Category category=categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category","id",id));
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category->modelMapper.map(category,CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category","id",id));
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        Category categoryUpdate=categoryRepository.save(category);
        return modelMapper.map(categoryUpdate,CategoryDto.class);

    }

    @Override
    public String deleteCategory(Long id) {
        Category category=categoryRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Category","id",id));
        categoryRepository.delete(category);
        return "Category Deleted Successfully! ";
    }
}
