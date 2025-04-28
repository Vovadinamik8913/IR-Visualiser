package ru.ir.visualiser.model.analysis;

import java.util.Map;
import java.util.Optional;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ir.visualiser.model.Ir;

/**
 * Contains mapping from line to the text that should show up upon clicking on that line when in scev analysis mode.
 */
@Entity
@NoArgsConstructor
public class Memoryssa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * A map from line in the source to the MemoryAccess of the expression on that line.
     */
    @ElementCollection
    @CollectionTable(name = "memoryssa_mapping", joinColumns = @JoinColumn(name = "memoryssa_id"))
    @MapKeyColumn(name = "memoryssa_line_number")
    @Column(name = "memoryssa_string")
    @Getter
    private final Map<Integer, String> lineToMemoryssaString;

    /**
     * A map from function name and MemoryAccess to the line in the source file.
     */
    @ElementCollection
    @CollectionTable(name = "memoryssa_access_to_line", joinColumns = @JoinColumn(name = "memoryssa_id"))
    @MapKeyColumn(name = "memoryssa_access_id")
    @Column(name = "memoryssa_access_line")
    @Getter
    private final Map<String, Integer> accessToLine;

    @OneToOne @JoinColumn(name = "memoryssa_id")
    @Setter @Getter
    private Ir ir;

    public Memoryssa(Map<Integer, String> lineToMemoryssaString, Map<String, Integer> accessToLine) {
        this.lineToMemoryssaString = lineToMemoryssaString;
        this.accessToLine = accessToLine;
    }

    /**
     * Returns concatenated function name and access. It sholud be the key for `accessToLine`.
     *
     * @param functionName function name
     * @param access access
     * @return concatenated function name and access
     */
    public String concatNameAndAccess(String functionName, int access) {
        return functionName + ":" + access;
    }

    /**
     * Returns text in the format in which it should show up.
     *
     * @param line line in the .ll file
     * @return text that should show up in the box or empty if no text should show up
     */
    public Optional<String> fromLine(int line) {
        String val = lineToMemoryssaString.get(line);
        return Optional.ofNullable(val);
    }

    /**
     * Returns the line in the source file that corresponds to the given MemoryAccess.
     */
    public Optional<Integer> fromAccess(String functionName, int access) {
        String key = concatNameAndAccess(functionName, access);
        Integer val = accessToLine.get(key);
        return Optional.ofNullable(val);
    }

}
