package org.example.blog.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Data
@NoArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Назва теми не може бути порожньою")
    @Size(max = 200, message = "Назва теми максимум 200 символів")
    private String title;

    public Topic(String title) {
        this.title = title;
    }
}