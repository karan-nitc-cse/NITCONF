package com.cfp;

import com.cfp.controller.HomeController;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testRegistrationPage() {
        Model model = mock(Model.class);
        Object result = userController.RegistrationPage(model);
        assertEquals(ModelAndView.class, result.getClass());
        assertEquals("register", ((ModelAndView) result).getViewName());
    }

    @Test
    void testRegisterAuthor() {
        HttpSession session = mock(HttpSession.class);
        User user = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(null);
        ModelAndView modelAndView = userController.RegisterAuthor(user, session);
        assertEquals("redirect:/signup", modelAndView.getViewName());
    }

    @Test
    void testLoginPage() {
        ModelAndView modelAndView = userController.LoginPage();
        assertEquals("login", modelAndView.getViewName());
    }

    @Test
    void testLoginAuthor() {
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        when(userRepository.findByUsername("username")).thenReturn(user);
        ModelAndView modelAndView = userController.LoginAuthor(user, model, session);
        assertEquals("redirect:/dashboard", modelAndView.getViewName());
    }
    @Test
    void testExistingUser() {
        String existingUsername = "existingUser";
        User existingUser = new User();
        existingUser.setUsername(existingUsername);

        // Mocking UserRepository behavior to return an existing user
        when(userRepository.findByUsername(existingUsername)).thenReturn(existingUser);

        // Calling the method under test
        ModelAndView modelAndView = userController.LoginPage();

        // Verifying the behavior
        assertEquals("login", modelAndView.getViewName());
    }
    // Test case to check for an existing user before registration
    @Test
    void testExistingUserBeforeRegistration() {
        String existingUsername = "existingUser";
        User existingUser = new User();
        existingUser.setUsername(existingUsername);

        // Mocking UserRepository behavior to return an existing user
        when(userRepository.findByUsername(existingUsername)).thenReturn(existingUser);

        // Creating a new user with an existing username
        User newUser = new User();
        newUser.setUsername(existingUsername);

        HttpSession session = mock(HttpSession.class);

        // Calling the method under test
        ModelAndView modelAndView = userController.RegisterAuthor(newUser, session);

        // Verifying the behavior
        assertEquals("redirect:/signup", modelAndView.getViewName());
    }
    @Test
    void testDashboard() {
        String currentUserName = "username";
        List<FileEntity> uploadedFiles = mock(List.class);
        when(fileRepository.findByuserid(currentUserName)).thenReturn(uploadedFiles);
        ModelAndView modelAndView = userController.Dashboard();
        assertEquals("speaker_Dashboard", modelAndView.getViewName());
    }

    @Test
    void testEdit() {
        String username = "username";
        Model model = mock(Model.class);
        User user = mock(User.class);
        when(userService.getUserByUsername(username)).thenReturn(user);
        Object result = userController.Edit(username, model);
        assertEquals(ModelAndView.class, result.getClass());
        assertEquals("edit_user", ((ModelAndView) result).getViewName());
    }

    @Test
    void testUpdateUserDetails() {
        User updatedUser = mock(User.class);
        ModelAndView modelAndView = (ModelAndView) userController.UpdateUserDetails(updatedUser);
        assertEquals("redirect:/profile", modelAndView.getViewName());
    }

    @Test
    void testGetProfile() {
        Model model = mock(Model.class);
        Object result = userController.GetProfile(model);
        assertEquals(ModelAndView.class, result.getClass());
        assertEquals("profile", ((ModelAndView) result).getViewName());
    }

    @Test
    void testLogout() {
        Object result = HomeController.HomePage();
        assertEquals(ModelAndView.class, result.getClass());
        assertEquals("index", ((ModelAndView) result).getViewName());
    }
}