package ru.ir.visualiser.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.classes.ir.BlockIR;

public interface BlockRepository extends JpaRepository<BlockIR, Long> {
}
