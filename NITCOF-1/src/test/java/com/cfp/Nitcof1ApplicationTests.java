package com.cfp;

import com.cfp.controller.UserController;
import com.cfp.entity.FileEntity;
import com.cfp.entity.User;
import com.cfp.repository.FileRepository;
import com.cfp.repository.UserRepository;
import com.cfp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class Nitcof1ApplicationTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private UserController userController;

    private User currentUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currentUser = new User();
        currentUser.setUsername("testUser");
    }



    @Test
    void testRegisterUser_ValidUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        when(userRepository.findByUsername(any())).thenReturn(null);
        when(userRepository.findByEmail(any())).thenReturn(null);
        when(session.getAttribute(anyString())).thenReturn("message1");

        ModelAndView modelAndView = userController.RegisterAuthor(user, session);

        assertNotNull(modelAndView);
        assertEquals("redirect:/signup", modelAndView.getViewName());
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    void testRegisterUser_ExistingUsername() {
        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.findByUsername(any())).thenReturn(user);
        when(session.getAttribute(any())).thenReturn("message1");

        ModelAndView modelAndView = userController.RegisterAuthor(user, session);

        assertNotNull(modelAndView);
        assertEquals("redirect:/signup", modelAndView.getViewName());
    }



    @Test
    void testLoginAuthor_InvalidCredentials() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("wrongPassword");

        when(userRepository.findByUsername(any())).thenReturn(null);
        when(session.getAttribute(anyString())).thenReturn("message2");

        ModelAndView modelAndView = userController.LoginAuthor(user, null, session);

        assertNotNull(modelAndView);
        assertEquals("redirect:/login", modelAndView.getViewName());
    }




    @Test
    void testUpdateUserDetails() {
        User updatedUser = new User();
        updatedUser.setUsername("testUser");
        updatedUser.setName("Updated Name");
        updatedUser.setPhone("1234567890");
        updatedUser.setEmail("updated@example.com");

        when(userRepository.findByUsername(anyString())).thenReturn(currentUser);

        ModelAndView modelAndView = (ModelAndView) userController.UpdateUserDetails(updatedUser);

        assertNotNull(modelAndView);
        assertEquals("redirect:/profile", modelAndView.getViewName());
        verify(userRepository, times(1)).updateuser(
                eq(updatedUser.getUsername()),
                eq(updatedUser.getName()),
                eq(updatedUser.getPhone()),
                eq(updatedUser.getEmail())
        );
    }

    @Test
    void testGetProfile() {
        Model model = mock(Model.class);

        ModelAndView modelAndView = (ModelAndView) userController.GetProfile(model);

        assertNotNull(modelAndView);
        assertEquals("profile", modelAndView.getViewName());
        verify(model, times(1)).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void testLogout() {
        ModelAndView modelAndView = (ModelAndView) userController.Logout();

        assertNotNull(modelAndView);
        assertEquals("redirect:/", modelAndView.getViewName());
    }
}
