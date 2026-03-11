package org.example.blog.repository;

import org.example.blog.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByTitleContainingIgnoreCase(String title);
}