package catan.game_table.domain.board;

import catan.game_table.domain.Color;
import catan.game_table.domain.GamePhase;
import catan.game_table.domain.GamePhaseHolder;
import catan.game_table.domain.piece.Building;
import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.Road;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlacementHelper {

    private final GameBoard gameBoard;
    private final GamePhaseHolder gamePhaseHolder;

    public PlacementHelper(GameBoard gameBoard, GamePhaseHolder gamePhaseHolder) {
        this.gameBoard = gameBoard;
        this.gamePhaseHolder = gamePhaseHolder;
    }

    public Set<Vertex> getValidBuildingSpots(Color color) {

        GamePhase gamePhase = gamePhaseHolder.getGamePhase();
        Set<Vertex> validVertices = new HashSet<>(gameBoard.getLandVertices());

        validVertices.removeAll(getVerticesInvalidUnderDistanceRule(validVertices));

        if (gamePhase == GamePhase.REGULAR) {
            validVertices.removeIf(vertex -> vertexHasNoRoadsOfColor(vertex, color));
        }

        return validVertices;

    }

    private Set<Vertex> getVerticesInvalidUnderDistanceRule(Set<Vertex> existingVertices) {

        Set<Vertex> verticesInvalidUnderDistanceRule = new HashSet<>();

        for (Vertex vertex : existingVertices) {
            if (vertexHasBuildingAlready(vertex)) {
                verticesInvalidUnderDistanceRule.add(vertex);
                verticesInvalidUnderDistanceRule.addAll(gameBoard.getNeighboringVertices(vertex));
            }
        }

        return verticesInvalidUnderDistanceRule;

    }

    private boolean vertexHasBuildingAlready(Vertex vertex) {
        List<Piece> pieces = vertex.getPieces();
        for (Piece piece : pieces) {
            if (piece instanceof Building) {
                return true;
            }
        }
        return false;
    }

    private boolean vertexHasNoRoadsOfColor(Vertex vertex, Color color) {
        Set<Edge> connectingEdges = gameBoard.getConnectingEdges(vertex);
        for (Edge edge : connectingEdges) {
            if (edgeHasRoadOfColor(edge, color)) {
                return false;
            }
        }
        return true;
    }

    private boolean edgeHasRoadOfColor(Edge edge, Color color) {
        List<Piece> pieces = edge.getPieces();
        for (Piece piece : pieces) {
            if (piece instanceof Road && piece.getPieceData().getColor() == color) {
                return true;
            }
        }
        return false;
    }

}
