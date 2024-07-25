package net.deeptodo.app.domain;


import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Board implements Serializable {
    private String title;

    @Builder
    public Board(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(title, board.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }
}
