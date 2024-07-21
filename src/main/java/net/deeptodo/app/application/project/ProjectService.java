package net.deeptodo.app.application.project;

import lombok.RequiredArgsConstructor;
import net.deeptodo.app.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

}
