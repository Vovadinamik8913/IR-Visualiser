package ru.ir.visualiser.model.ir;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private int id;
    @Getter @Setter
    private String label;
    @Getter
    @JsonIgnore
    private int startLine;
    @Getter
    @JsonIgnore
    private int endLine;

    public BlockIR(String label, int startLine, int endLine) {
        this.label = label;
        this.startLine = startLine;
        this.endLine = endLine;
    }
}
