package org.example.blog.controller;

import org.example.blog.exception.ResourceNotFoundException;
import org.example.blog.model.CustomUser;
import org.example.blog.model.Post;
import org.example.blog.service.CommentService;
import org.example.blog.service.PostService;
import org.example.blog.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
public class BlogController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    public BlogController(PostService postService, UserService userService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/blog")
    public String blogMain(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Post> postsPage = postService.getPostsPaginated(page, 5);
        model.addAttribute("posts", postsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postsPage.getTotalPages());
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAddPage() {
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogAdd(@RequestParam String title,
                          @RequestParam String anons,
                          @RequestParam String content,
                          Model model) {


        if (title == null || title.trim().isEmpty()) {
            model.addAttribute("titleError", "Заголовок не може бути порожнім");
            return "blog-add";
        }
        if (anons == null || anons.trim().isEmpty()) {
            model.addAttribute("anonError", "Анонс не може бути порожнім");
            return "blog-add";
        }
        if (content == null || content.trim().isEmpty()) {
            model.addAttribute("contentError", "Текст статті не може бути порожнім");
            return "blog-add";
        }

        User user = getCurrentUser();
        CustomUser author = userService.findByLogin(user.getUsername());
        Post post = new Post(title, anons, content);
        post.setAuthor(author);
        postService.savePost(post);

        return "redirect:/blog";
    }


    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Стаття з id " + id + " не знайдена"));


        postService.incrementViews(id);

        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.getCommentsByPost(post));

        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (principal instanceof User currentUserDetails) {
            CustomUser currentUser = userService.findByLogin(
                    currentUserDetails.getUsername());
            model.addAttribute("currentUser", currentUser);
        } else {
            model.addAttribute("currentUser", null);
        }

        return "blog-details";
    }

    @PostMapping("/blog/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             Model model) {
        if (content == null || content.trim().isEmpty()) {
            return "redirect:/blog/" + id;
        }

        Post post = postService.getPostById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Стаття з id " + id + " не знайдена"));

        User user = getCurrentUser();
        CustomUser author = userService.findByLogin(user.getUsername());

        commentService.addComment(content, author, post);

        return "redirect:/blog/" + id;
    }

    @PostMapping("/blog/{postId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId) {
        commentService.findById(commentId).ifPresent(comment -> {
            User user = getCurrentUser();
            CustomUser currentUser = userService.findByLogin(user.getUsername());


            boolean isAdmin = user.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isAuthor = comment.getAuthor().getId().equals(currentUser.getId());

            if (isAdmin || isAuthor) {
                commentService.deleteComment(commentId);
            }
        });

        return "redirect:/blog/" + postId;
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEditPage(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Стаття з id " + id + " не знайдена"));

        model.addAttribute("post", post);

        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogUpdate(@PathVariable Long id,
                             @RequestParam String title,
                             @RequestParam String anons,
                             @RequestParam String content) {
        Post post = postService.getPostById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setContent(content);
        postService.savePost(post);

        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogDelete(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/blog";
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}