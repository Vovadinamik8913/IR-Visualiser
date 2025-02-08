package ru.ir.visualiser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ru.ir.visualiser.files.Config;
import ru.ir.visualiser.files.FileWorker;
import ru.ir.visualiser.logic.llvm.Opt;
import ru.ir.visualiser.logic.llvm.Svg;
import ru.ir.visualiser.model.classes.Ir;
import ru.ir.visualiser.model.service.IrService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import ru.ir.visualiser.model.classes.ir.ModuleIR;
import ru.ir.visualiser.model.service.ModuleService;
import ru.ir.visualiser.logic.parser.Parser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class BuilderController {
    private final IrService irService;
    private final ModuleService moduleService;

    private void generate(int opt, Ir ir) throws IOException {
        String optPath = Config.getInstance().getOptsPath()[opt];
        if (Opt.validateOpt(optPath)) {
            if (Opt.generateDotFiles(
                    optPath,
                    ir.getDotPath(),
                    ir.getFilename()
            )) {
                Svg.generateSvgFiles(
                        ir.getDotPath(),
                        ir.getSvgPath()
                );
            }
        }
    }

    private Ir create(String folder, String filename, byte[] content) throws IOException {
        String folderName = FileWorker.getFolderName(filename);
        String path = FileWorker.absolutePath(folder + File.separator + folderName);


        Ir ir = new Ir(filename,
                path,
                path + File.separator + "svg_files",
                path + File.separator + "dot_files");

        FileWorker.createPath(ir.getSvgPath());
        FileWorker.createPath(ir.getDotPath());
        FileWorker.copy(ir.getIrPath(),
                filename,
                content
        );

        String moduleContent = new String(content, StandardCharsets.UTF_8);
        DirectoryStream<Path> dotsDirectory = Files.newDirectoryStream(Path.of(ir.getDotPath()));
        List<String> dots = new java.util.ArrayList<>();
        for (Path dotPath : dotsDirectory) {
            dots.add(Files.readString(dotPath));
        }

        ir = irService.create(ir);
        ModuleIR module = Parser.parseModule(moduleContent, dots);
        module.setIr(ir);
        moduleService.create(module);

        return ir;
    }

    @Operation(summary = "Сохранение файла по пути до него")
    @PostMapping(value = "/save/path")
    public ResponseEntity<Long> saveByPath(
            @Parameter(description = "Folder", required = true) @RequestParam("folder") String folder,
            @Parameter(description = "Path of file", required = true) @RequestParam("filePath") String filePath
    ) {
        try {
            File file = new File(filePath);
            String filename = file.getName();
            Ir ir = create(folder, filename, Files.readAllBytes(file.toPath()));
            return ResponseEntity.ok(ir.getId());
        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ofNullable(null);
    }

    @Operation(summary = "Сохранение переданного файла")
    @PostMapping(value = "/save/file")
    public ResponseEntity<Long> saveByFile(
            @Parameter(description = "Folder", required = true) @RequestParam("folder") String folder,
            @Parameter(description = "File to load", required = true) @RequestParam("file") MultipartFile file
    ) {
        try {
            String filename = file.getOriginalFilename();
            Ir ir = create(folder, filename, file.getBytes());
            return ResponseEntity.ok(ir.getId());
        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ofNullable(null);
    }

    @Operation(summary = "Создание svg и dot")
    @PostMapping(value = "/generate")
    public ResponseEntity<?> buildSVGByFile(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id,
            @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt
    ) {
        Ir ir = irService.get(id);
        if (ir == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            generate(opt, ir);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
