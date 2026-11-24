package catan.game_table.domain.board;

import catan.game_table.domain.Color;
import catan.game_table.domain.GamePhase;
import catan.game_table.domain.GamePhaseHolder;
import catan.game_table.domain.exception.InvalidPlacementException;
import catan.game_table.domain.piece.*;
import catan.mvc_util.Observed;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlacementHelper extends Observed {

    private final GameBoard gameBoard;
    private final GamePhaseHolder gamePhaseHolder;

    public PlacementHelper(GameBoard gameBoard, GamePhaseHolder gamePhaseHolder) {
        this.gameBoard = gameBoard;
        this.gamePhaseHolder = gamePhaseHolder;
    }

    Set<Vertex> getValidSettlementSpots(Color color) {

        GamePhase gamePhase = gamePhaseHolder.getGamePhase();
        Set<Vertex> validVertices = new HashSet<>(gameBoard.getLandVertices());

        validVertices.removeAll(getVerticesInvalidUnderDistanceRule(validVertices));

        if (gamePhase == GamePhase.REGULAR) {
            validVertices.removeIf(vertex -> vertexHasNoRoadsOfColor(vertex, color));
        }

        return validVertices;

    }

    Set<Vertex> getValidCitySpots(Color color) {

        GamePhase gamePhase = gamePhaseHolder.getGamePhase();

        if (gamePhase == GamePhase.SETUP) {
            return new HashSet<>();
        }

        Set<Vertex> validVertices = new HashSet<>(gameBoard.getLandVertices());

        validVertices.removeIf(vertex -> !vertexHasSettlementOfColor(vertex, color));

        return validVertices;

    }

    Set<Edge> getValidRoadSpots(Color color) {

        GamePhase gamePhase = gamePhaseHolder.getGamePhase();
        Set<Edge> validEdges = new HashSet<>(gameBoard.getLandEdges());

        validEdges.removeIf(this::edgeHasRoadAlready);

        validEdges.removeIf(edge -> !edgeTouchingSameColorPiece(edge, color));

        if (gamePhase == GamePhase.SETUP) {
            validEdges.removeIf(edge -> !edgeTouchingJustPlacedBuilding(edge, color));
        }

        return validEdges;

    }

    Set<Hex> getValidRobberSpots() {
        Set<Hex> validHexes = new HashSet<>(gameBoard.getLandHexes());
        validHexes.removeIf(hex -> locationHasPieceOfType(hex, PieceType.ROBBER));
        return validHexes;
    }

    public void place(Piece piece, Location location) throws InvalidPlacementException {

        switch (piece) {
            case Settlement settlement -> validateSettlementPlacement(piece, location);
            case City city -> {
                validateCityPlacement(piece, location);
                PieceData settlementData = getFirstPieceDataWithType(location, PieceType.SETTLEMENT);
                gameBoard.removePieceFromLocation(settlementData, location);
            }
            case Road road -> validateRoadPlacement(piece, location);
            case Robber robber -> validateRobberPlacement(piece, location);
            default -> {
            }
        }

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

    private boolean canDoCityUpgrade(Piece piece, Vertex vertex, Color color) {
        if (!(piece instanceof City)) {
            return false;
        }
        List<Piece> pieces = vertex.getPieces();
        for (Piece existingPiece : pieces) {
            if (existingPiece instanceof Settlement && existingPiece.getPieceData().getColor() == color) {
                return true;
            }
        }
        return false;
    }

    private boolean vertexHasSettlementOfColor(Vertex vertex, Color color) {
        List<Piece> pieces = vertex.getPieces();
        for (Piece piece : pieces) {
            if (piece instanceof Settlement && piece.getPieceData().getColor() == color) {
                return true;
            }
        }
        return false;
    }

    private boolean locationHasPieceOfType(Location location, PieceType pieceType) {
        List<Piece> pieces = location.getPieces();
        for (Piece piece : pieces) {
            PieceData existingPieceData = piece.getPieceData();
            PieceType existingPieceType = existingPieceData.getPieceType();
            if (pieceType == existingPieceType) {
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

    private void validateSettlementPlacement(Piece settlement, Location location) throws InvalidPlacementException {

        String pieceTypeName = settlement.getPieceData().getPieceType().toString().toLowerCase();
        String locationTypeName = location.getLocationType().toString().toLowerCase();

        Vertex vertex = enforceVertex(location, pieceTypeName, locationTypeName);

        Set<Vertex> validVertices = new HashSet<>(gameBoard.getLandVertices());
        if (!validVertices.contains(vertex)) {
            throw new InvalidPlacementException(
                    String.format(
                            "Cannot place %s at %s: %s is not on this board",
                            pieceTypeName,
                            locationTypeName,
                            locationTypeName
                    )
            );
        }

        if (vertexHasBuildingAlready(vertex)) {
            String existingPieceTypeName = getFirstPieceData(vertex).getPieceType().toString().toLowerCase();
            throw new InvalidPlacementException(
                    String.format(
                            "Cannot place %s at %s: %s already holds a %s",
                            pieceTypeName,
                            locationTypeName,
                            locationTypeName,
                            existingPieceTypeName
                    )
            );
        }

        validVertices.removeAll(getVerticesInvalidUnderDistanceRule(validVertices));

        if (!validVertices.contains(vertex)) {
            throw new InvalidPlacementException(
                    String.format(
                            "Cannot place %s at %s: too close to an existing building",
                            pieceTypeName,
                            locationTypeName
                    )
            );
        }

    }

    private void validateCityPlacement(Piece city, Location location) throws InvalidPlacementException {

        String pieceTypeName = city.getPieceData().getPieceType().toString().toLowerCase();
        String locationTypeName = location.getLocationType().toString().toLowerCase();

        Vertex vertex = enforceVertex(location, pieceTypeName, locationTypeName);

        if (location.getPieces().isEmpty()) {
            throw new InvalidPlacementException(
                    String.format(
                            "%s cannot be placed on empty %s",
                            pieceTypeName,
                            locationTypeName
                    )
            );
        }

        if (locationHasPieceOfType(location, PieceType.CITY)) {
            String existingPieceTypeName = getFirstPieceData(vertex).getPieceType().toString().toLowerCase();
            throw new InvalidPlacementException(
                    String.format(
                            "Cannot place %s at %s: %s already holds a %s",
                            pieceTypeName,
                            locationTypeName,
                            locationTypeName,
                            existingPieceTypeName
                    )
            );
        }

        if (!canDoCityUpgrade(city, vertex, city.getPieceData().getColor())) {
            throw new InvalidPlacementException(
                    String.format(
                            "Cannot place %s at %s: not your color",
                            pieceTypeName,
                            locationTypeName
                    )
            );
        }

    }

    private void validateRoadPlacement(Piece road, Location location) throws InvalidPlacementException {

        String pieceTypeName = road.getPieceData().getPieceType().toString().toLowerCase();
        String locationTypeName = location.getLocationType().toString().toLowerCase();

        Edge edge = enforceEdge(location, pieceTypeName, locationTypeName);

        if (locationHasPieceOfType(edge, PieceType.ROAD)) {
            String existingPieceTypeName = getFirstPieceData(location).getPieceType().toString().toLowerCase();
            throw new InvalidPlacementException(
                    String.format(
                            "Cannot place %s at %s: %s already holds a %s",
                            pieceTypeName,
                            locationTypeName,
                            locationTypeName,
                            existingPieceTypeName
                    )
            );
        }

    }

    private void validateRobberPlacement(Piece robber, Location location) throws InvalidPlacementException {

        String pieceTypeName = robber.getPieceData().getPieceType().toString().toLowerCase();
        String locationTypeName = location.getLocationType().toString().toLowerCase();

        Hex hex = enforceHex(location, pieceTypeName, locationTypeName);

        if (locationHasPieceOfType(hex, PieceType.ROBBER)) {
            String existingPieceTypeName = getFirstPieceData(location).getPieceType().toString().toLowerCase();
            throw new InvalidPlacementException(
                    String.format(
                            "Cannot place %s at %s: %s already holds a %s",
                            pieceTypeName,
                            locationTypeName,
                            locationTypeName,
                            existingPieceTypeName
                    )
            );
        }

    }

    private Vertex enforceVertex(Location location, String pieceTypeName, String locationTypeName) throws InvalidPlacementException {
        if (!(location instanceof Vertex vertex)) {
            throw new InvalidPlacementException(
                    String.format(
                            "Cannot place %s at %s: %s does not fit a %s",
                            pieceTypeName,
                            locationTypeName,
                            locationTypeName,
                            pieceTypeName
                    )
            );
        }
        return vertex;
    }

    private Edge enforceEdge(Location location, String pieceTypeName, String locationTypeName) throws InvalidPlacementException {
        if (!(location instanceof Edge edge)) {
            throw new InvalidPlacementException(
                    String.format(
                            "Cannot place %s at %s: %s does not fit a %s",
                            pieceTypeName,
                            locationTypeName,
                            locationTypeName,
                            pieceTypeName
                    )
            );
        }
        return edge;
    }

    private Hex enforceHex(Location location, String pieceTypeName, String locationTypeName) throws InvalidPlacementException {
        if (!(location instanceof Hex hex)) {
            throw new InvalidPlacementException(
                    String.format(
                            "Cannot place %s at %s: %s does not fit a %s",
                            pieceTypeName,
                            locationTypeName,
                            locationTypeName,
                            pieceTypeName
                    )
            );
        }
        return hex;
    }

    private PieceData getFirstPieceData(Location location) {
        List<Piece> locationPieces = location.getPieces();
        for (Piece piece : locationPieces) {
            return piece.getPieceData();
        }
        throw new RuntimeException();
    }

    private PieceData getFirstPieceDataWithType(Location location, PieceType pieceType) {
        List<Piece> locationPieces = location.getPieces();
        for (Piece piece : locationPieces) {
            if (piece.getPieceData().getPieceType() == pieceType)
                return piece.getPieceData();
        }
        throw new RuntimeException();
    }

}
