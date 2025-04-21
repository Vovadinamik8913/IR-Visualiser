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
    private Map<Integer, String> lineToMemoryssaString;

    /**
     * A map from function name and MemoryAccess to the line in the source file.
     */
    @ElementCollection
    @CollectionTable(name = "memoryssa_access_to_line", joinColumns = @JoinColumn(name = "memoryssa_id"))
    @MapKeyColumn(name = "memoryssa_access_id")
    @Column(name = "memoryssa_access_line")
    @Getter
    private Map<String, Integer> accessToLine;

    @OneToOne @JoinColumn(name = "memoryssa_id")
    @Setter @Getter
    private Ir ir;

    public Memoryssa(Map<Integer, String> lineToMemoryssaString, Map<String, Integer> accessToLine) {
        this.lineToMemoryssaString = lineToMemoryssaString;
        this.accessToLine = accessToLine;
    }

    /**
     * Returns text in the format in which it should show up.
     *
     * @param line line in the .ll file
     * @return text that should show up in the box or empty if no text should show up
     */
    public Optional<String> ofLine(int line) {
        String scevString = lineToScevString.get(line);

        if (scevString == null) {
            return Optional.empty();
        }

        StringBuilder str = new StringBuilder();

        int from = 0;
        String[] statStart = new String[]{"U:", "S:", "Exits:", "LoopDispositions:"};

        for (int i = 0; i <= scevString.length(); i++) {
            for (String ss : statStart) {
                if (ss.length() > i - from) {
                    continue;
                }

                int upto = i - ss.length();
                if (scevString.substring(upto, i).equals(ss)) {
                    int uptoWithoutWhitespace = upto;
                    while (uptoWithoutWhitespace > from && Character.isWhitespace(scevString.charAt(uptoWithoutWhitespace - 1))) {
                        uptoWithoutWhitespace--;
                    }

                    str.append(scevString, from, uptoWithoutWhitespace);
                    str.append("\n");
                    from = upto;
                    break;
                }
            }
        }

        str.append(scevString, from, scevString.length());

        return Optional.of(str.toString());
    }

    /**
     * Returns the loop count string for the given block.
     * 
     * @param loopBlock the loop block
     * @return the loop count string
     */
    public Optional<String> loopCount(String function, String block) {
        String loopBlock = function + ":" + block;
        return Optional.ofNullable(loopCount.get(loopBlock));
    }
}
