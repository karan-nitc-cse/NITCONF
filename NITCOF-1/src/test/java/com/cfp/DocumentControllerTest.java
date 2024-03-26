package com.cfp;

import com.cfp.controller.DocumentController;
import com.cfp.entity.FileEntity;
import com.cfp.repository.FileRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DocumentControllerTest {

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private DocumentController documentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testUploadPage() {
//        Model model = mock(Model.class);
//        Object result = documentController.UploadPage(model);
//        assertEquals(ModelAndView.class, result.getClass());
//        assertEquals("uploadedDoc", ((ModelAndView) result).getViewName());
//        verify(model).addAttribute("file", new FileEntity());
//    }

    @Test
    void testUploadedPaper() {
        HttpSession session = mock(HttpSession.class);
        FileEntity file = new FileEntity();
        ModelAndView modelAndView = documentController.UploadedPaper(file, session);
        assertEquals("redirect:/dashboard", modelAndView.getViewName());
        verify(fileRepository).save(file);
    }
}
