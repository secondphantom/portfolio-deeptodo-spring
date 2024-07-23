package net.deeptodo.app.repository.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import net.deeptodo.app.domain.Board;
import net.deeptodo.app.domain.Todo;
import net.deeptodo.app.repository.project.dto.PartialUpdateProjectByIdAndUserIdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProjectNativeQueryRepository {

    @Autowired
    private EntityManager em;

    @Autowired
    private ObjectMapper objectMapper;

    private record KeyWithParam(
            String key,
            Object value
    ) {
    }

    public void partialUpdateByIdAndUserId(
            PartialUpdateProjectByIdAndUserIdDto dto
    ) throws JsonProcessingException {

        List<String> setStrList = new ArrayList<>();
        List<KeyWithParam> keyWithparamList = new ArrayList<>();

        if (dto.version() != null) {
            setStrList.add("version = :version");
            keyWithparamList.add(
                    new KeyWithParam("version", dto.version())
            );
        }

        if (dto.title() != null) {
            setStrList.add("title = :title");
            keyWithparamList.add(
                    new KeyWithParam("title", dto.title())
            );
        }

        if (dto.root() != null) {
            setStrList.add("root = cast(:root as jsonb)");
            String rootJson = objectMapper.writeValueAsString(dto.root());
            keyWithparamList.add(
                    new KeyWithParam("root", rootJson)
            );
        }

        if (dto.boards() != null) {
            StringBuilder queryStr = new StringBuilder("boards = boards");

            List<String> nullKeys = dto.boards().entrySet().stream()
                    .filter(entry -> entry.getValue() == null)
                    .map(Map.Entry::getKey)
                    .toList();
            Map<String, Board> notNullMap = dto.boards().entrySet().stream()
                    .filter(entry -> entry.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (!nullKeys.isEmpty()) {
                for (String key : nullKeys) {
                    queryStr.append(" - :").append(key);
                    keyWithparamList.add(
                            new KeyWithParam(key, key)
                    );
                }
            }
            if (!notNullMap.isEmpty()) {
                String updateBoardJson = objectMapper.writeValueAsString(notNullMap);
                queryStr.append(" || cast(:updateBoardJson as jsonb)");
                keyWithparamList.add(
                        new KeyWithParam("updateBoardJson", updateBoardJson)
                );
            }
            setStrList.add((queryStr.toString()));
        }

        if (dto.todos() != null) {
            StringBuilder queryStr = new StringBuilder("todos = todos");

            List<String> nullKeys = dto.todos().entrySet().stream()
                    .filter(entry -> entry.getValue() == null)
                    .map(Map.Entry::getKey)
                    .toList();
            Map<String, Todo> notNullMap = dto.todos().entrySet().stream()
                    .filter(entry -> entry.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            if (!nullKeys.isEmpty()) {
                for (String key : nullKeys) {
                    queryStr.append(" - :").append(key);
                    keyWithparamList.add(
                            new KeyWithParam(key, key)
                    );
                }
            }
            if (!notNullMap.isEmpty()) {
                String updateTodoJson = objectMapper.writeValueAsString(notNullMap);
                queryStr.append(" || cast(:updateTodoJson as jsonb)");
                keyWithparamList.add(
                        new KeyWithParam("updateTodoJson", updateTodoJson)
                );
            }
            setStrList.add((queryStr.toString()));
        }

        boolean isNeedUpdate = setStrList.size() >= 2;
        System.out.println("isNeedUpdate = " + isNeedUpdate);
        if (!isNeedUpdate) {
            return;
        }

        String updateStr = "UPDATE projects ";
        String whereStr = " WHERE id = :projectId AND user_id = :userId";
        keyWithparamList.add(
                new KeyWithParam("projectId", dto.projectId())
        );
        keyWithparamList.add(
                new KeyWithParam("userId", dto.userId())
        );


        Query query = em.createNativeQuery(updateStr +
                "SET " + String.join(", ", setStrList) +
                whereStr
        );
        keyWithparamList.forEach(keyWithParam -> query.setParameter(keyWithParam.key, keyWithParam.value));


        query.executeUpdate();

    }
}
