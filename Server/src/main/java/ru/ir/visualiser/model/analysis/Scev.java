package ru.ir.visualiser.model.analysis;

import java.util.Map;
import java.util.Optional;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Scev {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "scev_id")
    @MapKey(name = "scev_string")
    @Getter
    /**
     * A map from line in the source to the line of she scalar evolution for the
     * binding, without the `-->` in the beginning.
     */
    private Map<Integer, String> lineToScevString;

    public Scev(Map<Integer, String> lineToScevString) {
        this.lineToScevString = lineToScevString;
    }

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
