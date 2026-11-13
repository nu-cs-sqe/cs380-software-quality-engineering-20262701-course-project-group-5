package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceProducts;

import java.util.Set;

public class FieldsHex extends Hex {
    public FieldsHex(
            int tokenValue,
            Set<Vertex> vertices,
            Set<Edge> edges,
            ResourceProducts products) {
        super(tokenValue, vertices, edges, products);
    }
}
