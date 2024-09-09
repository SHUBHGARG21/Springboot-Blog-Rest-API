package com.springboot.blog.service;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
public class PostServiceImpTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;
    private PostDto postDto;
    private Category category;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        category = new Category(1L, "JAVA", "JavaCategory", null);
        post = new Post(1L, "Post Title", "Post Desc", "Post content", null, category);
        postDto = new PostDto(1L, "Post Title", "Post Desc", "Post Content", null, 1L); // To remove the error we will add the @AllArgConstructor in its Dto class
    }

    @Test
    void testCreatePost(){
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(mapper.map(postDto,Post.class)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(mapper.map(post,PostDto.class)).thenReturn(postDto);

        PostDto savePost=postService.createPost(postDto);

        assertNotNull(savePost);
        assertEquals("Post Title",savePost.getTitle());
        verify(postRepository,times(1)).save(post);

    }

    @Test
    void testGetAllPosts(){
        Pageable pageable = (Pageable) PageRequest.of(0,10, Sort.by("id").ascending());
        Page<Post> posts = new PageImpl<>(Arrays.asList(post));
        when(postRepository.findAll(pageable)).thenReturn(posts);
        when(mapper.map(any(Post.class),eq(PostDto.class))).thenReturn(postDto);

        PostResponse postResponse = postService.getAllPosts(0,10,"id","asc");

        assertNotNull(postResponse);
        assertEquals(1,postResponse.getContent().size());
        verify(postRepository,times(1)).findAll(pageable);

    }

    @Test
    void testGetPostById(){
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(mapper.map(post,PostDto.class)).thenReturn(postDto);

        PostDto found=postService.getPostById(1L);

        assertNotNull(found);
        assertEquals("Post Title",found.getTitle());
        verify(postRepository,times(1)).findById(1L);
    }

    @Test
    void testUpdatePost(){
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(postRepository.save(post)).thenReturn(post);
        when(mapper.map(post,PostDto.class)).thenReturn(postDto);

        PostDto updatePost=postService.updatePost(postDto,1L);

        assertNotNull(updatePost);
        assertEquals("Post Title",updatePost.getTitle());
        verify(postRepository,times(1)).save(post);

    }

    @Test
    void testDeletePostById(){
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        postService.deletePostById(1L);
        verify(postRepository,times(1)).delete(post);
    }

    @Test
    void testGetPostsByCategory(){
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(postRepository.findByCategoryId(1L)).thenReturn(Arrays.asList(post));
        when(mapper.map(post,PostDto.class)).thenReturn(postDto);

        List<PostDto> posts=postService.getPostsByCategory(1L);

        assertNotNull(posts);
        assertEquals(1,posts.size());
        verify(postRepository,times(1)).findByCategoryId(1L);
    }

    @Test
    void testGetPostById_NotFound(){
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()-> postService.getPostById(1L));
    }

    @Test
    void testUpdatePost_NotFound(){
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()-> postService.updatePost(postDto,1L));
    }

    @Test
    void testDeletePostById_NotFound(){
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,()-> postService.deletePostById(2L));

    }
}
