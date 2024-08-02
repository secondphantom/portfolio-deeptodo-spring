package net.deeptodo.app.domain;


import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Board implements Serializable {
    private String boardId;
    private String title;
    private boolean fold;

    @Builder
    public Board(String boardId, String title, boolean fold) {
        this.boardId = boardId;
        this.title = title;
        this.fold = fold;
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
