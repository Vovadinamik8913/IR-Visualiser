package ru.ir.visualiser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ir.visualiser.model.classes.Ir;
import ru.ir.visualiser.model.service.IrService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files/get")
public class GetterController {
    private final IrService irService;

    @PostMapping(value = "/functions")
    @Operation(summary = "Получение всех имен функций")
    @ResponseBody
    public List<String> getFunctions(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id
    ) {
        Ir ir = irService.get(id);
        List<String> list = new ArrayList<>();
        File file = new File(ir.getSvgPath());
        if (file.exists()) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                String name = f.getName();
                list.add(name.substring(name.indexOf(".") + 1, name.lastIndexOf(".")));
            }
        }
        return list;
    }

    @Operation(summary = "Получение svg по имени функции")
    @PostMapping(value = "/svg")
    @ResponseBody
    public ResponseEntity<byte[]> getSvg(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id,
            @Parameter(description = "Function name", required = true) @RequestParam("function") String functionName
    ) {
        Ir ir = irService.get(id);
        String path = ir.getSvgPath() + File.separator + "." + functionName + ".svg";
        try {
            Path dirPath = Paths.get(path);
            return ResponseEntity.ok(Files.readAllBytes(dirPath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ofNullable(null);
    }

    @Operation(summary = "Получение svg по имени функции")
    @PostMapping(value = "/code")
    @ResponseBody
    public ResponseEntity<byte[]> getCode(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id
    ) {
        Ir ir = irService.get(id);
        String path = ir.getIrPath() + File.separator + ir.getFilename();
        try {
            Path dirPath = Paths.get(path);
            return ResponseEntity.ok(Files.readAllBytes(dirPath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ofNullable(null);
    }

}
