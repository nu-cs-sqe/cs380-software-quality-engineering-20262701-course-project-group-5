package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceStack;

import java.util.List;
import java.util.Set;

public class DesertHex extends Hex {
    private static final int NO_TOKEN = 0;
    private static final List<ResourceStack> NO_PRODUCTS = List.of();

    public DesertHex(Set<Vertex> vertices, Set<Edge> edges) {
        super(NO_TOKEN, vertices, edges, NO_PRODUCTS);
        canProduce = false;
    }

    @Override
    public void setCanProduce(boolean canProduce) {
    }
}
