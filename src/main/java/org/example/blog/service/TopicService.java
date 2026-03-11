package org.example.blog.service;

import org.example.blog.model.Topic;
import org.example.blog.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Transactional(readOnly = true)
    public List<Topic> findAll() {
        return topicRepository.findAll();
    }

    @Transactional
    public void save(Topic topic) {
        topicRepository.save(topic);
    }

    @Transactional
    public void deleteById(Long id) {
        topicRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        topicRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<Topic> search(String searchTerm) {
        return topicRepository.findByTitleContainingIgnoreCase(searchTerm);
    }
}