package catan.game_table.domain.board;

import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceData;

import java.util.Set;

public class GameBoard {

    public Set<Vertex> getLandVertices() {
        return null;
    }

    public Set<Edge> getLandEdges() {
        return null;
    }

    public Set<Hex> getLandHexes() {
        return null;
    }

    public Set<Vertex> getAdjacentVertices(Vertex vertex) {
        return null;
    }

    public Set<Vertex> getEndpointVertices(Edge edge) {
        return null;
    }

    public Set<Edge> getIncidentEdges(Vertex vertex) {
        return null;
    }

    public Set<Edge> getAdjacentEdges(Edge edge) {
        return null;
    }

    public void addPieceAtLocation(Piece piece, Location location) {}

    public void removePieceFromLocation(PieceData pieceData, Location destinationLocation) {

    }

}
