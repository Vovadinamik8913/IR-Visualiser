package ru.ir.visualiser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class CleanupService {
    private final IrService irService;

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
    }
}
