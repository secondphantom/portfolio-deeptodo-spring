package net.deeptodo.app.api.project.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import net.deeptodo.app.aop.auth.AuthArgumentResolver;
import net.deeptodo.app.aop.auth.dto.AuthUserInfo;
import net.deeptodo.app.api.RestDocsIntegration;
import net.deeptodo.app.api.project.application.ProjectService;
import net.deeptodo.app.api.project.dto.Pagination;
import net.deeptodo.app.api.project.dto.QueryProjectDto;
import net.deeptodo.app.api.project.dto.request.PartialUpdateProjectRequest;
import net.deeptodo.app.api.project.dto.response.CreateProjectResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectByIdResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectVersionByIdResponse;
import net.deeptodo.app.api.project.dto.response.GetProjectsByQueryResponse;
import net.deeptodo.app.common.config.JacksonConfig;
import net.deeptodo.app.domain.*;
import net.deeptodo.app.repository.project.ProjectRepository;
import net.deeptodo.app.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@Transactional
class ProjectControllerTest extends RestDocsIntegration {

    private final String URL_PATH = "/api/v1/projects";

    @MockBean
    private ProjectService projectService;

    @MockBean
    private AuthArgumentResolver authArgumentResolver;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JacksonConfig jacksonConfig;

    @Autowired
    private EntityManager em;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void beforeEach() throws Exception {
        AuthUserInfo authUserInfo = new AuthUserInfo(1L);
        given(authArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(authUserInfo);

    }

    public static class DateJson {
        private LocalDateTime date = LocalDateTime.now();
    }

    @Test
    public void createProject_success() throws Exception {
        //given
        CreateProjectResponse createProjectResponse = CreateProjectResponse.of(1L);

        given(projectService.createProject(any())).willReturn(createProjectResponse);

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(createProjectResponse.projectId()))
                .andDo(restDocs.document());
    }

    private User createUser () {
        User user = User.builder().build();;
        userRepository.create(user);
        em.flush();
        return userRepository.getById(user.getId()).get();
    }

    private Project createProject(Long projectId,User user) {

        Map<String, Board> boards = new HashMap<>();
        Board board1 = createBoard("board title 1");
        Board board2 = createBoard("board title 2");
        boards.put("boardId1", board1);
        boards.put("boardId2", board2);

        Map<String, Todo> todos = new HashMap<>();
        Todo todo1 = createTodo("todoId1", "todo title 1");
        Todo todo2 = createTodo("todoId2", "todo title 2");
        todos.put(todo1.getTodoId(), todo1);
        todos.put(todo2.getTodoId(), todo2);

        List root = List.of(List.of("boardId1", List.of("todoId1", "todoId2")), List.of("boardId1"));

        Project project = Project.builder()
                .id(projectId)
                .user(user)
                .version(0)
                .title("project title")
                .root(root)
                .boards(boards)
                .todos(todos)
                .build();
        projectRepository.create(project);
        return projectRepository.getById(project.getId()).get();
    }

    private Board createBoard(String title) {
        return Board.builder()
                .title(title)
                .build();
    }

    private Todo createTodo(String todoId, String title) {
        return Todo.builder()
                .todoId(todoId)
                .title(title)
                .done(true)
                .expand(false)
                .enableCalendar(true)
                .syncGoogleCalendar(false)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
    }

    @Test
    public void getProjectById_success() throws Exception {
        //given
        User user = createUser();
        Project project = createProject(1L,user);
        GetProjectByIdResponse getProjectByIdResponse = GetProjectByIdResponse.fromProject(project);

        given(projectService.getProjectById(any(),any())).willReturn(getProjectByIdResponse);

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(getProjectByIdResponse.projectId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").value(getProjectByIdResponse.version()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(getProjectByIdResponse.title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.root").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.boards").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.boards.boardId1.title").value(getProjectByIdResponse.boards().get("boardId1").title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.todos").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$.todos.todoId1.done").value(getProjectByIdResponse.todos().get("todoId1").done()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.todos.todoId1.title").value(getProjectByIdResponse.todos().get("todoId1").title()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.todos.todoId1.expand").value(getProjectByIdResponse.todos().get("todoId1").expand()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.todos.todoId1.todoId").value(getProjectByIdResponse.todos().get("todoId1").todoId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.todos.todoId1.startDate").value(getProjectByIdResponse.todos().get("todoId1").startDate().format(JacksonConfig.dateTimeFormatter)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.todos.todoId1.endDate").value(getProjectByIdResponse.todos().get("todoId1").endDate().format(JacksonConfig.dateTimeFormatter)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.todos.todoId1.enableCalendar").value(getProjectByIdResponse.todos().get("todoId1").enableCalendar()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.todos.todoId1.syncGoogleCalendar").value(getProjectByIdResponse.todos().get("todoId1").syncGoogleCalendar()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").value(getProjectByIdResponse.createdAt().format(JacksonConfig.dateTimeFormatter)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").value(getProjectByIdResponse.updatedAt().format(JacksonConfig.dateTimeFormatter)))
                .andDo(restDocs.document());

    }

    @Test
    public void updateProjectById_success() throws Exception {
        //given
        GetProjectVersionByIdResponse getProjectVersionByIdResponse = GetProjectVersionByIdResponse.of(1L, 1);

        given(projectService.updateProjectById(any(), any(), any())).willReturn(getProjectVersionByIdResponse);

        PartialUpdateProjectRequest body = PartialUpdateProjectRequest.builder()
                .version(0)
                .build();

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.patch(URL_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(getProjectVersionByIdResponse.projectId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").value(getProjectVersionByIdResponse.version()))
                .andDo(restDocs.document());

    }

    @Test
    public void deleteProjectById_success() throws Exception {
        //given
        willDoNothing().given(projectService).deleteProjectById(any(), any());

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_PATH + "/1")

                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(restDocs.document());

    }

    @Test
    public void getProjectVersionById_success() throws Exception {
        //given
        GetProjectVersionByIdResponse getProjectVersionByIdResponse = GetProjectVersionByIdResponse.of(1L, 1);

        given(projectService.getProjectVersionById(any(), any())).willReturn(getProjectVersionByIdResponse);

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH + "/1/version")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projectId").value(getProjectVersionByIdResponse.projectId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").value(getProjectVersionByIdResponse.version()))
                .andDo(restDocs.document());
    }

    @Test
    public void getProjectsByQuery_success() throws Exception {
        //given
        GetProjectsByQueryResponse response = GetProjectsByQueryResponse.of(
                List.of(
                        new QueryProjectDto(1L, "title", LocalDateTime.now(), LocalDateTime.now())
                ),
                Pagination.builder().pageSize(10).currentPage(1).build()
        );

        given(projectService.getProjectsByQuery(any(), any())).willReturn(response);

        System.out.println("objectMapper.writeValueAsString() response = " + objectMapper.writeValueAsString(response));

        //when & then
        mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH)
                        .param("page", "1")
                        .param("recent", "old")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.projects[0].projectId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.projects[0].title").value("title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pagination.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pagination.currentPage").value(1))
                .andDo(restDocs.document());

    }

}