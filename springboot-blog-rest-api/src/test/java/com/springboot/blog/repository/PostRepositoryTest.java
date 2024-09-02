package com.springboot.blog.repository;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import jakarta.persistence.EntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class PostRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    private Post post;
    private Category category;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE category").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE posts").executeUpdate();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

        category=new Category();
        category.setName("JAVA");
        category.setDescription("Java Decs");
        categoryRepository.save(category);

        post =new Post();
        post.setCategory(category);
        post.setTitle("First Test");
        post.setDescription("Test case one");
        post.setContent("Test case ");
        postRepository.save(post);

    }

    @Test
    public void testFindById() {
        Optional<Post> foundPost=postRepository.findById(post.getId());
        if(foundPost.isPresent()){
            System.out.println("categoryId "+foundPost.get().getCategory().getId());
            System.out.println("PostId "+category.getId());
        }
        assertThat(foundPost.isPresent());
        assertThat(foundPost.get().getCategory().getId()).isEqualTo(category.getId());
    }

    @Test
    public void testFindByCategoryId() {
        List<Post> postList=postRepository.findByCategoryId(category.getId());
        postList.forEach((post1 -> System.out.println(post1.getTitle()+post1.getCategory())));
        assertThat(postList).isNotEmpty();
        assertThat(postList.get(0).getCategory().getId());
    }

    @Test
    public void testSavePost() {
        Post post1 =new Post();
        post1.setTitle("First Post");
        post1.setDescription("Second post");
        post1.setContent("First Post one");
        post1.setCategory(category);

        Post p=postRepository.save(post1);
        System.out.println(p);

        assertThat(p.getId()).isNotNull();
        assertThat(p.getTitle()).isEqualTo("First Post");

    }

    @Test
    public void testUpdatePost() {
        post.setTitle("SecondPost");
        Post update = postRepository.save(post);
        assertThat(update.getTitle()).isEqualTo("SecondPost");

    }
    @Test
    public void testDeletePost(){
        postRepository.delete(post);
        Optional<Post> delete = postRepository.findById(post.getId());
        assertThat(delete.isEmpty());
    }

    @Test
    public void testSaveListOfPost(){
        ArrayList<Post> posts = new ArrayList<>();
        for(int i=1;i<4;i++){
            Post post1=new Post();
            post1.setTitle("Post no "+i);
            post1.setDescription("Post Desc "+i);
            post1.setContent("Post cont "+i);
            post1.setCategory(category);
            posts.add(post1);
        }
        List<Post> postList=postRepository.saveAll(posts);
        System.out.println(postList);

        assertThat(postList).hasSize(3);

        for(int i=0;i<postList.size();i++){
            Post found=postList.get(i);
            assertThat(found.getId()).isNotNull();
            assertThat(found.getTitle()).isEqualTo("Post no "+(i+1));
        }
    }


}