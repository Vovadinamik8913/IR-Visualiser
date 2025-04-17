package ru.ir.visualiser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ir.visualiser.config.Config;
import ru.ir.visualiser.core.llvm.Opt;
import ru.ir.visualiser.core.parser.Parser;
import ru.ir.visualiser.core.parser.domtree.DomTreeNode;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.model.ir.FunctionIR;
import ru.ir.visualiser.model.ir.ModuleIR;
import ru.ir.visualiser.service.IrService;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * I love reviewdog. It is a controller for domtree analysis.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/domtree")
public class DomtreeController {
    private final IrService irService;

    /**
     * Simple recursive search.
     *
     * @param node - node to search from
     *
     * @param targetName - name of the block
     *
     * @return either null or target node
     */
    private DomTreeNode findBlockByName(DomTreeNode node, String targetName) {
        if (node.getBlock().getLabel().equals(targetName)) {
            return node;
        }
        for (DomTreeNode child : node.getChildren()) {
            DomTreeNode result = findBlockByName(child, targetName);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    /**
     * See operation summary.
     *
     * @param id - id of ir
     *
     * @param opt - opt number
     *
     * @param functionName - func name
     *
     * @param block - block name
     *
     * @return Labels of the blocks separated by \n
     *
     * @throws IOException if couldnt start opt
     */
    @Operation(summary = "send children of block in domtree. "
            + "to get root of the domtree param 'block' must be 'root'")
    @PostMapping("/get/children")
    public String getChildren(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id,
            @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt,
            @Parameter(description = "Function name", required = true) @RequestParam("function") String functionName,
            @Parameter(description = "Block name", required = true) @RequestParam("block") String block
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return "ir not found";
        }
        ModuleIR moduleIr = ir.getModuleIR();
        FunctionIR function;
        function = moduleIr.getFunction(functionName);
        if (function == null) {
            return "Function not found";
        }
        String optPath = Config.getInstance().getOptsPath()[opt];
        String domTreeInfo = Opt.printDomtree(optPath, ir);
        DomTreeNode root = Parser.findDomtreeInfofromOpt(function, domTreeInfo);
        if (Objects.equals(block, "root")) {
            return root.getBlock().getLabel();
        }
        DomTreeNode targetNode = findBlockByName(root, block);
        if (targetNode == null) {
            return "block not found";
        }
        StringBuilder result = new StringBuilder();
        for (DomTreeNode child : targetNode.getChildren()) {
            result.append(child.getBlock().getLabel());
            result.append("\n");
        }
        return result.toString();
    }


    @Operation(summary = "get domtree ")
    @PostMapping("/get")
    public ResponseEntity<DomTreeNode> getDomtree(
            @Parameter(description = "Id of ir", required = true) @RequestParam("file") Long id,
            @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt,
            @Parameter(description = "Function name", required = true) @RequestParam("function") String functionName
    ) throws IOException {
        Ir ir = irService.get(id);
        if (ir == null) {
            return ResponseEntity.notFound().build();
        }
        ModuleIR moduleIr = ir.getModuleIR();
        FunctionIR function;
        function = moduleIr.getFunction(functionName);
        if (function == null) {
            return ResponseEntity.notFound().build();
        }
        String optPath = Config.getInstance().getOptsPath()[opt];
        String domTreeInfo = Opt.printDomtree(optPath, ir);
        DomTreeNode res = Parser.findDomtreeInfofromOpt(function, domTreeInfo);
        return ResponseEntity.ok(res);
    }
}
