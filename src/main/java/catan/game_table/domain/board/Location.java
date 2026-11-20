package catan.game_table.domain.board;

import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceData;

import java.util.ArrayList;
import java.util.Iterator;
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
        Iterator<Piece> iterator = pieces.iterator();
        while (iterator.hasNext()) {
            Piece piece = iterator.next();
            if (data.equals(piece.getPieceData())) {
                iterator.remove();
                return;
            }
        }
    }

    public boolean holdsPieceWithData(PieceData data) {
        return pieces.stream().anyMatch(piece -> data.equals(piece.getPieceData()));
    }
}
