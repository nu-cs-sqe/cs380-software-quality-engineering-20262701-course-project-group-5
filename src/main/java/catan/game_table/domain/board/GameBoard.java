package catan.game_table.domain.board;

import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceData;
import catan.mvc_util.Observed;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class GameBoard extends Observed {
    private final Set<Hex> hexes;
    private static final int MIN_TOKEN_VALUE = 2;
    private static final int MAX_TOKEN_VALUE = 12;

    private final Set<Vertex> vertices;
    private final Set<Edge> edges;
    private final Map<Vertex, Set<Vertex>> neighbors = new HashMap<>();

    //once we build real boards, this will be cleaned up. For now, this is the bare-minimum code.
    public GameBoard(Set<Hex> hexes) {
        this(hexes, Set.of(), Set.of());
    }

    public GameBoard(Set<Hex> hexes, Set<Vertex> vertices, Set<Edge> edges) {
        this.hexes = Set.copyOf(hexes);
        this.vertices = Set.copyOf(vertices);
        this.edges = Set.copyOf(edges);
        createNeighbors();
    }

    private void createNeighbors() {
        vertices.forEach(vertex -> neighbors.put(vertex, new HashSet<>()));
        Map<Point, Vertex> vertexAt = indexVerticesByPoint();
        edges.forEach(edge -> linkEndpointsOf(edge, vertexAt));
    }

    private Map<Point, Vertex> indexVerticesByPoint() {
        return vertices.stream().collect(toMap(Vertex::getPoint, Function.identity()));
    }

    private void linkEndpointsOf(Edge edge, Map<Point, Vertex> vertexAt) {
        List<Vertex> ends = edge.getEndpoints().stream().map(vertexAt::get).toList();
        link(ends.get(0), ends.get(1));
    }

    private void link(Vertex first, Vertex second) {
        neighbors.get(first).add(second);
        neighbors.get(second).add(first);
    }

    public Set<Vertex> getNeighbors(Vertex vertex) {
        return Set.copyOf(neighbors.get(vertex));
    }

    public void produceResourcesForToken(int tokenValue) {
        requireValidTokenValue(tokenValue);
        for (Hex hex : hexes) {
            if (hex.getTokenValue() == tokenValue) {
                hex.produceResources();
            }
        }
    }

    private static void requireValidTokenValue(int tokenValue) {
        if (tokenValue < MIN_TOKEN_VALUE || tokenValue > MAX_TOKEN_VALUE) {
            throw new IllegalArgumentException(String.format(
                    "Token value %d is outside the valid range [%d, %d]",
                    tokenValue, MIN_TOKEN_VALUE, MAX_TOKEN_VALUE));
        }
    }

    public void addPieceAtLocation(Piece piece, Location location) {
        location.addPiece(piece);
        notifyObservers();
    }

    public void removePieceFromLocation(PieceData pieceData, Location location) {
        if (!location.holdsPieceWithData(pieceData)) {
            return;
        }
        location.removePieceWithData(pieceData);
        notifyObservers();
    }
}
