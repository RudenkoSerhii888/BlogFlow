package org.example.blog.repository;

import org.example.blog.model.Post;
import org.example.blog.model.CustomUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthor(CustomUser author);
    Page<Post> findAll(Pageable pageable);
}