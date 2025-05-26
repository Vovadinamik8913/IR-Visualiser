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
import ru.ir.visualiser.model.analysis.Scev;
import ru.ir.visualiser.service.IrService;
import ru.ir.visualiser.service.AnalysisService;

/**
 * Controller for scev analysis.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/scev")
public class ScevController {

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
    @PostMapping("/get/scev/of/line")
    @ResponseBody
    public String scevOfLine(
        @Parameter(description = "Id of ir") @RequestParam("file") Long id,
        @Parameter(description = "Line number") @RequestParam("line") int line
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return null;
        }

        Scev scev = analysisService.getScev(ir);

        return scev.parsedLine(line).orElse("No scev for line " + line);
    }

    /**
     * Get text that should show up when requesting loop count.
     *
     * @param id - id of ir in database
     * @param function - name of the function
     * @param block - name of the block
     * @return text that shuld show up
     * @throws IOException if couldn`t launch opt
     */
    @Operation(summary = "text that should when requesting loop count")
    @PostMapping("/get/scev/loop/count")
    @ResponseBody
    public String scevOfLine(
        @Parameter(description = "Id of ir") @RequestParam("file") Long id,
        @Parameter(description = "Function name") @RequestParam("function") String function,
        @Parameter(description = "Block name") @RequestParam("block") String block
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return null;
        }

        Scev scev = analysisService.getScev(ir);

        return scev.loopCount(function, block).orElse("No loop count for " + function + ", " + block);
    }
}
