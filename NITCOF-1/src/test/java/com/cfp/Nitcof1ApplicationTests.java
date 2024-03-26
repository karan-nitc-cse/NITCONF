package com.cfp;

import com.cfp.controller.UserController;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cfp.entity.User;
import com.cfp.repository.UserRepository;
import com.cfp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@SpringBootTest
public class Nitcof1ApplicationTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testRegisterAuthor() {
        User user = new User();
        user.setUsername("testUser");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        ModelAndView modelAndView = userController.RegisterAuthor(user, session);
        verify(session).setAttribute("message1", "Registration successful");
        assertEquals("redirect:/signup", modelAndView.getViewName());
    }

    @Test
    public void testLoginAuthor() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(model.addAttribute("user", user)).thenReturn(model);
        ModelAndView modelAndView = userController.LoginAuthor(user, model, session);
        assertEquals("redirect:/dashboard", modelAndView.getViewName());
    }

    // Add tests for other methods similarly
}



