package org.example.blog.controller;

import org.example.blog.model.CustomUser;
import org.example.blog.model.UserRole;
import org.example.blog.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        User user = getCurrentUser();
        CustomUser dbUser = userService.findByLogin(user.getUsername());

        model.addAttribute("login", user.getUsername());
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("admin", isAdmin(user));
        model.addAttribute("email", dbUser.getEmail());
        model.addAttribute("phone", dbUser.getPhone());
        model.addAttribute("address", dbUser.getAddress());

        return "profile";
    }

    @PostMapping("/update")
    public String update(@RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone) {
        User user = getCurrentUser();
        userService.updateUser(user.getUsername(), email, phone);
        return "redirect:/profile";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/newuser")
    public String registerUser(@RequestParam String login,
                               @RequestParam String password,
                               @RequestParam(required = false) String email,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String address,
                               Model model) {

        // Ручна валідація
        if (login == null || login.trim().length() < 3) {
            model.addAttribute("loginError", "Логін мінімум 3 символи");
            return "register";
        }
        if (password == null || password.trim().length() < 4) {
            model.addAttribute("passwordError", "Пароль мінімум 4 символи");
            return "register";
        }
        if (email != null && !email.trim().isEmpty()) {
            if (!email.contains("@")) {
                model.addAttribute("emailError", "Невірний формат email");
                model.addAttribute("login", login);
                return "register";
            }
        }

        String passHash = passwordEncoder.encode(password);

        if (!userService.addUser(login, passHash, UserRole.USER, email, phone, address)) {
            model.addAttribute("exists", true);
            model.addAttribute("login", login);
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUsers(@RequestParam(name = "toDelete[]", required = false) List<Long> ids,
                              Model model) {
        if (ids != null) {
            userService.deleteUsers(ids);
        }
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/unauthorized")
    public String unauthorized(Model model) {
        User user = getCurrentUser();
        model.addAttribute("login", user.getUsername());
        return "unauthorized";
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private boolean isAdmin(User user) {
        Collection<GrantedAuthority> roles = user.getAuthorities();
        for (GrantedAuthority auth : roles) {
            if ("ROLE_ADMIN".equals(auth.getAuthority()))
                return true;
        }
        return false;
    }
}