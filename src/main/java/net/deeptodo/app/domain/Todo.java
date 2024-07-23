package net.deeptodo.app.domain;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Todo implements Serializable {

    private boolean done;
    private String title;
    private boolean expand;
    private String todoId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean enableCalendar;
    private boolean syncGoogleCalendar;

    @Builder
    public Todo(boolean done, String title, boolean expand, String todoId, LocalDateTime startDate, LocalDateTime endDate, boolean enableCalendar, boolean syncGoogleCalendar) {
        this.done = done;
        this.title = title;
        this.expand = expand;
        this.todoId = todoId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.enableCalendar = enableCalendar;
        this.syncGoogleCalendar = syncGoogleCalendar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return done == todo.done && expand == todo.expand && enableCalendar == todo.enableCalendar && syncGoogleCalendar == todo.syncGoogleCalendar && Objects.equals(title, todo.title) && Objects.equals(todoId, todo.todoId) && Objects.equals(startDate, todo.startDate) && Objects.equals(endDate, todo.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(done, title, expand, todoId, startDate, endDate, enableCalendar, syncGoogleCalendar);
    }
}
