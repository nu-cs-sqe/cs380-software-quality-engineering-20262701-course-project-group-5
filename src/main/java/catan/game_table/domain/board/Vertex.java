package catan.game_table.domain.board;

public class Vertex extends Location {
    private final Point point;

    public Vertex(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }
}
