package catan.game_table.domain.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class EdgeTests {

    @Test
    void Constructor_WithSamePointForBothEndpoints_ThrowsIllegalArgumentException() {
        Point endpoint = new Point(1, 1);

        assertThrows(IllegalArgumentException.class, () -> new Edge(endpoint, endpoint));
    }
}
