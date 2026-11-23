package catan.game_table.domain.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

    @Test
    void Equals_WhenOnlyXDiffers_ReturnsFalse() {
        Point first = new Point(1, 2);
        Point second = new Point(3, 2);

        assertNotEquals(first, second);
    }

    @Test
    void Equals_WhenOnlyYDiffers_ReturnsFalse() {
        Point first = new Point(1, 2);
        Point second = new Point(1, 3);

        assertNotEquals(first, second);
    }

    @Test
    void Equals_WhenComparedToString_ReturnsFalse() {
        Point point = new Point(1, 2);

        assertNotEquals("point", point);
    }

    @Test
    void HashCode_WhenDistinctPointsHaveTheSameCoordinates_ReturnsSameValue() {
        Point first = new Point(1, 2);
        Point second = new Point(1, 2);

        assertEquals(first.hashCode(), second.hashCode());
    }
}
