package ru.ir.visualiser.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.ir.visualiser.model.ir.ModuleIR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Ir {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String flags;
    private String filename;
    private String dotPath;
    private String svgPath;
    private String irPath;

    @ManyToOne @JoinColumn(name = "parent_id")
    private Ir parent;

    @OneToMany(mappedBy = "parent")
    private List<Ir> children;

    @OneToOne(mappedBy = "ir")
    private ModuleIR moduleIR;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "project_id")
    private Project project;

    public Ir(String filename, String irPath, String svgPath, String dotPath, Project project) {
        this.flags = "init";
        this.filename = filename;
        this.parent = null;
        this.children = new ArrayList<>();
        this.dotPath = dotPath;
        this.svgPath = svgPath;
        this.irPath = irPath;
        this.project = project;
    }

    public Ir(Ir parent, String flags) {
        this.parent = parent;
        this.irPath = parent.getIrPath() + File.separator + flags;
        this.svgPath = irPath + File.separator + "svg_files";
        this.dotPath = irPath + File.separator + "dot_files";
        this.flags = flags;
        if (flags.contains("-passes")) {
            String[] split = flags.split(" ");
            for (String s : split) {
                if (s.contains("-passes")) {
                    this.flags = s;
                    break;
                }
            }
        }
        this.filename = this.flags + ".ll";
        this.children = new ArrayList<>();
    }
}
