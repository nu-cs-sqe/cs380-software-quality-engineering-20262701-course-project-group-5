package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceProducts;

import java.util.Set;

public abstract class Hex extends Location {
    private static final int SIDE_COUNT = 6;

    public Hex(
            int tokenValue,
            Set<Vertex> vertices,
            Set<Edge> edges,
            ResourceProducts products) {
        if (vertices.size() != SIDE_COUNT) {
            throw new IllegalArgumentException("A hex needs exactly " + SIDE_COUNT + " vertices");
        }
    }
}
