package catan.game_table.domain.board;

import java.util.Set;

public class Edge extends Location {
    private final Set<Point> endpoints;

    public Edge(Point first, Point second) {
        endpoints = Set.of(first, second);
    }

    public Set<Point> getEndpoints() {
        return endpoints;
    }
}
