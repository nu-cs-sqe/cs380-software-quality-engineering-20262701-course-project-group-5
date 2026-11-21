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

    public Set<Edge> getValidRoadSpots(Color color) {

        GamePhase gamePhase = gamePhaseHolder.getGamePhase();
        Set<Edge> validEdges = new HashSet<>(gameBoard.getLandEdges());

        validEdges.removeIf(this::edgeHasRoadAlready);

        validEdges.removeIf(edge -> !edgeTouchingSameColorPiece(edge, color));

        if (gamePhase == GamePhase.SETUP) {
            validEdges.removeIf(edge -> !edgeTouchingJustPlacedBuilding(edge, color));
        }

        return validEdges;

    }

    public void place(Piece piece, Location location) {

        gameBoard.addPieceAtLocation(piece, location);

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

    private boolean edgeHasRoadAlready(Edge edge) {
        List<Piece> pieces = edge.getPieces();
        for (Piece piece : pieces) {
            if (piece instanceof Road) {
                return true;
            }
        }
        return false;
    }

    private boolean edgeTouchingSameColorPiece(Edge edge, Color color) {

        Set<Vertex> endpointVertices = gameBoard.getNeighboringVertices(edge);
        for (Vertex vertex : endpointVertices) {
            if (vertexHasBuildingOfColor(vertex, color)) {
                return true;
            }
        }

        Set<Edge> connectingEdges = new HashSet<>(gameBoard.getConnectingEdges(edge));

        for (Edge connectingEdge : connectingEdges) {
            if (edgeHasRoadOfColor(connectingEdge, color)) {
                Set<Edge> blockedEdges = getBlockedConnectingEdges(edge, color, endpointVertices);
                return !blockedEdges.contains(connectingEdge);
            }
        }

        return false;

    }

    private Set<Edge> getBlockedConnectingEdges(Edge edge, Color color, Set<Vertex> endpointVertices) {

        Set<Edge> blockedEdges = new HashSet<>();

        for (Vertex vertex : endpointVertices) {
            if (vertexHasBuildingAlready(vertex) && !vertexHasBuildingOfColor(vertex, color)) {
                blockedEdges.addAll(gameBoard.getConnectingEdges(vertex));
            }
        }

        blockedEdges.remove(edge);

        return blockedEdges;

    }

    private boolean vertexHasBuildingOfColor(Vertex vertex, Color color) {
        List<Piece> pieces = vertex.getPieces();
        for (Piece piece : pieces) {
            if (piece instanceof Building && piece.getPieceData().getColor() == color) {
                return true;
            }
        }
        return false;
    }

    private boolean edgeTouchingJustPlacedBuilding(Edge edge, Color color) {
        Set<Vertex> endpointVertices = gameBoard.getNeighboringVertices(edge);
        for (Vertex vertex : endpointVertices) {
            if (vertexHasBuildingOfColor(vertex, color)) {
                return !vertexConnectedToRoadOfSameColor(vertex, color);
            }
        }
        return false;
    }

    private boolean vertexConnectedToRoadOfSameColor(Vertex vertex, Color color) {
        Set<Edge> connectingEdges = gameBoard.getConnectingEdges(vertex);
        for (Edge edge : connectingEdges) {
            if (edgeHasRoadOfColor(edge, color)) {
                return true;
            }
        }
        return false;
    }

}
