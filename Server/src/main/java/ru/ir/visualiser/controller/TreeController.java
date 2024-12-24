package ru.ir.visualiser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ir.visualiser.files.Config;
import ru.ir.visualiser.files.FileWorker;
import ru.ir.visualiser.files.llvm.Opt;
import ru.ir.visualiser.files.model.Ir;
import ru.ir.visualiser.files.model.IrService;
import ru.ir.visualiser.parser.ModuleIR;
import ru.ir.visualiser.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@RestController
@RequiredArgsConstructor
@RequestMapping("/optimization/tree")
public class TreeController {
    private final IrService irService;

    @Operation(summary = "Использование оптимизаций")
    @PostMapping(value = "/add")
    public ResponseEntity<Long> optimizeFile(
            @Parameter(description = "Parent", required = true) @RequestParam("parent") Long file,
            @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt,
            @Parameter(description = "Optimization", required = true) @RequestParam("optimization") String optimization
    ) {
        Ir parent = irService.get(file);
        Ir child = new Ir(parent, optimization);
        FileWorker.createPaths(child.getIrPath(),
                new String[]{
                        "dot_files",
                        "svg_files"
                }
        );
        String optPath = Config.getInstance().getOptsPath()[opt];
        try {
            Opt.optimizeOpt(optPath, parent, child);
            File res = new File(child.getIrPath() + File.separator + child.getFilename());
            irService.create(child, null);
            return ResponseEntity.ok(child.getId());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "Использование оптимизаций")
    @PostMapping(value = "/delete")
    public void deleteBranch(
            @Parameter(description = "Parent", required = true) @RequestParam("parent") Long file
    ) {
        irService.deleteById(file);
    }
}
