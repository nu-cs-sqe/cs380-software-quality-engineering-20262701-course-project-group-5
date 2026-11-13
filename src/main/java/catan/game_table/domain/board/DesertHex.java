package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceProducts;

import java.util.List;
import java.util.Set;

public class DesertHex extends Hex {
    private static final int NO_TOKEN = 0;
    private static final ResourceProducts NO_PRODUCTS =
            new ResourceProducts(List.of(), List.of());

    public DesertHex(Set<Vertex> vertices, Set<Edge> edges) {
        super(NO_TOKEN, vertices, edges, NO_PRODUCTS);
        canProduce = false;
    }
}
