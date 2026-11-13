package catan.game_table.domain.board;

import catan.game_table.domain.resource.ResourceProducts;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void Constructor_WithNoVertices_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> forestHex(Set.of(), ring()));
    }

    @Test
    void Constructor_WithOneVertex_ThrowsIllegalArgumentException() {
        Set<Vertex> vertices = Set.of(new Vertex(CORNERS.getFirst()));

        assertThrows(IllegalArgumentException.class,
                () -> forestHex(vertices, ring()));
    }

    @Test
    void Constructor_WithFiveVertices_ThrowsIllegalArgumentException() {
        Set<Vertex> vertices = CORNERS.stream()
                .limit(5)
                .map(Vertex::new)
                .collect(toCollection(LinkedHashSet::new));

        assertThrows(IllegalArgumentException.class,
                () -> forestHex(vertices, ring()));
    }

    @Test
    void Constructor_WithSevenVertices_ThrowsIllegalArgumentException() {
        Set<Vertex> vertices = sixVertices();
        vertices.add(new Vertex(new Point(3, 3)));

        assertThrows(IllegalArgumentException.class,
                () -> forestHex(vertices, ring()));
    }

    @Test
    void Constructor_WithFiveEdges_ThrowsIllegalArgumentException() {
        Set<Edge> edges = ring().stream()
                .limit(5)
                .collect(toCollection(LinkedHashSet::new));

        assertThrows(IllegalArgumentException.class,
                () -> forestHex(sixVertices(), edges));
    }

    @Test
    void Constructor_WithSevenEdges_ThrowsIllegalArgumentException() {
        Set<Edge> edges = ring();
        edges.add(edge(0, 2));

        assertThrows(IllegalArgumentException.class,
                () -> forestHex(sixVertices(), edges));
    }

    @Test
    void Constructor_WithEdgeEndpointOutsideVertices_ThrowsIllegalArgumentException() {
        Point outsidePoint = new Point(4, 4);
        Set<Edge> edges = new LinkedHashSet<>(List.of(
                edge(0, 1),
                edge(1, 2),
                edge(2, 3),
                edge(3, 4),
                edge(4, 5),
                new Edge(CORNERS.getLast(), outsidePoint)));

        assertThrows(IllegalArgumentException.class,
                () -> forestHex(sixVertices(), edges));
    }

    @Test
    void Constructor_WithOpenChainAndDuplicateEdge_ThrowsIllegalArgumentException() {
        Set<Edge> edges = new LinkedHashSet<>(List.of(
                edge(0, 1),
                edge(1, 2),
                edge(2, 3),
                edge(3, 4),
                edge(4, 5),
                edge(0, 1)));

        assertThrows(IllegalArgumentException.class,
                () -> forestHex(sixVertices(), edges));
    }

    @Test
    void Constructor_WithTwoSeparateTriangles_ThrowsIllegalArgumentException() {
        Set<Edge> edges = new LinkedHashSet<>(List.of(
                edge(0, 1),
                edge(1, 2),
                edge(2, 0),
                edge(3, 4),
                edge(4, 5),
                edge(5, 3)));

        assertThrows(IllegalArgumentException.class,
                () -> forestHex(sixVertices(), edges));
    }

    @Test
    void Constructor_WithNoEdges_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> forestHex(sixVertices(), Set.of()));
    }

    @Test
    void Constructor_WithOneEdge_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> forestHex(sixVertices(), Set.of(edge(0, 1))));
    }

    @Test
    void CanProduce_OnNewForestHex_ReturnsTrue() {
        Hex hex = forestHex(sixVertices(), ring());

        assertTrue(hex.canProduce());
    }

    @Test
    void CanProduce_OnNewDesertHex_ReturnsFalse() {
        Hex hex = new DesertHex(sixVertices(), ring());

        assertFalse(hex.canProduce());
    }

    @Test
    void CanProduce_OnNewOceanHex_ReturnsFalse() {
        Hex hex = new OceanHex(sixVertices(), ring());

        assertFalse(hex.canProduce());
    }

    @Test
    void GetTokenValue_OnForestHexWithTokenTwo_ReturnsTwo() {
        Hex hex = new ForestHex(2, sixVertices(), ring(), PRODUCTS);

        assertEquals(2, hex.getTokenValue());
    }

    @Test
    void GetTokenValue_OnHillsHexWithTokenTwelve_ReturnsTwelve() {
        Hex hex = new HillsHex(12, sixVertices(), ring(), PRODUCTS);

        assertEquals(12, hex.getTokenValue());
    }

    @Test
    void GetTokenValue_OnOceanHex_ReturnsZero() {
        Hex hex = new OceanHex(sixVertices(), ring());

        assertEquals(0, hex.getTokenValue());
    }

    @Test
    void GetTokenValue_OnDesertHex_ReturnsZero() {
        Hex hex = new DesertHex(sixVertices(), ring());

        assertEquals(0, hex.getTokenValue());
    }

    @Test
    void SetCanProduce_OnForestHexWithFlagTrueAndInputFalse_ReturnsFalse() {
        Hex hex = forestHex(sixVertices(), ring());

        hex.setCanProduce(false);

        assertFalse(hex.canProduce());
    }

    @Test
    void SetCanProduce_OnMountainsHexWithFlagFalseAndInputTrue_ReturnsTrue() {
        Hex hex = new MountainsHex(TOKEN_VALUE, sixVertices(), ring(), PRODUCTS);
        hex.setCanProduce(false);

        hex.setCanProduce(true);

        assertTrue(hex.canProduce());
    }

    @Test
    void SetCanProduce_OnFieldsHexWithFlagTrueAndInputTrue_ReturnsTrue() {
        Hex hex = new FieldsHex(TOKEN_VALUE, sixVertices(), ring(), PRODUCTS);

        hex.setCanProduce(true);

        assertTrue(hex.canProduce());
    }

    @Test
    void SetCanProduce_OnPastureHexWithFlagFalseAndInputFalse_ReturnsFalse() {
        Hex hex = new PastureHex(TOKEN_VALUE, sixVertices(), ring(), PRODUCTS);
        hex.setCanProduce(false);

        hex.setCanProduce(false);

        assertFalse(hex.canProduce());
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
