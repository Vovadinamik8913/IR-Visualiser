package ru.ir.visualiser.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.classes.Ir;
import ru.ir.visualiser.model.classes.ir.ModuleIR;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<ModuleIR, Long> {
    Optional<ModuleIR> findByIr(Ir ir);
}
