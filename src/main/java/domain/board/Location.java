package domain.board;

import domain.piece.Piece;
import domain.piece.PieceData;

public abstract class Location {
    public void addPiece(Piece piece){
    }
    public void removePieceWithData(PieceData data) {
    }
    public boolean holdsPieceWithData(PieceData data) {
        return false;
    }
}
