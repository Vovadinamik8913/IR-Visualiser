package ru.ir.visualiser.model.ir;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ir.visualiser.model.Ir;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Class that holds a ModuleIr
 * with some info about it.
 */
@Entity
@NoArgsConstructor
public class ModuleIR {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    private String moduleName;
    /// Text of the module, split up into lines.
    @Getter
    private List<String> moduleText;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "module_id")
    @MapKey(name = "functionName")
    private Map<String, FunctionIR> nameToFunctions;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "module_id")
    @MapKey(name = "functionName")
    private Map<String, Dot> nameToDot;

    @OneToOne @JoinColumn(name = "ir_id")
    @Setter @Getter
    private Ir ir;

    /**
     * Create new module ir.
     *
     * @param moduleName name of the module
     * @param moduleText full text of the module split up into lines
     */
    public ModuleIR(String moduleName, List<String> moduleText) {
        this.moduleName = moduleName;
        this.moduleText = moduleText;
        this.nameToFunctions = new HashMap<>();
        this.nameToDot = new HashMap<>();
    }

    public void addFunction(FunctionIR function) {
        nameToFunctions.putIfAbsent(function.getFunctionName(), function);
    }

    public void addNameToDot(String name, Dot dot) {
        nameToDot.putIfAbsent(name, dot);
    }

    public FunctionIR getFunction(String name) {
        return nameToFunctions.get(name);
    }

    public Dot getDot(String name) {
        return nameToDot.get(name);
    }

    public Collection<FunctionIR> getFunctions() {
        return nameToFunctions.values();
    }

    public Map<String, Dot> getDots() {
        return nameToDot;
    }
}
