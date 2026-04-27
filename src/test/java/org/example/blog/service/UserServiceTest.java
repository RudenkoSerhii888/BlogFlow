package org.example.blog.service;

import org.example.blog.model.CustomUser;
import org.example.blog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findByLogin_ShouldReturnUser_WhenExists() {

        CustomUser user = new CustomUser();
        user.setLogin("testuser");
        when(userRepository.findByLogin("testuser")).thenReturn(user);


        CustomUser result = userService.findByLogin("testuser");


        assertNotNull(result);
        assertEquals("testuser", result.getLogin());
    }

    @Test
    void findByLogin_ShouldReturnNull_WhenNotFound() {

        when(userRepository.findByLogin("nobody")).thenReturn(null);


        CustomUser result = userService.findByLogin("nobody");


        assertNull(result);
    }
}