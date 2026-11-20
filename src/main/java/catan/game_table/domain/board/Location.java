package catan.game_table.domain.board;

import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceData;

import java.util.ArrayList;
import java.util.List;

public abstract class Location {
    protected final List<Piece> pieces = new ArrayList<>();

    public List<Piece> getPieces() {
        return List.copyOf(pieces);
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public void removePieceWithData(PieceData data) {
    }
    public boolean holdsPieceWithData(PieceData data) {
        return false;
    }
}
