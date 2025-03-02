package ru.ir.visualiser.model.ir;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that holds dot representation of IR.
 * Holds svg ids mappings to label.
 */
@Entity
@NoArgsConstructor
public class Dot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    private String functionName;

    @ElementCollection
    @CollectionTable(name = "dot_svg_mapping",
            joinColumns = @JoinColumn(name = "dot_id"))
    @MapKeyColumn(name = "svg_id")
    @Column(name = "label")
    private Map<String, String> svgIdToLabel;

    @ElementCollection
    @CollectionTable(name = "dot_label_mapping",
            joinColumns = @JoinColumn(name = "dot_id"))
    @MapKeyColumn(name = "label")
    @Column(name = "svg_id")
    private Map<String, String> labelToSvgId;

    public Dot(String functionName) {
        this.functionName = functionName;
        this.svgIdToLabel = new HashMap<>();
        this.labelToSvgId = new HashMap<>();
    }

    public void addSvgIdToLabel(String svgId, String label) {
        svgIdToLabel.put(svgId, label);
        labelToSvgId.put(label, svgId);
    }

    /**
     * Method to get label by svg id.
     *
     * @param svgId - svg id
     *
     * @return - label
     */
    public String getLabelBySvgId(String svgId) {
        return svgIdToLabel.get(svgId);
    }

    /**
     * Method to get svg id by label.
     *
     * @param label - label
     *
     * @return - svg id
     */
    public String getSvgIdByLabel(String label) {
        if (label == null || label.isEmpty()) {
            label = null;
            for (String labelNow : labelToSvgId.keySet()) {
                if (label == null) {
                    label = labelNow;
                    continue;
                }

                int labelInt;
                try {
                    labelInt = Integer.decode(label);
                } catch (NumberFormatException e) {
                    label = labelNow;
                    continue;
                }
                
                int labelNowInt;
                try {
                    labelNowInt = Integer.decode(labelNow);
                } catch (NumberFormatException e) {
                    continue;
                }

                if (labelNowInt < labelInt) {
                    label = labelNow;
                }
            }
        }
        return labelToSvgId.get(label);
    }
}
