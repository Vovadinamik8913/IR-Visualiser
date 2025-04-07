package ru.ir.visualiser.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import ru.ir.visualiser.config.Config;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.model.Project;
import ru.ir.visualiser.service.IrService;
import ru.ir.visualiser.service.ModuleService;
import ru.ir.visualiser.service.ProjectService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuilderControllerTest {

    @Mock
    private IrService irService;

    @Mock
    private ModuleService moduleService;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private BuilderController builderController;

    private Project testProject;
    private Ir testIr;

    @BeforeEach
    void setUp() {
        testProject = new Project("testFolder");
        testIr = new Ir("test.ll", "/test/path", "/test/path/svg", "/test/path/dot", testProject);
        testIr.setId(1L);
    }

    @Test
    void saveByFile_Success() {
        // Arrange
        String folder = "testFolder";
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.ll",
            "text/plain",
            "test content".getBytes()
        );

        when(projectService.findByName(folder)).thenReturn(testProject);
        when(irService.create(any(Ir.class))).thenReturn(testIr);

        // Act
        ResponseEntity<Long> response = builderController.saveByFile(folder, file);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(1L, response.getBody());
    }

    @Test
    void saveByPath_Success() {
        // Arrange
        String folder = "testFolder";
        String filePath = getClass().getClassLoader().getResource("test1/test.ll").getPath();

        when(projectService.findByName(folder)).thenReturn(testProject);
        when(irService.create(any(Ir.class))).thenReturn(testIr);

        // Act
        ResponseEntity<Long> response = builderController.saveByPath(folder, filePath);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(1L, response.getBody());
    }

    @Test
    void buildSVGByFile_NotFound() {
        // Arrange
        Long id = 1L;
        int opt = 0;

        when(irService.get(id)).thenReturn(null);

        // Act
        ResponseEntity<?> response = builderController.buildSVGByFile(id, opt);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
    }

    @Test
    void saveByFile_WithNewProject() {
        // Arrange
        String folder = "newFolder";
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.ll",
            "text/plain",
            "test content".getBytes()
        );

        when(projectService.findByName(folder)).thenReturn(null);
        when(irService.create(any(Ir.class))).thenReturn(testIr);

        // Act
        ResponseEntity<Long> response = builderController.saveByFile(folder, file);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(1L, response.getBody());
    }
}
