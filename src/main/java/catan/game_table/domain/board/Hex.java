package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceProducts;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public abstract class Hex extends Location {
    private static final int SIDE_COUNT = 6;

    public Hex(
            int tokenValue,
            Set<Vertex> vertices,
            Set<Edge> edges,
            ResourceProducts products) {
        requireSideCount(vertices, "vertices");
        requireSideCount(edges, "edges");
        requireEndpointsInVertices(vertices, edges);
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
