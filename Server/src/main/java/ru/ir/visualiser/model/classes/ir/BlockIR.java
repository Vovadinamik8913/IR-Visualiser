package ru.ir.visualiser.model.classes.ir;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
    private String textRaw;
    @Getter
    private String label;
    @Getter
    private int startLine;
    @Getter
    private int endLine;

    public BlockIR(String textRaw, String label, int startLine, int endLine) {
        this.textRaw = textRaw;
        this.label = label;
        this.startLine = startLine;
        this.endLine = endLine;
    }
}
