package com.springboot.blog.repository;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import jakarta.persistence.EntityManager;
import org.hibernate.metamodel.model.domain.internal.EntityTypeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PostRepository postRepository;


    @Autowired
    EntityManager entityManager;

    private Category category;
    private Post post;

    @BeforeEach
    public void setUp() {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE category").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE posts").executeUpdate();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

        category = new Category();
        category.setName("JAVA");
        category.setDescription("Java desc");
        categoryRepository.save(category);

        post = new Post();
        post.setCategory(category);
        post.setTitle("Post 1");
        post.setDescription("Post 1 desc");
        post.setContent("Post 1 content");
        postRepository.save(post);

        // Flush and clear the persistence context
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testSaveCategory(){
        Category newcat=categoryRepository.save(category);
        assertThat(newcat.getName()).isEqualTo("JAVA");
        assertThat(newcat.getId()).isGreaterThan(0);
        assertThat(newcat.getDescription()).isEqualTo("Java desc");
    }

    @Test
    public void testFindById(){
        Optional<Category> findid=categoryRepository.findById(category.getId());
        assertThat(findid).isPresent();
        assertThat(findid.get().getName()).isEqualTo(category.getName());
    }

    @Test
    public void testUpdateCategory(){
        category.setName("Updated Java category");
        Category update=categoryRepository.save(category);
        System.out.println(category);
        assertThat(update.getName()).isEqualTo(category.getName());
    }

    @Test
    public void testFindALl(){
        List<Category> categoryList=categoryRepository.findAll();
        assertThat(categoryList).isNotEmpty();
        assertThat(categoryList.get(0).getName()).isEqualTo("JAVA");
    }

    @Test
    public void testDeleteCategory(){
        categoryRepository.delete(category);
        Optional<Category> delete = categoryRepository.findById(category.getId());
        assertThat(delete).isEmpty();
    }

    @Test
    public void testCategoryWithPost() {
        Optional<Category> savedCategory = categoryRepository.findById(category.getId());
        assertThat(savedCategory).isNotEmpty();
        Category category1 = savedCategory.get();

        assertThat(category1.getPost()).isNotEmpty();
        assertThat(category1.getPost()).isNotNull();
        assertThat(category1.getPost().get(0).getTitle()).isEqualTo("Post 1");
    }

}
