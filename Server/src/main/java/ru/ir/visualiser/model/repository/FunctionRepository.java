package ru.ir.visualiser.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.classes.ir.FunctionIR;

public interface FunctionRepository extends JpaRepository<FunctionIR, Long> {
}
