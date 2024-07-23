package net.deeptodo.app.repository.project;

import jakarta.persistence.EntityManager;
import net.deeptodo.app.domain.*;
import net.deeptodo.app.repository.project.dto.PartialUpdateProjectByIdAndUserIdDto;
import net.deeptodo.app.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProjectNativeQueryRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectNativeQueryRepository projectNativeQueryRepository;

    @Test
    public void partialUpdateByIdAndUserId_success_not_need_update() throws Exception {
        //given
        User newUser = User.createNewUser(
                "nickName",
                "email",
                "oauthServerId",
                OauthServerType.GOOGLE
        );
        userRepository.create(newUser);

        Project newProject = Project.builder()
                .title("new")
                .version(1)
                .user(newUser)
                .build();

        projectRepository.create(newProject);

        em.flush();
        em.clear();
        //when
        PartialUpdateProjectByIdAndUserIdDto dto = PartialUpdateProjectByIdAndUserIdDto.builder()
                .projectId(newProject.getId())
                .userId(newUser.getId())
                .version(2)
                .title(null)
                .root(null)
                .boards(null)
                .todos(null).build();

        projectNativeQueryRepository.partialUpdateByIdAndUserId(dto);

        //then
        em.clear();

        Project findProject = projectRepository.getByIdAndUserId(newProject.getId(), newProject.getId()).get();

        assertThat(findProject.getVersion()).isEqualTo(newProject.getVersion());

    }

    @Test
    public void partialUpdateByIdAndUserId_success() throws Exception {

        //given
        List root = List.of();

        Map<String, Board> boards = new HashMap<>();
        Board board = Board.builder().title("new").build();
        boards.put("board1", board);
        boards.put("board2", board);

        Map<String, Todo> todos = new HashMap<>();
        Todo todo = Todo.builder().title("new").build();
        todos.put("todo1", todo);
        todos.put("todo2", todo);
        todos.put("todo3", todo);

        User newUser = User.createNewUser(
                "nickName",
                "email",
                "oauthServerId",
                OauthServerType.GOOGLE
        );
        userRepository.create(newUser);

        Project newProject = Project.builder()
                .title("new")
                .version(1)
                .root(root)
                .user(newUser)
                .boards(boards)
                .todos(todos)
                .build();

        projectRepository.create(newProject);


        em.flush();
        em.clear();

        //when
        Map<String, Board> updatedBoards = new HashMap<>();
        Board updatedBoard = Board.builder().title("update").build();
        updatedBoards.put("board1", updatedBoard);
        updatedBoards.put("board2", null);

        Map<String, Todo> updatedTodos = new HashMap<>();
        Todo updatedTodo = Todo.builder().title("updated").build();
        updatedTodos.put("todo1", updatedTodo);
        updatedTodos.put("todo2", null);
        updatedTodos.put("todo3", null);

        Integer updatedVersion = 2;
        String updatedTitle = "updated";

        PartialUpdateProjectByIdAndUserIdDto dto = PartialUpdateProjectByIdAndUserIdDto.builder()
                .projectId(newProject.getId())
                .userId(newUser.getId())
                .version(updatedVersion)
                .title(updatedTitle)
                .root(List.of("key", List.of()))
                .boards(updatedBoards)
                .todos(updatedTodos).build();

        projectNativeQueryRepository.partialUpdateByIdAndUserId(dto);

        //then
        em.clear();

        Project findProject = projectRepository.getByIdAndUserId(newProject.getId(), newProject.getId()).get();


        assertThat(findProject.getVersion()).isEqualTo(updatedVersion);
        assertThat(findProject.getTitle()).isEqualTo(updatedTitle);

        assertThat(findProject.getBoards().get("board1")).isEqualTo(updatedBoards.get("board1"));
        assertThat(findProject.getBoards().containsKey("board2")).isEqualTo(false);

        assertThat(findProject.getTodos().get("todo1")).isEqualTo(updatedTodos.get("todo1"));
        assertThat(findProject.getTodos().containsKey("todo2")).isEqualTo(false);
        assertThat(findProject.getTodos().containsKey("todo3")).isEqualTo(false);

    }
}