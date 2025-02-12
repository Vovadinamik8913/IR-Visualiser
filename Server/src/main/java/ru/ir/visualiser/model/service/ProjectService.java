package ru.ir.visualiser.model.service;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ir.visualiser.model.classes.Project;
import ru.ir.visualiser.model.repository.ProjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project create(Project project) {
        return projectRepository.save(project);
    }

    @Nullable
    public Project findByName(String name) {
        return projectRepository.findByName(name).orElse(null);
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }
}
