package catan.game_table.domain.piece;

import catan.game_table.domain.Color;

import java.util.Objects;

public class PieceData {

    private final Color color;
    private final String type;

    public PieceData(Color color, String type) {
        this.color = color;
        this.type = type;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PieceData)) {
            return false;
        }
        PieceData otherOne = (PieceData) other;
        return color == otherOne.color && Objects.equals(type, otherOne.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}
