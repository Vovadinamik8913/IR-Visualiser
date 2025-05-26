package ru.ir.visualiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.Ir;

public interface IrRepository extends JpaRepository<Ir, Long> { }
