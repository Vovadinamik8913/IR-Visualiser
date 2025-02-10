package ru.ir.visualiser.logic.parser.loops;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds loop info.
 */
@Getter
public class LoopIR {
    private final List<LoopBlock> blocks;
    private final int depth;

    public LoopIR(ArrayList<LoopBlock> blocks, int depth) {
        this.blocks = blocks;
        this.depth = depth;
    }


}
