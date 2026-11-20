package catan.game_table.domain.piece;

import java.util.Locale;

public abstract class Piece {
    private final PieceData data = new PieceData(getClass().getSimpleName().toLowerCase(Locale.ROOT));

    public PieceData getPieceData() {
        return data;
    }
}
