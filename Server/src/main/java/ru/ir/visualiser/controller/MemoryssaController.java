package ru.ir.visualiser.controller;

import java.io.IOException;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.model.analysis.Memoryssa;
import ru.ir.visualiser.service.IrService;
import ru.ir.visualiser.service.AnalysisService;

/**
 * Controller for memoryssa analysis.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/memoryssa")
public class MemoryssaController {

    private final IrService irService;
    private final AnalysisService analysisService;

    /**
     * Get text that should show up by clicking on line.
     *
     * @param id - id of ir in database
     * @param line - line in the file
     * @return text that shuld show up
     * @throws IOException if couldn`t launch opt
     */
    @Operation(summary = "text that should show up by clicking")
    @PostMapping("/get/memoryssa/of/line")
    @ResponseBody
    public ResponseEntity<String> memoryssaOfLine(
        @Parameter(description = "Id of ir") @RequestParam("file") Long id,
        @Parameter(description = "Line number") @RequestParam("line") int line
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return ResponseEntity.badRequest().build();
        }

        Memoryssa memoryssa = analysisService.getMemoryssa(ir);
        String res = memoryssa.fromLine(line).orElse("No memoryssa for line " + line);

        return ResponseEntity.ok(res);
    }

    /**
     * Get text that should show up by clicking on analysis number.
     *
     * @param id - id of ir in database
     * @param functionName - name of a function currently being worked on
     * @param access - access to jump to
     * @return line to jump to
     * @throws IOException if couldn`t launch opt
     */
    @Operation(summary = "text that should show up by clicking")
    @PostMapping("/get/memoryssa/of/access")
    @ResponseBody
    public ResponseEntity<Integer> memoryssaOfAccess(
        @Parameter(description = "Id of ir") @RequestParam("file") Long id,
        @Parameter(description = "Function name") @RequestParam("functionName") String functionName,
        @Parameter(description = "Access") @RequestParam("access") String access
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return ResponseEntity.badRequest().build();
        }

        Memoryssa memoryssa = analysisService.getMemoryssa(ir);

        Optional<Integer> line = Optional.empty();
        try {
            int intAccess = Integer.parseInt(access);
            line = memoryssa.fromAccess(functionName, intAccess);
        } catch (NumberFormatException e) {
        }

        if (line.isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(line.get());
        }
    }

}
