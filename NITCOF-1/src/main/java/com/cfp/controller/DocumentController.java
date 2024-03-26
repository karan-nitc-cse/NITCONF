package com.cfp.controller;

import com.cfp.entity.FileEntity;
import com.cfp.entity.User;
import com.cfp.repository.FileRepository;
import com.cfp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Tag(name = "Document Controller", description = "Endpoints for document management")
public class DocumentController {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserService service;

    // Mapping for uploading document
    @GetMapping("/uploadhere/{username}")
    @Operation(summary = "Upload Document Page", description = "Displays the page for uploading documents")
    public Object UploadPage(@PathVariable String username,@NotNull Model model) {
        // Prepare a new FileEntity object and add it to the model for upload form
        FileEntity file = new FileEntity();
        User user = service.getUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("file", file);
        return new ModelAndView("uploadedDoc"); // Return the view for document upload
    }

    // Mapping for processing document upload
    @PostMapping("/uploadDoc")
    @Operation(summary = "Upload Document", description = "Endpoint to upload a document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document uploaded successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ModelAndView UploadedPaper(@ModelAttribute("file") FileEntity file, HttpSession session) {
        // Save the uploaded document
        fileRepository.save(file);
        return new ModelAndView("redirect:/paper"); // Redirect to the dashboard page after upload
    }

}
