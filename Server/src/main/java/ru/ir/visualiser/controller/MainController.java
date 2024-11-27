package ru.ir.visualiser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ir.visualiser.files.Config;
import ru.ir.visualiser.files.FileWorker;
import ru.ir.visualiser.files.llvm.Opt;
import ru.ir.visualiser.files.llvm.Svg;
import ru.ir.visualiser.files.model.Ir;
import ru.ir.visualiser.files.model.IrService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/llvm")
public class MainController {
    @Autowired
    IrService irService;

    public MainController() {
        irService = new IrService();
    }

    @Operation(summary = "Отправка файла")
    @PostMapping(value = "/build/path")
    public ResponseEntity<String> buildSVGByPath(
            @Parameter(description = "Ключ") @RequestParam("folder") String folder,
            @Parameter(description = "Имя opt") @RequestParam("opt") int opt,
            @Parameter(description = "Путь к файлу") @RequestParam("filePath") String strFilePath
    ) {
        try {
            Path filePath = Path.of(strFilePath);
            String filename = filePath.getFileName().toString();
            String folderName = FileWorker.getFolderName(filename);
            String path = folder + File.separator + folderName;
            String optPath = Config.getInstance().getOptsPath()[opt];
            Ir ir = new Ir(filePath);
            irService.create(ir);
            FileWorker.createPath(
                    "",
                    path
            );
            FileWorker.createPaths(path,
                    new String[]{
                            "dot_files/"+folderName,
                            "ir_files/"+folderName,
                            "svg_files/"+folderName
                    }
            );
            FileWorker.addFile(path+"/ir_files/"+folderName,
                    filename
            );
            FileWorker.copy(path+"/ir_files/"+folderName,
                    filename,
                    Files.readAllBytes(filePath)
            );
            if (Opt.validateOpt(optPath)) {
                if (Opt.generateDotFiles(
                        optPath,
                        FileWorker.absolutePath(path+"/dot_files/"+folderName),
                        filename
                )) {
                    Svg.generateSvgFiles(
                            FileWorker.absolutePath(path + "/dot_files/" + folderName),
                            FileWorker.absolutePath(path + "/svg_files/" + folderName)
                    );
                }
            }
            return ResponseEntity.ok("ok");
        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return (ResponseEntity<String>) ResponseEntity.badRequest();
    }

    @Operation(summary = "Отправка файла")
    @PostMapping(value = "/build/file")
    public ResponseEntity<String> buildSVGByFile(
            @Parameter(description = "Ключ") @RequestParam("folder") String folder,
            @Parameter(description = "Имя opt") @RequestParam("opt") int opt,
            @Parameter(description = "Файл для загрузки", required = true) @RequestParam("file") MultipartFile file
    ) {
        try {
            String filename = file.getOriginalFilename();
            String folderName = FileWorker.getFolderName(filename);
            String path = folder + File.separator + folderName;
            String optPath = Config.getInstance().getOptsPath()[opt];
            FileWorker.createPath(
                    "",
                    path
            );
            FileWorker.createPaths(path,
                    new String[]{
                            "dot_files/"+folderName,
                            "ir_files/"+folderName,
                            "svg_files/"+folderName
                    }
            );
            FileWorker.addFile(path+"/ir_files/"+folderName,
                    filename
            );
            FileWorker.copy(path+"/ir_files/"+folderName,
                    filename,
                    file.getBytes()
            );

            // TODO: make normal
            // Based on FileWorker.addFile
            Ir ir = new Ir(Path.of(FileWorker.absolutePath(path) + File.separator + filename));
            irService.create(ir);

            if (Opt.validateOpt(optPath)) {
                if (Opt.generateDotFiles(
                        optPath,
                        FileWorker.absolutePath(path+"/dot_files/"+folderName),
                        filename
                )) {
                    Svg.generateSvgFiles(
                            FileWorker.absolutePath(path + "/dot_files/" + folderName),
                            FileWorker.absolutePath(path + "/svg_files/" + folderName)
                    );
                }
            }
            return ResponseEntity.ok("ok");
        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
        return (ResponseEntity<String>) ResponseEntity.badRequest();
    }

    @PostMapping(value = "/get/functions")
    @ResponseBody
    public List<String> getSvgs(
            @Parameter(description = "Ключ") @RequestParam("folder") String folder,
            @Parameter(description = "Имя файла") @RequestParam("filename") String filename
    ) {
        String folderName = FileWorker.getFolderName(filename);
        String path = folder + File.separator + folderName
                + "/svg_files/" + folderName;
        List<String> list = new ArrayList<>();
        File file = new File(FileWorker.absolutePath(path));
        if (file.exists()) {
            for (File f : file.listFiles()) {
                String name = f.getName();
                list.add(name.substring(name.indexOf(".") + 1, name.lastIndexOf(".")));
            }
        }
        return list;
    }

    @PostMapping(value = "/get/svg")
    @ResponseBody
    public byte[] getSvg(
            @Parameter(description = "Ключ") @RequestParam("folder") String folder,
            @Parameter(description = "Имя файла") @RequestParam("filename") String filename,
            @Parameter(description = "Имя функции") @RequestParam("svgname") String svgname
    ) {
        String folderName = FileWorker.getFolderName(filename);
        String path = folder + File.separator + folderName
                + "/svg_files/" + folderName
                + "/." + svgname + ".svg";
        try {
            Path dirPath = Paths.get(FileWorker.absolutePath(path));
            return Files.readAllBytes(dirPath);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
