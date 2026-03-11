package org.example.blog.controller;

import org.example.blog.model.Topic;
import org.example.blog.service.TopicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping
    public String topics(Model model) {
        model.addAttribute("topics", topicService.findAll());
        model.addAttribute("newTopic", new Topic());
        return "topics";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String add(@RequestParam String title) {
        topicService.save(new Topic(title));
        return "redirect:/topics";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable Long id) {
        topicService.deleteById(id);
        return "redirect:/topics";
    }

    @PostMapping("/removeAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String removeAll() {
        topicService.deleteAll();
        return "redirect:/topics";
    }

    @PostMapping("/search")
    public String search(@RequestParam String searchTerm, Model model) {
        model.addAttribute("topics", topicService.search(searchTerm));
        model.addAttribute("newTopic", new Topic());
        model.addAttribute("searchTerm", searchTerm);
        return "topics";
    }
}