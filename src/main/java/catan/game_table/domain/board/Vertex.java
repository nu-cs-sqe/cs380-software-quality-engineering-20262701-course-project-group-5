package catan.game_table.domain.board;

import catan.game_table.domain.LocationType;
import catan.game_table.domain.piece.Building;
import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.resource.ResourceStack;

import java.util.List;

public class Vertex extends Location {
    private final Point point;

    public Vertex(Point point) {
        this.point = point;
    }

    @Override
    public LocationType getLocationType() {
        return LocationType.VERTEX;
    }

    public Point getPoint() {
        return point;
    }

    public void produceResources(List<ResourceStack> products) {
        for (Piece piece : pieces) {
            Building building = (Building) piece;
            building.produceResources(products);
        }
    }
}
