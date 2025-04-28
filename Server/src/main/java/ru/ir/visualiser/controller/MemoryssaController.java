package ru.ir.visualiser.controller;

import java.io.IOException;

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
 * Controller for scev analysis.
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
     * @param opt - number of opt
     * @param line - line in the file
     * @return text that shuld show up
     * @throws IOException if couldn`t launch opt
     */
    @Operation(summary = "text that should show up by clicking")
    @PostMapping("/get/memoryssa/of/line")
    @ResponseBody
    public String memoryssaOfLine(
        @Parameter(description = "Id of ir") @RequestParam("file") Long id,
        @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt,
        @Parameter(description = "Line number") @RequestParam("line") int line
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return null;
        }

        Memoryssa memoryssa = analysisService.getMemoryssa(ir, opt);
        String res = memoryssa.fromLine(line).orElse("No memoryssa for line " + line);

        return res;
    }

    /**
     * 
     */
    @Operation(summary = "text that should show up by clicking")
    @PostMapping("/get/memoryssa/of/line")
    @ResponseBody
    public String memoryssaOfLine(
        @Parameter(description = "Id of ir") @RequestParam("file") Long id,
        @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt,
        @Parameter(description = "Function name") @RequestParam("functionName") String functionName,
        @Parameter(description = "Access") @RequestParam("access") int access,
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return null;
        }

        Memoryssa memoryssa = analysisService.getMemoryssa(ir, opt);
        String res = memoryssa.fromLine(line).orElse("No memoryssa for line " + line);

        return res;
    }

}
