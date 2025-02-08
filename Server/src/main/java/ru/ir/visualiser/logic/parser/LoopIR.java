package ru.ir.visualiser.logic.parser;

import lombok.Getter;
import ru.ir.visualiser.logic.parser.LoopBlock;

import java.util.ArrayList;
import java.util.List;

public class LoopIR {
    @Getter
    private final List<LoopBlock> blocks;
    @Getter
    private final int depth;

    public LoopIR(ArrayList<LoopBlock> blocks, int depth) {
        this.blocks = blocks;
        this.depth = depth;
    }


}
