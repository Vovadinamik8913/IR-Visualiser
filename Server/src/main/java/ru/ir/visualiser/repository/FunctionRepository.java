package ru.ir.visualiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.ir.FunctionIR;

public interface FunctionRepository extends JpaRepository<FunctionIR, Long> {
}
