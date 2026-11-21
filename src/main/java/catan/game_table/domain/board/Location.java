package catan.game_table.domain.board;

import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceData;

public abstract class Location {
    public void addPiece(Piece piece){
    }
    public void removePieceWithData(PieceData data) {
    }
    public boolean holdsPieceWithData(PieceData data) {
        return false;
    }
}
