package catan.game_table.domain.board;

import catan.game_table.domain.Color;
import catan.game_table.domain.GamePhase;
import catan.game_table.domain.GamePhaseHolder;
import catan.game_table.domain.exception.InvalidPlacementException;
import catan.game_table.domain.piece.Building;
import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceData;
import catan.game_table.domain.piece.PieceType;
import catan.mvc_util.Observed;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PlacementHelper extends Observed {

    private final GameBoard gameBoard;
    private final GamePhaseHolder gamePhaseHolder;

    private Piece cachedPiece;
    private Set<? extends Location> cachedValidSpots;

    public PlacementHelper(GameBoard gameBoard, GamePhaseHolder gamePhaseHolder) {
        this.gameBoard = gameBoard;
        this.gamePhaseHolder = gamePhaseHolder;
    }

    public Set<? extends Location> getAndCacheValidSpots(Piece inputPiece) {

        if (cachedPiece == inputPiece) {
            return cachedValidSpots;
        }

        cachedPiece = inputPiece;

        cachedValidSpots = getValidSpots(inputPiece);

        //notifyObservers();

        return cachedValidSpots;

    }

    Set<Vertex> getValidSettlementSpots(Color color) {

        GamePhase gamePhase = gamePhaseHolder.getGamePhase();
        Set<Vertex> validVertices = new HashSet<>(gameBoard.getLandVertices());

        validVertices.removeAll(getVerticesInvalidUnderDistanceRule(validVertices));

        if (gamePhase == GamePhase.REGULAR) {
            validVertices.removeIf(vertex -> !vertexHasRoadsOfColor(vertex, color));
        }

        return validVertices;

    }

    Set<Vertex> getValidCitySpots(Color color) {

        GamePhase gamePhase = gamePhaseHolder.getGamePhase();

        if (gamePhase == GamePhase.SETUP) {
            return new HashSet<>();
        }

        Set<Vertex> validVertices = new HashSet<>(gameBoard.getLandVertices());

        validVertices.removeIf(vertex -> !locationHasPieceOfTypeAndColor(vertex, PieceType.SETTLEMENT, color));

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

        Optional<Set<? extends Location>> optionalValidLocations = tryGetValidSpots(piece);
        Set<? extends Location> validLocations;

        if (optionalValidLocations.isPresent()) {

            validLocations = optionalValidLocations.get();

            PieceData pieceData = piece.getPieceData();
            PieceType pieceType = pieceData.getPieceType();

            if (!validLocations.contains(location)) {
                String pieceTypeName = pieceType.toString().toLowerCase();
                String locationTypeName = location.getLocationType().toString().toLowerCase();
                String message = String.format("Cannot place %s at %s", pieceTypeName, locationTypeName);
                throw new InvalidPlacementException(message);
            }

            if (pieceType == PieceType.CITY) {
                PieceData settlementData = getFirstPieceDataWithType(location, PieceType.SETTLEMENT);
                gameBoard.removePieceFromLocation(settlementData, location);
            }

        }

        gameBoard.addPieceAtLocation(piece, location);

        notifyObserversInternal();

    }

    void notifyObserversInternal() {
        notifyObservers();
    }

    private Set<Vertex> getVerticesInvalidUnderDistanceRule(Set<Vertex> existingVertices) {

        Set<Vertex> verticesInvalidUnderDistanceRule = new HashSet<>();

        for (Vertex vertex : existingVertices) {
            if (locationHasBuilding(vertex)) {
                verticesInvalidUnderDistanceRule.add(vertex);
                verticesInvalidUnderDistanceRule.addAll(gameBoard.getNeighboringVertices(vertex));
            }
        }

        return verticesInvalidUnderDistanceRule;

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

    private boolean locationHasPieceOfTypeAndColor(Location location, PieceType pieceType, Color pieceColor) {
        List<Piece> pieces = location.getPieces();
        for (Piece piece : pieces) {
            PieceData existingPieceData = piece.getPieceData();
            PieceType existingPieceType = existingPieceData.getPieceType();
            Color existingPieceColor = existingPieceData.getColor();
            if (pieceType == existingPieceType && pieceColor == existingPieceColor) {
                return true;
            }
        }
        return false;
    }

    private boolean locationHasBuilding(Location location) {
        List<Piece> pieces = location.getPieces();
        for (Piece piece : pieces) {
            if (piece instanceof Building) {
                return true;
            }
        }
        return false;
    }

    private boolean locationHasBuildingOfColor(Location location, Color pieceColor) {
        List<Piece> pieces = location.getPieces();
        for (Piece piece : pieces) {
            if (piece instanceof Building) {
                PieceData existingPieceData = piece.getPieceData();
                Color existingPieceColor = existingPieceData.getColor();
                return existingPieceColor == pieceColor;
            }
        }
        return false;
    }

    private boolean vertexHasRoadsOfColor(Vertex vertex, Color color) {
        Set<Edge> connectingEdges = gameBoard.getConnectingEdges(vertex);
        for (Edge edge : connectingEdges) {
            if (edgeHasRoadOfColor(edge, color)) {
                return true;
            }
        }
        return false;
    }

    private boolean edgeHasRoadOfColor(Edge edge, Color color) {
        return locationHasPieceOfTypeAndColor(edge, PieceType.ROAD, color);
    }

    private boolean edgeHasRoadAlready(Edge edge) {
        return locationHasPieceOfType(edge, PieceType.ROAD);
    }

    private boolean edgeTouchingSameColorPiece(Edge edge, Color color) {

        Set<Vertex> endpointVertices = gameBoard.getNeighboringVertices(edge);
        for (Vertex vertex : endpointVertices) {
            if (locationHasBuildingOfColor(vertex, color)) {
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
            if (locationHasBuilding(vertex) && !locationHasBuildingOfColor(vertex, color)) {
                blockedEdges.addAll(gameBoard.getConnectingEdges(vertex));
            }
        }

        blockedEdges.remove(edge);

        return blockedEdges;

    }

    private boolean edgeTouchingJustPlacedBuilding(Edge edge, Color color) {
        Set<Vertex> endpointVertices = gameBoard.getNeighboringVertices(edge);
        for (Vertex vertex : endpointVertices) {
            if (locationHasBuildingOfColor(vertex, color)) {
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

    private Set<? extends Location> getValidSpots(Piece piece) {

        PieceData pieceData = piece.getPieceData();
        PieceType pieceType = pieceData.getPieceType();
        Color pieceColor = pieceData.getColor();

        switch (pieceType) {
            case PieceType.SETTLEMENT -> {
                return getValidSettlementSpots(pieceColor);
            }
            case PieceType.ROAD -> {
                return getValidRoadSpots(pieceColor);
            }
            case PieceType.CITY -> {
                return getValidCitySpots(pieceColor);
            }
            case PieceType.ROBBER -> {
                return getValidRobberSpots();
            }
            default -> {
                throw new IllegalArgumentException("No defined behavior exists for this piece type");
            }
        }

    }

    private Optional<Set<? extends Location>> tryGetValidSpots(Piece piece) {
        try {
            return Optional.of(getValidSpots(piece));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
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
