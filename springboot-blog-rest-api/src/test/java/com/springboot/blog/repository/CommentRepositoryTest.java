package com.springboot.blog.repository;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class CommentRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    EntityManager entityManager;

    @Autowired
    CommentRepository commentRepository;

    private Comment comment;
    private Post post;

    @BeforeEach
    public void setUp(){

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE posts").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE comments").executeUpdate();

        post = new Post();
        post.setTitle("Post 1");
        post.setContent("Post 1 cont");
        post.setDescription("Post 1 desc");
        postRepository.save(post);

        comment = new Comment();
        comment.setBody("Comment 1 body");
        comment.setPost(post);
        comment.setName("Comment 1");
        comment.setEmail("shubh@gmail.com");
        commentRepository.save(comment);

    }

    @Test
    public void testSaveComment(){
        Comment comment1=new Comment();
        comment1.setBody("Comment 2 body");
        comment1.setPost(post);
        comment1.setName("Comment 2");
        comment1.setEmail("sg@gmail.com");
        Comment savecom=commentRepository.save(comment1);
        System.out.println(savecom);

        assertThat(savecom.getId()).isNotNull();
        assertThat(savecom.getBody()).isEqualTo("Comment 2 body");
    }

    @Test
    public void testFindById(){
        Optional<Comment> findComment=commentRepository.findById(comment.getId());
        assertThat(findComment.isPresent());
        assertThat(findComment.get().getEmail()).isEqualTo("shubh@gmail.com");
    }

    @Test
    public void testCommentUpdate(){
        comment.setName("Updated Comment 2");
        Comment update=commentRepository.save(comment);
        Comment found=commentRepository.findById(comment.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(comment.getName()).isEqualTo("Updated Comment 2");
    }

    @Test
    public void testDelete(){
        commentRepository.delete(comment);
        Optional<Comment> comm=commentRepository.findById(comment.getId());
        assertThat(comm).isEmpty();
    }


}
