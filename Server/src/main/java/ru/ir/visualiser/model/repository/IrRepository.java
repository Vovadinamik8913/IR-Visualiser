package ru.ir.visualiser.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.classes.Ir;

public interface IrRepository extends JpaRepository<Ir, Long> { }
