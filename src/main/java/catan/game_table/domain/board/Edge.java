package catan.game_table.domain.board;

import java.util.Set;

public class Edge extends Location {
    private final Set<Point> endpoints;

    public Edge(Point first, Point second) {
        this(validatedEndpoints(first, second));
    }

    private Edge(Set<Point> endpoints) {
        this.endpoints = Set.copyOf(endpoints);
    }

    public Set<Point> getEndpoints() {
        return endpoints;
    }

    private static Set<Point> validatedEndpoints(Point first, Point second) {
        long horizontalDistance = Math.abs((long) first.x() - second.x());
        long verticalDistance = Math.abs((long) first.y() - second.y());
        if (horizontalDistance + verticalDistance != 1) {
            throw new IllegalArgumentException("Edge endpoints must be one step apart horizontally or vertically");
        }
        return Set.of(first, second);
    }
}
