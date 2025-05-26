package ru.ir.visualiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.analysis.Scev;
import java.util.Optional;
import ru.ir.visualiser.model.Ir;

/**
 * Scev repository.
 */
public interface ScevRepository extends JpaRepository<Scev, Long> {

    Optional<Scev> findByIr(Ir ir);
}
