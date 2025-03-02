package ru.ir.visualiser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.model.Project;
import ru.ir.visualiser.model.ir.Dot;
import ru.ir.visualiser.model.ir.ModuleIR;
import ru.ir.visualiser.service.IrService;
import ru.ir.visualiser.service.ModuleService;
import ru.ir.visualiser.service.ProjectService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** getter controller.
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/files/get")
public class GetterController {
    private final IrService irService;
    private final ModuleService moduleService;
    private final ProjectService projectService;

    /** get list of func.
     *
     * @param id id of ir description.
     * @return list of func
     */
    @PostMapping(value = "/functions")
    @Operation(summary = "Получение всех имен функций")
    @ResponseBody
    public ResponseEntity<List<String>> getFunctions(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id
    ) {
        ModuleIR moduleIR = moduleService.find(id);
        if (moduleIR == null) {
            return ResponseEntity.notFound().build();
        }
        List<Dot> dots = moduleIR.getDots().values().stream().toList();
        List<String> result = new ArrayList<>();
        for (Dot dot : dots) {
            result.add(dot.getFunctionName());
        }
        return ResponseEntity.ok(result);
    }

    /** get svg by function.
     *
     * @param id id of ir description
     * @param functionName function
     * @return svg of function
     */
    @Operation(summary = "Получение svg по имени функции")
    @PostMapping(value = "/svg")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getSvg(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id,
            @Parameter(description = "Function name", required = true) @RequestParam("function") String functionName
    ) {
        Ir ir = irService.get(id);
        if (ir == null) {
            return ResponseEntity.notFound().build();
        }
        String path = ir.getSvgPath() + File.separator + "." + functionName + ".svg";

        File file = new File(path);
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName()  + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .body(new InputStreamResource(inputStream));
    }

    /** get ir file by id.
     *
     * @param id id of ir description
     * @return ir
     */
    @Operation(summary = "Получение ir файла")
    @PostMapping(value = "/code")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getCode(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id
    ) {
        Ir ir = irService.get(id);
        if (ir == null) {
            return ResponseEntity.notFound().build();
        }
        String path = ir.getIrPath() + File.separator + ir.getFilename();
        File file = new File(path);
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName()  + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .body(new InputStreamResource(inputStream));
    }

    /**
     * get projects.
     *
     * @return List Projects
     */
    @Operation(summary = "Получение проектов")
    @PostMapping(value = "/projects")
    @ResponseBody
    public ResponseEntity<List<Project>> getProjets() {
        return ResponseEntity.ok(projectService.getAll());
    }
}
