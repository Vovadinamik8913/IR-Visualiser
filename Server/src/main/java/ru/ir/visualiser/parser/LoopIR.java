package ru.ir.visualiser.parser;

import lombok.Getter;

import java.util.List;

public class LoopIR {
    @Getter
    private final List<LoopBlock> blocks;
    @Getter
    private final int depth;

    public LoopIR(List<LoopBlock> blocks, int depth) {
        this.blocks = blocks;
        this.depth = depth;
    }


}
