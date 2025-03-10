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
public class Scev {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * A map from line in the source to the line of she scalar evolution for the
     * binding, without the `-->` in the beginning.
     */
    @ElementCollection
    @CollectionTable(name = "scev_mapping", joinColumns = @JoinColumn(name = "scev_id"))
    @MapKeyColumn(name = "scev_line_number")
    @Column(name = "scev_string")
    @Getter
    private Map<Integer, String> lineToScevString;

    @OneToOne @JoinColumn(name = "ir_id")
    @Setter @Getter
    private Ir ir;

    public Scev(Map<Integer, String> lineToScevString) {
        this.lineToScevString = lineToScevString;
    }

    /**
     * Returns text in the format in which it should show up.
     *
     * @param line line in the .ll file
     * @return text that should show up in the box or empty if no text should show up
     */
    public Optional<String> parsedLine(int line) {
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
                    from = uptoWithoutWhitespace;
                    break;
                }
            }
        }

        str.append(scevString, from, scevString.length());

        return Optional.of(str.toString());
    }
}
