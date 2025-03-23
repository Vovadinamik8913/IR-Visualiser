package ru.ir.visualiser.model.ir;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Class for blockIr.
 *
 */
@Entity
@NoArgsConstructor
public class BlockIR {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Getter @Setter
    private String label;
    @Getter
    private int startLine;
    @Getter
    private int endLine;

    public BlockIR(String label, int startLine, int endLine) {
        this.label = label;
        this.startLine = startLine;
        this.endLine = endLine;
    }
}
