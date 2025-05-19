package ru.ir.visualiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.model.ir.ModuleIR;

import java.util.Optional;

public interface ModuleRepository extends JpaRepository<ModuleIR, Long> {
    Optional<ModuleIR> findByIr(Ir ir);
}
