package ru.ir.visualiser.core.parser.domtree;

import lombok.Getter;
import ru.ir.visualiser.model.ir.BlockIR;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DomTreeNode {
    private final BlockIR block;
    private final int Depth;
    private final List<DomTreeNode> children;

    public DomTreeNode(BlockIR block, int depth) {
        this.block = block;
        this.Depth = depth;
        this.children = new ArrayList<>();
    }

    public void addChild(DomTreeNode child) {
        children.add(child);
    }
}
