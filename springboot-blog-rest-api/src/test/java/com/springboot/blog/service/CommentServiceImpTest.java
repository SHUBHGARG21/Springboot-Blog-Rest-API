package com.springboot.blog.service;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class
CommentServiceImpTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private CommentServiceImpl commentServiceImpl;

    private Post post;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp(){
        post = new Post(1L,"Post 1","Post Desc","Post content",null,null);
        comment =new Comment(1l,"Comment 1","shubh@gmail.com","Comment Body",post);
        commentDto=new CommentDto(1L,"Comment 1","shubh@gmail.com","Comment Body"); // To remove the error we will add the @AllArgConstructor in its Dto class
    }

    @Test
    public void testCreateComment(){
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(mapper.map(any(CommentDto.class),eq(Comment.class))).thenReturn(comment);
        when(mapper.map(any(Comment.class),eq(CommentDto.class))).thenReturn(commentDto);

        CommentDto saveComment=commentServiceImpl.createComment(1L,commentDto);

        assertNotNull(saveComment);
        assertEquals("Comment 1",saveComment.getName());
        verify(postRepository,times(1)).findById(1L);
        verify(commentRepository,times(1)).save(comment);
    }

    @Test
    public void testGetCommentByPostId(){
        when(commentRepository.findByPostId(1L)).thenReturn(Arrays.asList(comment));
        when(mapper.map(any(Comment.class),eq(CommentDto.class))).thenReturn(commentDto);

        List<CommentDto> comments = commentServiceImpl.getCommentsByPostId(1L);

        assertNotNull(comments);
        assertEquals(1,comments.size());
        verify(commentRepository,times(1)).findByPostId(1L);
    }

    @Test
    public void testGetCommentById_ThrowsException_WhenCommentDoesNotBelongToPost(){
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        Post diffPost=new Post();
        diffPost.setId(2L);
        comment.setPost(diffPost);

        BlogAPIException exception = assertThrows(BlogAPIException.class,()->{
            commentServiceImpl.getCommentById(1L,1L);
        });

        assertEquals("Comment does not belong to post",exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());

    }

    @Test
    public void testUpdateComment(){
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(mapper.map(any(Comment.class),eq(CommentDto.class))).thenReturn(commentDto);

        CommentDto updateComment=commentServiceImpl.updateComment(1L,1L,commentDto);

        assertNotNull(updateComment);
        assertEquals("shubh@gmail.com",commentDto.getEmail());
        verify(commentRepository,times(1)).save(any(Comment.class));
    }

    @Test
    public void testDeleteComment(){
        when(postRepository.findById(any(Long.class))).thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));

        commentServiceImpl.deleteComment(1L,1L);

        verify(commentRepository,times(1)).delete(comment);
    }





}
