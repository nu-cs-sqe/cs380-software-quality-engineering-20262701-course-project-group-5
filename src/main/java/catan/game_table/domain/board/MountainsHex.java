package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceProducts;

import java.util.Set;

public class MountainsHex extends Hex {
    public MountainsHex(
            int tokenValue,
            Set<Vertex> vertices,
            Set<Edge> edges,
            ResourceProducts products) {
        super(tokenValue, vertices, edges, products);
    }
}
