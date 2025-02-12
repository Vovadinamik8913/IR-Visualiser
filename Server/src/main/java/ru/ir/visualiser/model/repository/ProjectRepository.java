package ru.ir.visualiser.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.classes.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);
}
