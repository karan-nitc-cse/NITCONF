package com.cfp.controller;

import com.cfp.entity.FileEntity;
import com.cfp.entity.User;
import com.cfp.repository.FileRepository;
import com.cfp.repository.UserRepository;
import com.cfp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "UserController", description = "User Operations")
public class UserController {

    User currentuser = new User();

    @Autowired
    private UserRepository repo;

    @Autowired
    private UserService service;

    @Autowired
    private FileRepository fileRepository;



    // User Registration Page
    @GetMapping("/signup")
    @Operation(summary = "User Registration Page", description = "Displays the registration page for users.")
    public Object RegistrationPage(@NotNull Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return new ModelAndView("register");
    }

    // Register User
    @PostMapping("/registerUser")
    @Operation(summary = "Register User", description = "Registers a new user.")
    @ApiResponse(responseCode = "200", description = "Registration successful")
    @ApiResponse(responseCode = "400", description = "Username already exists")
    public ModelAndView RegisterAuthor(@ModelAttribute("user") @NotNull User user, HttpSession session) {
        User existingUser = repo.findByUsername(user.getUsername());
        User existingEmail = repo.findByEmail(user.getEmail());
        if (existingUser != null) {
            session.setAttribute("message1", "Username already exists");
            return new ModelAndView("redirect:/signup");
        } else {
            service.registerUser(user);
            session.setAttribute("message1", "Registration successful");
            return new ModelAndView("redirect:/signup");
        }
    }

    // Login Page
    @GetMapping("/login")
    @Operation(summary = "Login Page", description = "Displays the login page.")
    public ModelAndView LoginPage() {
        return new ModelAndView("login", "user", new User());
    }

    // User Login
    @PostMapping("/signing")
    @Operation(summary = "User Login", description = "Authenticates a user.")
    @ApiResponse(responseCode = "200", description = "User authenticated successfully")
    @ApiResponse(responseCode = "302", description = "User not found")
    public ModelAndView LoginAuthor(@ModelAttribute("user") @NotNull User user, Model model, HttpSession session) {
        String userID = user.getUsername();
        Optional<User> userData = Optional.ofNullable(repo.findByUsername(userID));

        if (userData.isPresent() && user.getPassword().equals(userData.get().getPassword())) {
            model.addAttribute("user", userData.get());
            currentuser = userData.get();
            return new ModelAndView("redirect:/dashboard");
        } else {
            session.setAttribute("message2", "User not found");
            return new ModelAndView("redirect:/login");
        }
    }

    // User Dashboard
    @GetMapping("/dashboard")
    @Operation(summary = "User Dashboard", description = "Displays the user dashboard.")
    public ModelAndView Dashboard() {
        String currentUserName = currentuser.getUsername();
        List<FileEntity> uploadedFiles = fileRepository.findByuserid(currentUserName);
        ModelAndView modelAndView = new ModelAndView("speaker_Dashboard");
        modelAndView.addObject("uploadedFiles", uploadedFiles);
        modelAndView.addObject("user", currentuser);
        return modelAndView;
    }
    //mapping for uploaded_paper
    @GetMapping("/paper")
    public Object Papers() {
        String currentUserName = currentuser.getUsername();
        List<FileEntity> uploadedFiles = fileRepository.findByuserid(currentUserName);
        ModelAndView modelAndView = new ModelAndView("uploaded_paper");
        modelAndView.addObject("uploadedFiles", uploadedFiles);
        modelAndView.addObject("user", currentuser);
        return modelAndView;
    }

    // Edit User Profile
    @GetMapping("/updateProfile/{username}")
    @Operation(summary = "Edit User Profile", description = "Displays the form to edit user profile.")
    public Object Edit(@PathVariable String username, @NotNull Model model) {
        User user = service.getUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("edit_user");
    }

    // Update User Details
    @Transactional
    @PostMapping("/updateuserDetails")
    @Operation(summary = "Update User Details", description = "Updates the user profile details.")
    public Object UpdateUserDetails(@ModelAttribute @NotNull User updatedUser) {
        repo.updateuser(updatedUser.getUsername(), updatedUser.getName(), updatedUser.getPhone(), updatedUser.getEmail());
        Optional<User> userData = Optional.ofNullable(repo.findByUsername(updatedUser.getUsername()));
        userData.ifPresent(user -> currentuser = user);
        return new ModelAndView("redirect:/profile");
    }

    // User Profile
    @GetMapping("/profile")
    @Operation(summary = "User Profile", description = "Displays the user profile.")
    public Object GetProfile(@NotNull Model model) {
        model.addAttribute("user", currentuser);
        return new ModelAndView("profile");
    }

    // Logout
    @GetMapping("/logout")
    @Operation(summary = "Logout", description = "Logs out the user.")
    public Object Logout() {
        return new ModelAndView("redirect:/");
    }
}
