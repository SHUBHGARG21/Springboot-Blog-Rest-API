package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
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
public class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @Test
    public void testCreateComment(){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setName("Comment");

        when(commentService.createComment(anyLong(),any(CommentDto.class))).thenReturn(commentDto);

        ResponseEntity<CommentDto> response = commentController.createComment(1L,commentDto);

        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(commentDto,response.getBody());
        verify(commentService,times(1)).createComment(1L,commentDto);
    }

    @Test
    public void testGetCommentByPostId(){
        CommentDto commentDto1 = new CommentDto();
        commentDto1.setId(1L);
        commentDto1.setName("Comment 1");

        CommentDto commentDto2 = new CommentDto();
        commentDto2.setId(2L);
        commentDto2.setName("Comment 2");

        List<CommentDto> comments = Arrays.asList(commentDto1,commentDto2);

//        when(commentService.getCommentsByPostId(anyLong())).thenReturn(comments);
        when(commentService.getCommentsByPostId(1L)).thenReturn(comments);

        List<CommentDto> response=commentController.getCommentsByPostId(1L);

        assertEquals(response,response);
        verify(commentService).getCommentsByPostId(1L);
    }

    @Test
    public void testGetCommentById(){
        CommentDto commentDto1 = new CommentDto();
        commentDto1.setId(1L);
        commentDto1.setName("Comment 1");

        when(commentService.getCommentById(anyLong(),anyLong())).thenReturn(commentDto1);

        ResponseEntity<CommentDto> response = commentController.getCommentById(1L,1L);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(commentDto1,response.getBody());
        verify(commentService).getCommentById(1L,1L);
    }

    @Test
    public void testUpdateComment(){
        CommentDto commentDto1 = new CommentDto();
        commentDto1.setId(1L);
        commentDto1.setName("Updated Comment");

        when(commentService.updateComment(1L,1L,commentDto1)).thenReturn(commentDto1);

        ResponseEntity<CommentDto> response = commentController.updateComment(1L,1L,commentDto1);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(commentDto1,response.getBody());
        verify(commentService).updateComment(1L,1L,commentDto1);
    }

    @Test
    public void testDeleteComment(){
        doNothing().when(commentService).deleteComment(1L,1L);

        ResponseEntity<String> response = commentController.deleteComment(1L,1L);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("Comment deleted successfully",response.getBody());
        verify(commentService,times(1)).deleteComment(1L,1L);

    }

}
