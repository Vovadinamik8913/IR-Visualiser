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
import ru.ir.visualiser.core.llvm.Opt;
import ru.ir.visualiser.core.llvm.Svg;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.model.Project;
import ru.ir.visualiser.service.IrService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import ru.ir.visualiser.model.ir.ModuleIR;
import ru.ir.visualiser.service.ModuleService;
import ru.ir.visualiser.core.parser.Parser;
import ru.ir.visualiser.service.ProjectService;

/** controller for saving files.
 * and generating dots and svgs
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class BuilderController {
    private final IrService irService;
    private final ModuleService moduleService;
    private final ProjectService projectService;

    /** generating opts and svgs.
     *
     * @param opt number of opt in arr
     * @param ir description of our file
     * @throws IOException if problems with file
     */
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

    private Ir create(Project project, String folder, String filename, byte[] content) throws IOException {
        String folderName = FileWorker.getFolderName(filename);
        String path = FileWorker.absolutePath(folder + File.separator + folderName);


        Ir ir = new Ir(filename,
                path,
                path + File.separator + "svg_files",
                path + File.separator + "dot_files",
                project);

        FileWorker.createPath(ir.getSvgPath());
        FileWorker.createPath(ir.getDotPath());
        FileWorker.copy(ir.getIrPath(),
                filename,
                content
        );
        ir = irService.create(ir);
        return ir;
    }

    /**
     * saving to work path by file path.
     *
     * @param folder   name of project group
     * @param filePath path to ir file
     * @return id of class description
     */
    @Operation(summary = "Сохранение файла по пути до него")
    @PostMapping(value = "/save/path")
    public ResponseEntity<Long> saveByPath(
            @Parameter(description = "Folder", required = true) @RequestParam("folder") String folder,
            @Parameter(description = "Path of file", required = true) @RequestParam("filePath") String filePath
    ) {
        File file = new File(filePath);
        String filename = file.getName();
        Project project = projectService.findByName(folder);
        if (project == null) {
            project = new Project(folder);
        }
        Ir ir = null;
        try {
            ir = create(project, folder, filename,
                    Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(ir.getId());
    }

    /**
     * saving to work path file.
     *
     * @param folder name of project group
     * @param file   file
     * @return id of class description
     */
    @Operation(summary = "Сохранение переданного файла")
    @PostMapping(value = "/save/file")
    public ResponseEntity<Long> saveByFile(
            @Parameter(description = "Folder", required = true) @RequestParam("folder") String folder,
            @Parameter(description = "File to load", required = true) @RequestParam("file") MultipartFile file
    ) {
        String filename = file.getOriginalFilename();
        Project project =  projectService.findByName(folder);
        if (project == null) {
            project = new Project(folder);
        }
        Ir ir = null;
        try {
            ir = create(project, folder, filename, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(ir.getId());
    }

    /** end-point for generating.
     * dots and svgs
     *
     * @param id id of class description
     * @param opt index of opt
     * @return ok or badRequest
     */
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
            File file = new File(ir.getIrPath() + File.separator + ir.getFilename());
            byte[] content = Files.readAllBytes(file.toPath());
            String moduleContent = new String(content, StandardCharsets.UTF_8);
            DirectoryStream<Path> dotsDirectory = Files.newDirectoryStream(Path.of(ir.getDotPath()));
            List<String> dots = new java.util.ArrayList<>();
            for (Path dotPath : dotsDirectory) {
                dots.add(Files.readString(dotPath));
            }
            ModuleIR module = Parser.parseModule(moduleContent, dots);
            module.setIr(ir);
            moduleService.create(module);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
