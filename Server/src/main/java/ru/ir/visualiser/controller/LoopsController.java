package ru.ir.visualiser.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ir.visualiser.files.Config;
import ru.ir.visualiser.model.classes.Ir;
import ru.ir.visualiser.model.classes.ir.BlockIR;
import ru.ir.visualiser.model.classes.ir.Dot;
import ru.ir.visualiser.model.classes.ir.FunctionIR;
import ru.ir.visualiser.model.classes.ir.ModuleIR;
import ru.ir.visualiser.model.service.IrService;
import ru.ir.visualiser.logic.parser.*;
import org.jsoup.Jsoup;
import ru.ir.visualiser.logic.parser.LoopBlock;
import ru.ir.visualiser.logic.parser.LoopIR;
import ru.ir.visualiser.logic.llvm.Opt;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
     * @throws IOException if could`nt read a svg file
     */
    @Operation(summary = "send svg with highlighted loops")
    @PostMapping("/get/svg")
    @ResponseBody
    public ResponseEntity<byte[]> highlightedLoops(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id,
            @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt,
            @Parameter(description = "Function name", required = true) @RequestParam("function") String functionName
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return ResponseEntity.notFound().build();
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
            return ResponseEntity.notFound().build();
        }
        String svgPath = ir.getSvgPath() +  File.separator + "." + functionName + ".svg";
        String optPath = Config.getInstance().getOptsPath()[opt];
        String loopInfoRaw = Opt.printLoops(optPath, ir);
        FunctionIR function;
        function = moduleIr.getFunction(functionName);
        if (function == null) {
            return ResponseEntity.notFound().build();
        }
        List<LoopIR> loops = Parser.findLoopInfofromOpt(function, loopInfoRaw);

        String svgContent = new String(Files.readAllBytes(Paths.get(svgPath)));
        Document svgDoc = Jsoup.parse(svgContent);
        for (LoopIR loop : loops) {
            for (LoopBlock block : loop.getBlocks()) {
                BlockIR nowBlock = block.getBlock();
                String svgId = dot.getSvgIdByLabel(nowBlock.getLabel());
                for (Element titleElement : svgDoc.select("title")) {
                    if (titleElement.text().equals(svgId)) {
                        Element parent = titleElement.parent(); // Получаем родительский <g>
                        if (parent != null) {
                            Element polygon = parent.selectFirst("polygon"); // Берем первый <polygon>
                            if (polygon != null) {
                                polygon.attr("fill", "#FFFF00");  // Новый цвет (жёлтый)
                                polygon.attr("stroke", "#FFFF00");
                            }
                        }
                    }
                }
            }
        }
        return ResponseEntity.ok(svgDoc.outerHtml().getBytes(StandardCharsets.UTF_8));
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
