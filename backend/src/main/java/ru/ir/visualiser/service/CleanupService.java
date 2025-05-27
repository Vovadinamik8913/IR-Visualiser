package ru.ir.visualiser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.ir.visualiser.config.LocalConfig;
import ru.ir.visualiser.files.FileWorker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class CleanupService {
    private final IrService irService;
    private final ProjectService projectService;
    private final LocalConfig config;

    @Scheduled(fixedDelay = 5000)
    public void cleanupIRFiles() {
        irService.getAll().forEach(ir -> {
           Path irPath = Path.of(ir.getIrPath() + File.separator + ir.getFilename());
           if (!Files.exists(irPath)) {
               try {
                   irService.deleteById(ir.getId());
               } catch (IOException ignored) {
               }
           }
        });
        projectService.getAll().forEach(project -> {
            Path projectPath = Path.of(FileWorker.absolutePath(config,File.separator + project.getName()));
            if (!Files.exists(projectPath)) {
                projectService.delete(project.getId());
            }
        });
    }
}
