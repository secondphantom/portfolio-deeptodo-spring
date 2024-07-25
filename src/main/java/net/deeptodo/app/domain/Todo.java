package net.deeptodo.app.domain;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Todo implements Serializable {

    private String todoId;
    private String title;
    private boolean done;
    private boolean expand;
    private boolean enableCalendar;
    private boolean syncGoogleCalendar;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public Todo(boolean done, String title, boolean expand, String todoId, LocalDateTime startDate, LocalDateTime endDate, boolean enableCalendar, boolean syncGoogleCalendar) {
        this.todoId = todoId;
        this.title = title;
        this.done = done;
        this.expand = expand;
        this.enableCalendar = enableCalendar;
        this.syncGoogleCalendar = syncGoogleCalendar;
        this.startDate = startDate;
        this.endDate = endDate;
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
