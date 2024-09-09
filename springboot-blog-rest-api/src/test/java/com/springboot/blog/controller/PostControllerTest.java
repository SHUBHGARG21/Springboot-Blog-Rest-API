package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
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
public class PostControllerTest {
    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @Test
    public void testCreatePost(){
        PostDto postDto=new PostDto();
        postDto.setId(1L);
        postDto.setTitle("Post");

        when(postService.createPost(any(PostDto.class))).thenReturn(postDto);
        ResponseEntity<PostDto> response = postController.createPost(postDto);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(postDto,response.getBody());
        verify(postService,times(1)).createPost(postDto);
    }

    @Test
    public void testGetAllPost(){
        PostResponse postResponse = new PostResponse();
        postResponse.setPageNo(1);
        postResponse.setPageSize(10);

        when(postService.getAllPosts(1,10,"id","asc")).thenReturn(postResponse);

        PostResponse response = postController.getAllPosts(1,10,"id","asc");
        System.out.println(response);

        assertEquals(postResponse,response);
        verify(postService,times(1)).getAllPosts(1,10,"id","asc");
    }

    @Test
    public void testGetPostById(){
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setTitle("Post");

        when(postService.getPostById(1L)).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.getPostById(1L);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(postDto,response.getBody());
        verify(postService,times(1)).getPostById(1L);
    }

    @Test
    public void testUpdatePost(){
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setTitle("Update Post");

        when(postService.updatePost(any(PostDto.class),eq(1L))).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.updatePost(postDto,1L);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(postDto,response.getBody());
        verify(postService,times(1)).updatePost(postDto,1L);
    }

    @Test
    public void testDeletePost(){
        doNothing().when(postService).deletePostById(1L);

        ResponseEntity<String> response = postController.deletePost(1L);

        assertEquals("Post entity deleted successfully.",response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        verify(postService,times(1)).deletePostById(1L);

    }

    @Test
    public void testPostByCategory(){
        List<PostDto> posts = Arrays.asList(new PostDto(),new PostDto());

        when(postService.getPostsByCategory(1L)).thenReturn(posts);

        ResponseEntity<List<PostDto>> response = postController.getPostByCategory(1L);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(posts,response.getBody());
        verify(postService).getPostsByCategory(1L);
    }
}
