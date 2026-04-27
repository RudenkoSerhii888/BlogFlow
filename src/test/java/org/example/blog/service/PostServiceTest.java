package org.example.blog.service;

import org.example.blog.model.Post;
import org.example.blog.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void getPostById_ShouldReturnPost_WhenExists() {

        Post post = new Post("Заголовок", "Анонс", "Контент");
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));


        Optional<Post> result = postService.getPostById(1L);


        assertTrue(result.isPresent());
        assertEquals("Заголовок", result.get().getTitle());
    }

    @Test
    void getPostById_ShouldReturnEmpty_WhenNotFound() {

        when(postRepository.findById(99L)).thenReturn(Optional.empty());


        Optional<Post> result = postService.getPostById(99L);


        assertFalse(result.isPresent());
    }

    @Test
    void savePost_ShouldCallRepository() {

        Post post = new Post("Заголовок", "Анонс", "Контент");


        postService.savePost(post);


        verify(postRepository, times(1)).save(post);
    }
}