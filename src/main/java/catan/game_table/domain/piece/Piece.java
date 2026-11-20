package catan.game_table.domain.piece;

import catan.game_table.domain.Color;

import java.util.Locale;

public abstract class Piece {
    private final PieceData data =
            new PieceData(Color.NONE, getClass().getSimpleName().toLowerCase(Locale.ROOT));

    public PieceData getPieceData() {
        return data;
    }
}
