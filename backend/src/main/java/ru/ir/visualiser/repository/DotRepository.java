package ru.ir.visualiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.ir.Dot;

public interface DotRepository extends JpaRepository<Dot, Long> {
}
