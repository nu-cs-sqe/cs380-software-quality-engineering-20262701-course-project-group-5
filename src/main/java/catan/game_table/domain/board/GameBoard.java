package catan.game_table.domain.board;

import catan.game_table.domain.piece.Piece;

import java.util.Set;

public class GameBoard {

    public Set<Vertex> getLandVertices() {
        return null;
    }

    public Set<Edge> getLandEdges() {
        return null;
    }

    public Set<Edge> getConnectingEdges(Location location) {
        return null;
    }

    public Set<Vertex> getNeighboringVertices(Location location) {
        return null;
    }

    public void addPieceAtLocation(Piece piece, Location location) {}
}
