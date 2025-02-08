package ru.ir.visualiser.logic.parser;

import lombok.Getter;
import lombok.Setter;
import ru.ir.visualiser.model.classes.ir.BlockIR;

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
