package ru.ir.visualiser.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ir.visualiser.config.Config;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.model.ir.BlockIR;
import ru.ir.visualiser.model.ir.Dot;
import ru.ir.visualiser.model.ir.FunctionIR;
import ru.ir.visualiser.model.ir.ModuleIR;
import ru.ir.visualiser.service.IrService;
import ru.ir.visualiser.core.parser.*;
import ru.ir.visualiser.core.parser.loops.LoopBlock;
import ru.ir.visualiser.core.parser.loops.LoopIR;
import ru.ir.visualiser.core.llvm.Opt;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Controller that return highlighted svg and loop info.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/loops")
public class LoopsController {
    private final IrService irService;

    /**
     * See operation summary.
     *
     * @param id - id of ir in database
     *
     * @param opt - number of opt
     *
     * @param functionName - function name ex. "add"
     *
     * @return svg with highlighted loops
     *
     * @throws IOException if couldn`t read a svg file
     */
    @Operation(summary = "send svg with highlighted loops")
    @PostMapping("/get/all/svg")
    @ResponseBody
    public String highlightedLoops(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id,
            @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt,
            @Parameter(description = "Function name", required = true) @RequestParam("function") String functionName
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return "IR not found";
        }
        ModuleIR moduleIr = ir.getModuleIR();
        Collection<Dot> dots = moduleIr.getDots().values();
        Dot dot = null;
        for (Dot nowDot : dots) {
            if (nowDot.getFunctionName().equals(functionName)) {
                dot = nowDot;
            }
        }
        if (dot == null) {
            return "Dot not found";
        }
        String optPath = Config.getInstance().getOptsPath()[opt];
        String loopInfoRaw = Opt.printLoops(optPath, ir);
        FunctionIR function;
        function = moduleIr.getFunction(functionName);
        if (function == null) {
            return "function not found";
        }
        List<LoopIR> loops = Parser.findLoopInfofromOpt(function, loopInfoRaw);
        StringBuilder sb = new StringBuilder();
        for (LoopIR loop : loops) {
            for (LoopBlock block : loop.getBlocks()) {
                BlockIR nowBlock = block.getBlock();
                sb.append(nowBlock.getLabel().substring(1)).append("\n");
            }
        }
        return sb.toString();
    }


    /**
     *
     * @param id - ir
     *
     * @param opt - index of opt path in config
     *
     * @param functionName - function to be analyzed
     *
     * @param blockName - name of clicked block
     *
     * @param click - number of click
     *
     * @return - svg with highlighted loop corresponding to depth
     *
     * @throws IOException - if svg not found
     */
    @Operation(summary = "send svg with highlighted loop according to depth")
    @PostMapping("/get/loop/svg/by/click")
    @ResponseBody
    public String highlightedLoopDepth(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id,
            @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt,
            @Parameter(description = "Function name", required = true) @RequestParam("function") String functionName,
            @Parameter(description = "Block name", required = true) @RequestParam("block") String blockName,
            @Parameter(description = "Click", required = true) @RequestParam ("click") int click
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return "Ir not found";
        }
        ModuleIR moduleIr = ir.getModuleIR();
        Collection<Dot> dots = moduleIr.getDots().values();
        Dot dot = null;
        for (Dot nowDot : dots) {
            if (nowDot.getFunctionName().equals(functionName)) {
                dot = nowDot;
            }
        }
        if (dot == null) {
            return "Dot not found`";
        }
        String optPath = Config.getInstance().getOptsPath()[opt];
        String loopInfoRaw = Opt.printLoops(optPath, ir);
        FunctionIR function;
        function = moduleIr.getFunction(functionName);
        if (function == null) {
            return "function not found";
        }
        List<LoopIR> loops = Parser.findLoopInfofromOpt(function, loopInfoRaw);
        int maxDepth = 0;
        for (LoopIR loop : loops) {
            for (LoopBlock block : loop.getBlocks()) {
                BlockIR nowBlock = block.getBlock();
                if (nowBlock.getLabel().equals(blockName)) {
                    if (loop.getDepth() > maxDepth) {
                        maxDepth = loop.getDepth();
                    }
                }
            }
        }
        click = (click - 1) % maxDepth + 1; //цикличный остаток от деления
        int desiredDepth = (maxDepth - (click - 1));
        LoopIR loopToBeHighlight = null;
        for (LoopIR loop : loops) {
            for (LoopBlock block : loop.getBlocks()) {
                BlockIR nowBlock = block.getBlock();
                if (nowBlock.getLabel().equals(blockName)) {
                    if (loop.getDepth() == desiredDepth) {
                        loopToBeHighlight = loop;
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (LoopBlock block : loopToBeHighlight.getBlocks()) {
            BlockIR nowBlock = block.getBlock();
            sb.append(nowBlock.getLabel()).append("\n");
        }
        return sb.toString();
    }

    /**
     * See operation summary.
     *
     * @param id - id of ir
     *
     * @param opt - id of opt
     *
     * @param functionName - function name ex "add"
     *
     * @param blockName - block name without "%" ex "13"
     *
     * @return - info about block neighbors
     *
     * @throws IOException - if could`nt start opt
     */
    @Operation(summary = "get loop info about block")
    @PostMapping("/get/block/by/name")
    @ResponseBody
    public String blockLoopInfo(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id,
            @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt,
            @Parameter(description = "Function name", required = true) @RequestParam("function") String functionName,
            @Parameter(description = "Block name", required = true) @RequestParam("block") String blockName
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return "IR not found";
        }
        ModuleIR moduleIR = ir.getModuleIR();
        String optPath = Config.getInstance().getOptsPath()[opt];
        String loopInfoRaw = Opt.printLoops(optPath, ir);
        if (moduleIR.getFunction(functionName) == null) {
            return "Function not found";
        }
        List<LoopIR> loops = Parser.findLoopInfofromOpt(moduleIR.getFunction(functionName), loopInfoRaw);
        int maxDepth = 0;
        LoopIR neighbors = null;
        for (LoopIR loop : loops) {
            for (LoopBlock block : loop.getBlocks()) {
                BlockIR nowBlock = block.getBlock();
                if (nowBlock.getLabel().equals(blockName)) {
                    if (loop.getDepth() > maxDepth) {
                        maxDepth = loop.getDepth();
                        neighbors = loop;
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Neighbors of the block:\n");
        if (neighbors == null) {
            return "Block not found";
        }
        for (LoopBlock block: neighbors.getBlocks()) {
            if (!block.getBlock().getLabel().equals(blockName)) {
                sb.append(block.getBlock().getLabel()).append(" ");
                if (block.isHeader()) {
                    sb.append("Header ");
                }
                if (block.isLatch()) {
                    sb.append("Latch ");
                }
                if (block.isExit()) {
                    sb.append("Exit ");
                }
                sb.append("\n");
            }
        }
        sb.append("Max depth: ").append(maxDepth).append("\n");
        return sb.toString();
    }
}
