package net.deeptodo.app.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProjectRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testJsonbFields() throws Exception {
        // given
        User user = User.createNewUser(
                "nickName",
                "email",
                "oauthServerId",
                OauthServerType.GOOGLE
        ); // Assuming User entity is properly defined
        userRepository.create(user);

        Map<String, Board> boards = new HashMap<>();
        Board board = new Board();
        board.setTitle("Sample Board");
        boards.put("board1", board);
        boards.put("board2", board);

        Map<String, Todo> todos = new HashMap<>();
        Todo todo = new Todo();
        todo.setTitle("Sample Todo");
        todo.setDone(false);
        todo.setExpand(true);
        todo.setTodoId("todo1");
        todo.setStartDate(LocalDateTime.now());
        todo.setEndDate(LocalDateTime.now());
        todo.setEnableCalendar(true);
        todo.setSyncGoogleCalendar(false);
        todos.put("todo1", todo);

        Project project = Project.builder()
                .user(user)
                .title("Sample Project")
                .root("root")
                .boards(boards)
                .todos(todos)
                .build();

        projectRepository.create(project);

        // When
        Project foundProject = projectRepository.getById(project.getId()).orElse(null);

        // then
        assertThat(foundProject).isNotNull();
        assertThat(foundProject.getBoards()).isEqualTo(boards);
        assertThat(foundProject.getTodos()).isEqualTo(todos);

    }
}