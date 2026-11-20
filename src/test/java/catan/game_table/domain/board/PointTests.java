package catan.game_table.domain.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PointTests {

    @Test
    void Equals_WhenComparedToItself_ReturnsTrue() {
        Point point = new Point(1, 2);

        assertEquals(point, point);
    }

    @Test
    void Equals_WhenDistinctPointsHaveTheSameCoordinates_ReturnsTrue() {
        Point first = new Point(1, 2);
        Point second = new Point(1, 2);

        assertEquals(first, second);
    }
}
