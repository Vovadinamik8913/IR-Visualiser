package ru.ir.visualiser.files.model;

import java.io.IOException;
import java.nio.file.Path;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import ru.ir.visualiser.parser.ModuleIR;
import ru.ir.visualiser.parser.Parser;

@Entity
@Table(name = "ir")
@Getter
public class Ir {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String optimization = "init";
    private String filename;
    @OneToOne @JoinColumn(name = "id")
    @Setter
    private Ir parent;
    @OneToMany @JoinColumn(name = "id")
    private List<Ir> children;
    @Getter
    private ModuleIR parsedModule;

    public Ir(Path filePath) throws IOException {
        this.filename = filePath.getFileName().toString();
        this.parent = null;
        this.children = new ArrayList<>();
        this.parsedModule = Parser.parseModule(filePath);
    }

    public Ir(String optimization, Path filePath) throws IOException {
        this(filePath);
        this.optimization = optimization;
    }
}
