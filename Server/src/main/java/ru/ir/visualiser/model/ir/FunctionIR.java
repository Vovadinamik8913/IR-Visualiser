package ru.ir.visualiser.model.ir;

import java.util.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Class that holds a FunctionIr
 * with some info about it.
 */
@Entity
@NoArgsConstructor
public class FunctionIR {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // добавляем поле id для идентификации сущности

    @Getter
    private String functionName;

    @ElementCollection
    @CollectionTable(name = "function_parameters",
            joinColumns = @JoinColumn(name = "function_id"))
    @Column(name = "parameter")
    private List<String> parameters;

    @Getter
    private String functionTextRaw;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "function_id")
    @MapKey(name = "label")
    private Map<String, BlockIR> labelToBlock;

    @Getter
    private int startLine;

    @Getter
    private int endLine;

    public FunctionIR(String functionName,  String functionTextRaw, int startLine, int endLine) {
        this.functionName = functionName;
        this.functionTextRaw = functionTextRaw;
        this.startLine = startLine;
        this.endLine = endLine;
        this.labelToBlock = new HashMap<>();
        this.parameters = new ArrayList<>();
    }

    public void addParameter(String parameter) {
        parameters.add(parameter);
    }

    public void addBlock(BlockIR block) {
        labelToBlock.put(block.getLabel(), block);
    }

    /**
     * Get block by label.
     *
     * @param label - label
     *
     * @return - block
     */
    public BlockIR getBlock(String label) {
        BlockIR block = labelToBlock.get(label);
        if (block == null) {
            return labelToBlock.get("");
        }
        return block;
    }

    public Collection<BlockIR> getBlocks() {
        return labelToBlock.values();
    }

}
