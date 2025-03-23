package ru.ir.visualiser.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ir.visualiser.config.Config;
import ru.ir.visualiser.files.FileWorker;
import ru.ir.visualiser.core.llvm.Opt;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.model.Project;
import ru.ir.visualiser.service.IrService;
import ru.ir.visualiser.service.ProjectService;
import ru.ir.visualiser.controller.response.Node;

import java.io.IOException;

/** controller for tree.
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tree")
public class TreeController {
    private final IrService irService;
    private final ProjectService projectService;

    /** generating new ir.
     * by applying optimizations
     *
     * @param id id of class description
     * @param opt index of opt
     * @param flags flags(param)
     * @return id of class desc for new file
     */
    @Operation(summary = "Использование оптимизаций и создание новой ветки")
    @PostMapping(value = "/add")
    public ResponseEntity<Long> optimizeFile(
            @Parameter(description = "Parent", required = true) @RequestParam("file") Long id,
            @Parameter(description = "Opt", required = true) @RequestParam("opt") int opt,
            @Parameter(description = "Optimization", required = true) @RequestParam("flags") String flags
    ) {
        Ir parent = irService.get(id);
        if (parent == null) {
            return ResponseEntity.notFound().build();
        }
        Ir child = new Ir(parent, flags);
        FileWorker.createPath(child.getDotPath());
        FileWorker.createPath(child.getSvgPath());
        String optPath = Config.getInstance().getOptsPath()[opt];
        try {
            Opt.optimizeOpt(optPath, parent, child);
            child = irService.create(child);
            Opt.optimizeOpt(optPath, parent, child);
            irService.create(child);
            return ResponseEntity.ok(child.getId());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok(null);
    }

    /** delete this ir and children.
     *
     * @param id id of ir description
     */
    @Operation(summary = "Удаление ветки")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteBranch(
            @Parameter(description = "node", required = true) @RequestParam("node") Long id
    ) {
        try {
            irService.deleteById(id);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    /** get tree.
     *
     * @param id id of ir description
     * @return tree
     */
    @Operation(summary = "Получение дерева")
    @PostMapping(value = "/get")
    public ResponseEntity<Node> get(
            @Parameter(description = "Project", required = true) @RequestParam("project") Long id
    ) {
        Project project = projectService.find(id);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        Ir parent = project.getIr();
        Node res = new Node(parent);
        return ResponseEntity.ok(res);
    }
}
