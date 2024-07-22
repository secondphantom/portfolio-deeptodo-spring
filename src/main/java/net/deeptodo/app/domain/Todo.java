package net.deeptodo.app.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Todo implements Serializable {

    private boolean done;
    private String title;
    private boolean expand;
    private String todoId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean enableCalendar;
    private boolean syncGoogleCalendar;
}
