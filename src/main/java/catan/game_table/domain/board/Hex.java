package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceProducts;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public abstract class Hex extends Location {
    private static final int SIDE_COUNT = 6;
    private static final int EDGES_PER_CORNER = 2;
    private final int tokenValue;
    private final Set<Vertex> vertices;
    private final ResourceProducts products;
    protected boolean canProduce = true;

    public Hex(
            int tokenValue,
            Set<Vertex> vertices,
            Set<Edge> edges,
            ResourceProducts products) {
        requireClosedRing(vertices, edges);
        this.tokenValue = tokenValue;
        this.vertices = vertices;
        this.products = products;
    }

    public boolean canProduce() {
        return canProduce;
    }

    public int getTokenValue() {
        return tokenValue;
    }

    public void setCanProduce(boolean canProduce) {
        this.canProduce = canProduce;
    }

    public void produceResources() {
        vertices.forEach(vertex -> vertex.produceResources(products));
    }

    private static void requireClosedRing(Set<Vertex> vertices, Set<Edge> edges) {
        requireSideCount(vertices, "vertices");
        requireSideCount(edges, "edges");
        requireEndpointsInVertices(vertices, edges);
        requireTwoEdgesPerCorner(vertices, edges);
        requireSingleRing(vertices, edges);
    }

    private static void requireSingleRing(Set<Vertex> vertices, Set<Edge> edges) {
        Set<Point> visitedPoints = new HashSet<>();
        Deque<Point> pointsToVisit = new ArrayDeque<>();
        pointsToVisit.push(vertices.iterator().next().getPoint());
        while (!pointsToVisit.isEmpty()) {
            Point currentPoint = pointsToVisit.pop();
            if (visitedPoints.add(currentPoint)) {
                addConnectedPoints(currentPoint, edges, pointsToVisit);
            }
        }
        if (visitedPoints.size() != vertices.size()) {
            throw new IllegalArgumentException("The edges must form one closed ring");
        }
    }

    private static void addConnectedPoints(Point currentPoint, Set<Edge> edges, Deque<Point> pointsToVisit) {
        for (Edge edge : edges) {
            if (edge.getEndpoints().contains(currentPoint)) {
                pointsToVisit.addAll(edge.getEndpoints());
            }
        }
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
