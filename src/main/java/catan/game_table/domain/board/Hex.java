package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceProducts;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public abstract class Hex extends Location {
    private static final int SIDE_COUNT = 6;
    private static final int EDGES_PER_CORNER = 2;

    public Hex(
            int tokenValue,
            Set<Vertex> vertices,
            Set<Edge> edges,
            ResourceProducts products) {
        requireSideCount(vertices, "vertices");
        requireSideCount(edges, "edges");
        requireEndpointsInVertices(vertices, edges);
        requireTwoEdgesPerCorner(vertices, edges);
    }

    private static void requireTwoEdgesPerCorner(Set<Vertex> vertices, Set<Edge> edges) {
        for (Vertex vertex : vertices) {
            long touchingEdges = edges.stream()
                    .filter(edge -> edge.getEndpoints().contains(vertex.getPoint()))
                    .count();
            if (touchingEdges != EDGES_PER_CORNER) {
                throw new IllegalArgumentException(
                        "Every corner must touch exactly " + EDGES_PER_CORNER + " edges");
            }
        }
    }

    private static void requireEndpointsInVertices(Set<Vertex> vertices, Set<Edge> edges) {
        Set<Point> corners = vertices.stream().map(Vertex::getPoint).collect(toSet());
        for (Edge edge : edges) {
            if (!corners.containsAll(edge.getEndpoints())) {
                throw new IllegalArgumentException("Every edge endpoint must be a vertex of the hex");
            }
        }
    }

    private static void requireSideCount(Set<?> elements, String name) {
        if (elements.size() != SIDE_COUNT) {
            throw new IllegalArgumentException("A hex needs exactly " + SIDE_COUNT + " " + name);
        }
    }
}
