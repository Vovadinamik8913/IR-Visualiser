package ru.ir.visualiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.ir.BlockIR;

public interface BlockRepository extends JpaRepository<BlockIR, Long> {
}
