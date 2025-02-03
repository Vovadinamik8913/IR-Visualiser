package ru.ir.visualiser.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.classes.ir.Dot;

public interface DotRepository extends JpaRepository<Dot, Long> {
}
