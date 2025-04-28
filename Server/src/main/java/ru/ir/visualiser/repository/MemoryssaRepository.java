package ru.ir.visualiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.analysis.Scev;
import java.util.Optional;
import ru.ir.visualiser.model.Ir;

/**
 * Memoryssa repository.
 */
public interface MemoryssaRepository extends JpaRepository<Scev, Long> {

    Optional<Scev> findByIr(Ir ir);
}
