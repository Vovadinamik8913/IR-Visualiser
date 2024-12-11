package ru.ir.visualiser.files.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import ru.ir.visualiser.parser.ModuleIR;

@Entity
@Table(name = "ir")
@Getter
public class Ir {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String optimization;
    private String filename;
    @Setter
    private String dotPath;
    @Setter
    private String svgPath;
    @Setter
    private String irPath;
    @OneToOne @JoinColumn(name = "id")
    @Setter
    private Ir parent;
    @OneToMany @JoinColumn(name = "id")
    private List<Ir> children;
    @Setter
    @OneToOne @JoinColumn(name = "id")
    private ModuleIR module;

    public Ir(String filename) {
        this.optimization = "init";
        this.filename = filename;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public Ir(String optimization, String filename) {
        this.optimization = optimization;
        this.filename = filename;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public Ir() {

    }


}
