package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceProducts;

import java.util.Set;

public abstract class Hex extends Location {
    public Hex(
            int tokenValue,
            Set<Vertex> vertices,
            Set<Edge> edges,
            ResourceProducts products) {
        if (vertices.isEmpty()) {
            throw new IllegalArgumentException("A hex needs exactly 6 vertices");
        }
    }
}
