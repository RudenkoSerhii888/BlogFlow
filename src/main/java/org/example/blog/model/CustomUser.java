package org.example.blog.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Логін не може бути порожнім")
    @Size(min = 3, max = 50, message = "Логін від 3 до 50 символів")
    private String login;
    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 4, message = "Пароль мінімум 4 символи")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Email(message = "Невірний формат email")
    private String email;
    private String phone;
    private String address;

    public CustomUser(String login, String password, UserRole role,
                      String email, String phone, String address) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
