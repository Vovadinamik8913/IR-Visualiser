package ru.ir.visualiser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.Optional;
import ru.ir.visualiser.files.model.IrService;
import ru.ir.visualiser.parser.FunctionIR;
import ru.ir.visualiser.parser.ModuleIR;
import ru.ir.visualiser.parser.BlockIR;
import ru.ir.visualiser.parser.Dot;


/**
 * Controller that starts parsing and maps lines to function names.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/fromline")
public class LineToSvgController {
    private final IrService irService;

    /**
     * Method to get svg and svg id from line 
     *
     * @param line - line number in .ll file
     * @param id - id of the ir
     *
     * @return - Array of two elements. String[0] - name of the function, String[1] - svg id
     */
    @Operation(summary = "send function info corresponding to a line")
    @PostMapping(value = "/get/svg")
    @ResponseBody
    public String[] getSvg(
        @Parameter(description = "Номер строки") @RequestParam("line") int line,
        @Parameter(description = "Id of ir") @RequestParam("file") Long id
    ) {
        ModuleIR module = irService.getModule(id);

        Collection<FunctionIR> functions = module.getFunctions();
        FunctionIR function = null;
        for (FunctionIR functionNow : functions) {
            if (functionNow.getStartLine() <= line && functionNow.getEndLine() >= line) {
                function = functionNow;
                break;
            }
        }
        if (function == null) {
            System.err.println("Function not found");
            return null;
        }
        BlockIR block = null;
        for (BlockIR blockNow : function.getBlocks()) {
            if (blockNow.getStartLine() <= line && blockNow.getEndLine() >= line) {
                block = blockNow;
                break;
            }
        }
        if (block == null) {
            System.err.println("Block not found");
            return new String[]{function.getFunctionName()};
        }

        Dot dot = module.getDot(function.getFunctionName());
        String svgId = dot.getSvgIdByLabel(block.getLabel());

        return new String[]{function.getFunctionName(), svgId};
    }

    /**
     * Method to get line from svg id.
     *
     * @param svgId - svg id
     * @param id - id of the ir
     * @param function - name of the current function
     *
     * @return - line number from the beginning of the file
     */
    @Operation(summary = "send line corresponding to a svg id")
    @PostMapping(value = "/get/line")
    @ResponseBody
    public int getLine(
        @Parameter(description = "Id of svg") @RequestParam("id") String svgId,
        @Parameter(description = "Id of ir") @RequestParam("file") Long id,
        @Parameter(description = "Function name") @RequestParam("function") String function
    ) {
        ModuleIR module = irService.getModule(id);
        String label = module.getDot(function).getLabelBySvgId(svgId);

        FunctionIR functionIr = module.getFunction(function);

        BlockIR blockIr = functionIr.getBlock(Optional.ofNullable(label));

        return blockIr.getStartLine();
    }
}
