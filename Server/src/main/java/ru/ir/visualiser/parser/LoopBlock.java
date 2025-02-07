package ru.ir.visualiser.parser;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LoopBlock {
    BlockIR block;
    @Setter
    private boolean latch = false;
    @Setter
    private boolean exit = false;
    @Setter
    private boolean header = false;

    public LoopBlock(BlockIR block) {
        this.block = block;

    }
}
