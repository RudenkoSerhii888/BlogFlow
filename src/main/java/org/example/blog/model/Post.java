package org.example.blog.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Заголовок не може бути порожнім")
    @Size(max = 200, message = "Заголовок максимум 200 символів")
    private String title;

    @NotBlank(message = "Анонс не може бути порожнім")
    @Size(max = 500, message = "Анонс максимум 500 символів")
    private String anons;

    @NotBlank(message = "Текст статті не може бути порожнім")
    @Column(columnDefinition = "TEXT")
    private String content;

    private int views;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private CustomUser author;

    public Post(String title, String anons, String content) {
        this.title = title;
        this.anons = anons;
        this.content = content;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}