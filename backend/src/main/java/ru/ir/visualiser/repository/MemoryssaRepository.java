package ru.ir.visualiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.analysis.Memoryssa;
import java.util.Optional;
import ru.ir.visualiser.model.Ir;

/**
 * Memoryssa repository.
 */
public interface MemoryssaRepository extends JpaRepository<Memoryssa, Long> {

    Optional<Memoryssa> findByIr(Ir ir);
}
