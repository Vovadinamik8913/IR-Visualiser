package ru.ir.visualiser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ir.visualiser.model.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);
}
