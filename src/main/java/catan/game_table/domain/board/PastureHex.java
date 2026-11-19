package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceStack;

import java.util.List;
import java.util.Set;

public class PastureHex extends Hex {
    public PastureHex(
            int tokenValue,
            Set<Vertex> vertices,
            Set<Edge> edges,
            List<ResourceStack> products) {
        super(tokenValue, vertices, edges, products);
    }
}
