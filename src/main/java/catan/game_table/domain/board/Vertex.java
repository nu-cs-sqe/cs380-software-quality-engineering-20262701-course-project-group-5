package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceProducts;

public class Vertex extends Location {
    private final Point point;

    public Vertex(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void produceResources(ResourceProducts products) {
    }
}
