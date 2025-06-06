package ru.ir.visualiser.core.parser.loops;

import lombok.Getter;
import lombok.Setter;
import ru.ir.visualiser.model.ir.BlockIR;

/**
 * Class that holds single block of loop.
 */
@Getter
public class LoopBlock {
    @Getter
    private BlockIR block;
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
