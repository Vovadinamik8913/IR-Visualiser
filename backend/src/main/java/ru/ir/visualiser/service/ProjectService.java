package ru.ir.visualiser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ir.visualiser.model.Project;
import ru.ir.visualiser.repository.ProjectRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project create(Project project) {
        return projectRepository.save(project);
    }

    public Project find(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project findByName(String name) {
        return projectRepository.findByName(name).orElse(null);
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
