package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceProducts;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HexTests {
    private static final int TOKEN_VALUE = 8;
    private static final ResourceProducts PRODUCTS =
            new ResourceProducts(List.of(), List.of());
    private static final List<Point> CORNERS = List.of(
            new Point(1, 0),
            new Point(2, 0),
            new Point(3, 1),
            new Point(2, 2),
            new Point(1, 2),
            new Point(0, 1));

    @Test
    void Constructor_WithClosedRingOfSixVertices_CreatesHex() {
        assertDoesNotThrow(() -> forestHex(sixVertices(), ring()));
    }

    private static Hex forestHex(Set<Vertex> vertices, Set<Edge> edges) {
        return new ForestHex(TOKEN_VALUE, vertices, edges, PRODUCTS);
    }

    private static Set<Vertex> sixVertices() {
        return CORNERS.stream()
                .map(Vertex::new)
                .collect(toCollection(LinkedHashSet::new));
    }

    private static Set<Edge> ring() {
        return new LinkedHashSet<>(List.of(
                edge(0, 1),
                edge(1, 2),
                edge(2, 3),
                edge(3, 4),
                edge(4, 5),
                edge(5, 0)));
    }

    private static Edge edge(int from, int to) {
        return new Edge(CORNERS.get(from), CORNERS.get(to));
    }
}
