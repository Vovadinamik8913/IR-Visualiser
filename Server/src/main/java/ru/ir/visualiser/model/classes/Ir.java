package ru.ir.visualiser.model.classes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.ir.visualiser.model.classes.ir.ModuleIR;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity

@NoArgsConstructor
public class Ir {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;
    @Getter
    private String flags;
    @Getter
    private String filename;
    @Getter
    private String dotPath;
    @Getter
    private String svgPath;
    @Getter
    private String irPath;
    @Getter
    @Setter
    private List<String> functions;
    @ManyToOne @JoinColumn(name = "parent_id")
    @Getter
    private Ir parent;
    @OneToMany(mappedBy = "parent")
    private List<Ir> children;
    @OneToOne(mappedBy = "ir")
    @Getter
    private ModuleIR moduleIR;

    public Ir(String filename, String irPath, String svgPath, String dotPath) {
        this.flags = "init";
        this.filename = filename;
        this.parent = null;
        this.children = new ArrayList<>();
        this.dotPath = dotPath;
        this.svgPath = svgPath;
        this.irPath = irPath;
    }

    public Ir(Ir parent, String flags) {
        this.parent = parent;
        flags = flags.substring(flags.lastIndexOf("-passes=") + "-passes=".length());
        this.irPath = parent.getIrPath() + File.separator + flags;
        this.svgPath = irPath + File.separator + "svg_files";
        this.dotPath = irPath + File.separator + "dot_files";
        this.flags = flags;
        this.filename = flags + ".ll";
        this.children = new ArrayList<>();
    }

    public void addChild(Ir child) {
        children.add(child);
    }

    public Collection<Ir> getChildren() {
        return children;
    }
}
