package ru.ir.visualiser.controller.response;

import lombok.Getter;
import ru.ir.visualiser.model.Ir;

import java.util.ArrayList;
import java.util.List;

/** Tree response.
 *
 */
@Getter
public class Node {
    private final long id;
    private final String name;
    private final String flags;
    private final List<Node> children;

    /** constructor.
     *
     * @param ir ir
     */
    public Node(Ir ir) {
        this.name = ir.getFilename().substring(0, ir.getFilename().lastIndexOf('.'));
        this.id = ir.getId();
        this.flags = ir.getFlags();
        children = new ArrayList<>();
        setChildren(ir);
    }

    private void setChildren(Ir ir) {
        for (Ir child : ir.getChildren()) {
            children.add(new Node(child));
        }
    }
}
