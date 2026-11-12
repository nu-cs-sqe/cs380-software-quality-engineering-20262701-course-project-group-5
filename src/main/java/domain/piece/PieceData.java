package domain.piece;

import java.util.Objects;

public class PieceData {

    private final String type;

    public PieceData(String type) {
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
        return Objects.equals(type, otherOne.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
