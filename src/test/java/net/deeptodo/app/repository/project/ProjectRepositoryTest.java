package net.deeptodo.app.repository.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.*;
import net.deeptodo.app.repository.user.UserRepository;
import net.deeptodo.app.testutils.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProjectRepositoryTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testJsonbFields() throws Exception {
        // given
        SubscriptionPlan plan = EntityUtils.createDefaultPlan(SubscriptionPlan.builder().build(), 1L);
        em.persist(plan);
        User user = EntityUtils.createNewUser(plan);
        em.persist(user);
        em.flush();

        Map<String, Board> boards = new HashMap<>();
        Board board = Board.builder().title("Sample Board").build();
        boards.put("board1", board);
        boards.put("board2", board);

        Map<String, Todo> todos = new HashMap<>();
        Todo todo = Todo.builder()
                .title("Sample Todo")
                .done(false)
                .startDate(LocalDateTime.now())
                .build();
        todos.put("todo1", todo);

        List root = List.of("boardId");


        Project project = EntityUtils.createDefaultProject(Project.builder()
                .title("Sample Project")
                .root(root)
                .boards(boards)
                .todos(todos)
                .build(), user);
        em.persist(project);
        em.flush();
        em.clear();

        // When
        Project foundProject = projectRepository.getById(project.getId()).orElse(null);

        // then
        assertThat(foundProject).isNotNull();
        assertThat(foundProject.getBoards()).isEqualTo(boards);
        assertThat(foundProject.getTodos()).isEqualTo(todos);
        assertThat(foundProject.getRoot()).isEqualTo(root);

    }
}