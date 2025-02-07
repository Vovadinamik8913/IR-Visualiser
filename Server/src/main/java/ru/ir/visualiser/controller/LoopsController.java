package ru.ir.visualiser.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ir.visualiser.files.Config;
import ru.ir.visualiser.files.llvm.Opt;
import ru.ir.visualiser.files.model.Ir;
import ru.ir.visualiser.files.model.IrService;
import ru.ir.visualiser.parser.*;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loops")
public class LoopsController {
    private final IrService irService;


    @Operation(summary = "send svg with highlighted loops")
    @PostMapping("/get/svg")
    @ResponseBody
    public ResponseEntity<byte[]> highlightedLoops(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id,
            @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt,
            @Parameter(description = "Function name", required = true) @RequestParam("function") String functionName
    ) throws IOException {
        Ir ir = irService.get(id);
        if(ir == null) {
            return ResponseEntity.notFound().build();
        }
        Dot dot = irService.getModule(id).getDot(functionName);
        String svgPath = ir.getSvgPath() +  File.separator + "." + functionName + ".svg";
        String optPath = Config.getInstance().getOptsPath()[opt];
        String loopInfoRaw = Opt.printLoops(optPath, ir);
        FunctionIR function;
        function = irService.getModule(id).getFunction(functionName);
        if(function == null) {
            return ResponseEntity.notFound().build();
        }
        List<LoopIR> loops = Parser.findLoopInfofromOpt(function, loopInfoRaw);

        String svgContent = new String(Files.readAllBytes(Paths.get(svgPath)));
        Document svgDoc = Jsoup.parse(svgContent);
        for (LoopIR loop : loops) {
            for(LoopBlock block : loop.getBlocks()) {
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
        if(ir == null) {
            return "IR not found";
        }
        String optPath = Config.getInstance().getOptsPath()[opt];
        String loopInfoRaw = Opt.printLoops(optPath, ir);
        if(irService.getModule(id).getFunction(functionName) == null) {
            return "Function not found";
        }
        List<LoopIR> loops = Parser.findLoopInfofromOpt(irService.getModule(id).getFunction(functionName), loopInfoRaw);
        int maxDepth = 0;
        LoopIR neighbors = null;
        for(LoopIR loop : loops) {
            for(LoopBlock block : loop.getBlocks()) {
                BlockIR nowBlock = block.getBlock();
                if(nowBlock.getLabel().equals(Optional.of(blockName))) {
                    if(loop.getDepth() > maxDepth) {
                        maxDepth = loop.getDepth();
                        neighbors = loop;
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Neighbors of the block:\n");
        if(neighbors == null) {
            return "Block not found";
        }
        for(LoopBlock block: neighbors.getBlocks()) {
            if(!block.getBlock().getLabel().equals(Optional.of(blockName))) {
                sb.append(block.getBlock().getLabel());
                if(block.isHeader()) {
                    sb.append("Header ");
                }
                if(block.isLatch()) {
                    sb.append("Latch ");
                }
                if(block.isExit()) {
                    sb.append("Exit ");
                }
                sb.append("\n");
            }
        }
        sb.append("Max depth: ").append(maxDepth).append("\n");
        return sb.toString();
    }
}
