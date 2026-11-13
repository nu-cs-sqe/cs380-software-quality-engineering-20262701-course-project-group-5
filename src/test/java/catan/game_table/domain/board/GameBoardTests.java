package catan.game_table.domain.board;

import catan.game_table.domain.board.*;
import catan.game_table.domain.piece.*;
import catan.mvc_util.Observer;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTests {

    // produceResourcesForToken tests

    @Test
    void ProduceResourcesForToken_OnBoardWithoutHexes_DoesNothing() {
        GameBoard board = boardOf();

        assertDoesNotThrow(() -> board.produceResourcesForToken(6));
    }

    @Test
    void ProduceResourcesForToken_OnBoardWithOneMatchingHex_AsksThatHexToProduce() {
        Hex hex = hexExpectedToProduce(8);
        GameBoard board = boardOf(hex);

        board.produceResourcesForToken(8);

        EasyMock.verify(hex);
    }

    @Test
    void ProduceResourcesForToken_WhenNoHexMatches_NoHexProduces() {
        Hex first = hexNotExpectedToProduce(3);
        Hex second = hexNotExpectedToProduce(4);
        Hex third = hexNotExpectedToProduce(9);
        GameBoard board = boardOf(first, second, third);

        board.produceResourcesForToken(6);

        EasyMock.verify(first, second, third);
    }

    @Test
    void ProduceResourcesForToken_WhenOnlyFirstHexMatches_OnlyFirstHexProduces() {
        Hex first = hexExpectedToProduce(2);
        Hex second = hexNotExpectedToProduce(5);
        Hex third = hexNotExpectedToProduce(9);
        GameBoard board = boardOf(first, second, third);

        board.produceResourcesForToken(2);

        EasyMock.verify(first, second, third);
    }

    @Test
    void ProduceResourcesForToken_WhenOnlyLastHexMatches_OnlyLastHexProduces() {
        Hex first = hexNotExpectedToProduce(5);
        Hex second = hexNotExpectedToProduce(9);
        Hex third = hexExpectedToProduce(12);
        GameBoard board = boardOf(first, second, third);

        board.produceResourcesForToken(12);

        EasyMock.verify(first, second, third);
    }

    @Test
    void ProduceResourcesForToken_WhenOnlyMiddleHexMatches_OnlyMiddleHexProduces() {
        Hex first = hexNotExpectedToProduce(3);
        Hex second = hexExpectedToProduce(5);
        Hex third = hexNotExpectedToProduce(9);
        GameBoard board = boardOf(first, second, third);

        board.produceResourcesForToken(5);

        EasyMock.verify(first, second, third);
    }

    @Test
    void ProduceResourcesForToken_WhenTwoHexesShareTheToken_BothProduce() {
        Hex firstEight = hexExpectedToProduce(8);
        Hex other = hexNotExpectedToProduce(3);
        Hex secondEight = hexExpectedToProduce(8);
        GameBoard board = boardOf(firstEight, other, secondEight);

        board.produceResourcesForToken(8);

        EasyMock.verify(firstEight, other, secondEight);
    }

    @Test
    void ProduceResourcesForToken_BelowMinimumTokenValue_ThrowsIllegalArgumentException() {
        Hex first = hexNotExpectedToProduce(3);
        Hex second = hexNotExpectedToProduce(4);
        GameBoard board = boardOf(first, second);

        assertThrows(IllegalArgumentException.class, () -> board.produceResourcesForToken(1));
    }

    @Test
    void ProduceResourcesForToken_AboveMaximumTokenValue_ThrowsIllegalArgumentException() {
        Hex first = hexNotExpectedToProduce(3);
        Hex second = hexNotExpectedToProduce(4);
        GameBoard board = boardOf(first, second);

        assertThrows(IllegalArgumentException.class, () -> board.produceResourcesForToken(13));
    }

    @Test
    void ProduceResourcesForToken_WithRobberValueSeven_NoHexProduces() {
        Hex first = hexNotExpectedToProduce(6);
        Hex second = hexNotExpectedToProduce(8);
        GameBoard board = boardOf(first, second);

        board.produceResourcesForToken(7);

        EasyMock.verify(first, second);
    }

    private static final int NO_TOKEN = 0;
    private static final int ROLLED_TOKEN = 8;
    private static final int OCEAN_HEX_COUNT = 6;
    private static final int[] LAND_HEX_TOKENS = {
            2, 3, 3, 4, 4, 5, 5, 6, 6,
            NO_TOKEN, // desert
            8, 8, 9, 9, 10, 10, 11, 11, 12
    };

    @Test
    void ProduceResourcesForToken_OnFullBoardWithTwoHexesOfTokenEight_ExactlyThoseProduce() {
        List<Hex> hexes = fullBoardHexes();
        GameBoard board = boardOf(hexes.toArray(new Hex[0]));

        board.produceResourcesForToken(ROLLED_TOKEN);

        EasyMock.verify(hexes.toArray());
    }

    private static List<Hex> fullBoardHexes() {
        List<Hex> hexes = new ArrayList<>();
        for (int token : LAND_HEX_TOKENS) {
            if (token == ROLLED_TOKEN) {
                hexes.add(hexExpectedToProduce(token));
            } else {
                hexes.add(hexNotExpectedToProduce(token));
            }
        }
        for (int i = 0; i < OCEAN_HEX_COUNT; i++) {
            hexes.add(hexNotExpectedToProduce(NO_TOKEN));
        }
        return hexes;
    }

    private static GameBoard boardOf(Hex... hexes) {
        return new GameBoard(new LinkedHashSet<>(List.of(hexes)));
    }

    private static Hex hexExpectedToProduce(int tokenValue) {
        Hex hex = EasyMock.createMock(Hex.class);
        EasyMock.expect(hex.getTokenValue()).andStubReturn(tokenValue);
        hex.produceResources();
        EasyMock.expectLastCall();
        EasyMock.replay(hex);
        return hex;
    }

    private static Hex hexNotExpectedToProduce(int tokenValue) {
        Hex hex = EasyMock.createMock(Hex.class);
        EasyMock.expect(hex.getTokenValue()).andStubReturn(tokenValue);
        EasyMock.replay(hex);
        return hex;
    }

    // addPieceAtLocation tests

    @Test
    void AddPieceAtLocation_SettlementOnEmptyVertex_VertexAddsPieceAndObserversNotified() {
        Settlement settlement = EasyMock.createMock(Settlement.class);
        Vertex vertex = EasyMock.createMock(Vertex.class);
        vertex.addPiece(settlement);
        EasyMock.expectLastCall();
        Observer observer = observerExpectingOneUpdate();
        EasyMock.replay(settlement, vertex);
        GameBoard board = boardOf();

        board.addObserver(observer);

        board.addPieceAtLocation(settlement, vertex);

        EasyMock.verify(vertex, observer);
    }

    private static Observer observerExpectingOneUpdate() {
        Observer observer = EasyMock.createMock(Observer.class);
        observer.update();
        EasyMock.expectLastCall();
        EasyMock.replay(observer);
        return observer;
    }

    @Test
    void AddPieceAtLocation_CityOnEmptyVertex_VertexAddsIt() {
        City city = EasyMock.createMock(City.class);
        Vertex vertex = EasyMock.createMock(Vertex.class);
        assertLocationIsToldToAdd(city, vertex);
    }

    @Test
    void AddPieceAtLocation_RoadOnEmptyEdge_EdgeAddsIt() {
        Road road = EasyMock.createMock(Road.class);
        Edge edge = EasyMock.createMock(Edge.class);
        assertLocationIsToldToAdd(road, edge);
    }

    @Test
    void AddPieceAtLocation_HarborOnEmptyEdge_EdgeAddsIt() {
        Harbor harbor = EasyMock.createMock(Harbor.class);
        Edge edge = EasyMock.createMock(Edge.class);
        assertLocationIsToldToAdd(harbor, edge);
    }

    @Test
    void AddPieceAtLocation_RobberOnEmptyHex_HexAddsIt() {
        Robber robber = EasyMock.createMock(Robber.class);
        Hex hex = EasyMock.createMock(Hex.class);
        assertLocationIsToldToAdd(robber, hex);
    }

    @Test
    void AddPieceAtLocation_HarborOnEdgeHoldingRoad_EdgeAddsBoth() {
        Road road = EasyMock.createMock(Road.class);
        Harbor harbor = EasyMock.createMock(Harbor.class);
        Edge edge = EasyMock.createStrictMock(Edge.class); // checks both calls and their order
        edge.addPiece(road);
        edge.addPiece(harbor);
        EasyMock.replay(road, harbor, edge);
        GameBoard board = boardOf();

        board.addPieceAtLocation(road, edge);
        board.addPieceAtLocation(harbor, edge);

        EasyMock.verify(edge);
    }

    private static void assertLocationIsToldToAdd(Piece piece, Location location) {
        location.addPiece(piece);
        EasyMock.expectLastCall();
        EasyMock.replay(piece, location);

        boardOf().addPieceAtLocation(piece, location);

        EasyMock.verify(location);
    }

    // removePieceFromLocation tests

    private static final PieceData SETTLEMENT_DATA = new PieceData("settlement");
    private static final PieceData ROAD_DATA = new PieceData("road");
    private static final PieceData HARBOR_DATA = new PieceData("harbor");
    private static final PieceData ROBBER_DATA = new PieceData("robber");



    @Test
    void RemovePieceFromLocation_OnlyPieceOnVertex_VertexRemovesItAndObserversNotified() {
        assertPieceIsRemovedAndObserversNotified(
                EasyMock.createMock(Vertex.class), SETTLEMENT_DATA);
    }

    private static void assertPieceIsRemovedAndObserversNotified(Location location, PieceData data) {
        EasyMock.expect(location.holdsPieceWithData(data)).andStubReturn(true);
        location.removePieceWithData(data);
        EasyMock.expectLastCall();
        EasyMock.replay(location);
        Observer observer = observerExpectingOneUpdate();
        GameBoard board = boardOf();
        board.addObserver(observer);

        board.removePieceFromLocation(data, location);

        EasyMock.verify(location, observer);
    }

    @Test
    void RemovePieceFromLocation_OnlyRoadOnEdge_EdgeRemovesIt() {
        assertLocationIsToldToRemove(
                EasyMock.createMock(Edge.class), ROAD_DATA);
    }

    private static void assertLocationIsToldToRemove(Location location, PieceData data) {
        EasyMock.expect(location.holdsPieceWithData(data)).andStubReturn(true);
        location.removePieceWithData(data);
        EasyMock.expectLastCall();
        EasyMock.replay(location);

        boardOf().removePieceFromLocation(data, location);

        EasyMock.verify(location);
    }

    @Test
    void RemovePieceFromLocation_RobberOnHex_HexRemovesIt() {
        assertLocationIsToldToRemove(
                EasyMock.createMock(Hex.class), ROBBER_DATA);
    }

    @Test
    void RemovePieceFromLocation_OnVertexWithNoPieces_NothingIsRemoved() {
        assertNothingRemoved(
                EasyMock.createMock(Vertex.class), SETTLEMENT_DATA);
    }

    private static void assertNothingRemoved(Location location, PieceData data) {
        EasyMock.expect(location.holdsPieceWithData(data)).andStubReturn(false);
        EasyMock.replay(location);

        boardOf().removePieceFromLocation(data, location);

        EasyMock.verify(location);
    }

    @Test
    void RemovePieceFromLocation_WhenPieceDataMatchesNone_NothingIsRemoved() {
        assertNothingRemoved(
                EasyMock.createMock(Vertex.class), ROAD_DATA);
    }

    @Test
    void RemovePieceFromLocation_RoadThenHarborOnEdge_RemovingRoadRemovesOnlyRoad() {
        Edge edge = EasyMock.createMock(Edge.class);
        EasyMock.expect(edge.holdsPieceWithData(ROAD_DATA)).andStubReturn(true);
        EasyMock.expect(edge.holdsPieceWithData(HARBOR_DATA)).andStubReturn(true);
        edge.removePieceWithData(ROAD_DATA);
        EasyMock.expectLastCall();
        EasyMock.replay(edge);

        boardOf().removePieceFromLocation(ROAD_DATA, edge);

        EasyMock.verify(edge);
    }

    @Test
    void RemovePieceFromLocation_RoadThenHarborOnEdge_RemovingHarborRemovesOnlyHarbor() {
        Edge edge = EasyMock.createMock(Edge.class);
        EasyMock.expect(edge.holdsPieceWithData(ROAD_DATA)).andStubReturn(true);
        EasyMock.expect(edge.holdsPieceWithData(HARBOR_DATA)).andStubReturn(true);
        edge.removePieceWithData(HARBOR_DATA);
        EasyMock.expectLastCall();
        EasyMock.replay(edge);

        boardOf().removePieceFromLocation(HARBOR_DATA, edge);

        EasyMock.verify(edge);
    }

    private static final String SETTLEMENT_TYPE = "settlement";

    @Test
    void RemovePieceFromLocation_WithEqualButDistinctPieceData_VertexRemovesIt() {
        Vertex vertex = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertex.holdsPieceWithData(SETTLEMENT_DATA)).andStubReturn(true);
        vertex.removePieceWithData(SETTLEMENT_DATA);
        EasyMock.expectLastCall();
        EasyMock.replay(vertex);

        boardOf().removePieceFromLocation(new PieceData(SETTLEMENT_TYPE), vertex);

        EasyMock.verify(vertex);
    }

    // createNeighbors() tests

    @Test
    void CreateNeighbors_WithNoVerticesOrEdges_DoesNotThrow() {
        assertDoesNotThrow(() -> boardWith(Set.of(), Set.of()));
    }

    private static GameBoard boardWith(Set<Vertex> vertices, Set<Edge> edges) {
        return new GameBoard(Set.of(), vertices, edges);
    }

    private static final Point A = new Point(0, 0);
    private static final Point B = new Point(1, 0);
    private static final Point C = new Point(2, 0);
    private static final Point D = new Point(3, 0);
    private static final Point E = new Point(4, 0);
    private static final Point F = new Point(5, 0);
    private static final Point G = new Point(6, 0);
    private static final Point H = new Point(7, 0);
    private static final Point I = new Point(8, 0);
    private static final Point J = new Point(9, 0);

    @Test
    void CreateNeighbors_OnSingleHexRing_EachVertexHasItsTwoNeighbors() {
        Vertex a = vertexAt(A); Vertex b = vertexAt(B); Vertex c = vertexAt(C);
        Vertex d = vertexAt(D); Vertex e = vertexAt(E); Vertex f = vertexAt(F);
        GameBoard board = boardWith(
                orderedSetOf(a, b, c, d, e, f),
                orderedSetOf(edgeBetween(A, B), edgeBetween(B, C), edgeBetween(C, D),
                        edgeBetween(D, E), edgeBetween(E, F), edgeBetween(F, A)));

        assertEquals(Set.of(b, f), board.getNeighbors(a));
        assertEquals(Set.of(e, a), board.getNeighbors(f));
    }

    private static Vertex vertexAt(Point point) {
        Vertex vertex = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertex.getPoint()).andStubReturn(point);
        EasyMock.replay(vertex);
        return vertex;
    }

    private static Edge edgeBetween(Point first, Point second) {
        return edgeJoining(Set.of(first, second));
    }

    private static Edge edgeJoining(Set<Point> endpoints) {
        Edge edge = EasyMock.createMock(Edge.class);
        EasyMock.expect(edge.getEndpoints()).andStubReturn(endpoints);
        EasyMock.replay(edge);
        return edge;
    }

    @SafeVarargs
    private static <T> Set<T> orderedSetOf(T... items) {
        return new LinkedHashSet<>(List.of(items));
    }

    @Test
    void CreateNeighbors_OnTwoHexesSharingAnEdge_SharedVerticesMergeNeighbors() {
        Vertex a = vertexAt(A); Vertex b = vertexAt(B); Vertex c = vertexAt(C);
        Vertex d = vertexAt(D); Vertex e = vertexAt(E); Vertex f = vertexAt(F);
        Vertex g = vertexAt(G); Vertex h = vertexAt(H); Vertex i = vertexAt(I);
        Vertex j = vertexAt(J);
        GameBoard board = boardWith(
                orderedSetOf(a, b, c, d, e, f, g, h, i, j),
                orderedSetOf(edgeBetween(A, B), edgeBetween(B, C), edgeBetween(C, D),
                        edgeBetween(D, E), edgeBetween(E, F), edgeBetween(F, A),
                        edgeBetween(D, G), edgeBetween(G, H), edgeBetween(H, I),
                        edgeBetween(I, J), edgeBetween(J, C)));

        assertEquals(Set.of(b, d, j), board.getNeighbors(c));
        assertEquals(Set.of(c, e, g), board.getNeighbors(d));
    }

    private static final int[][] CORNER_OFFSETS = {
            {0, -2}, {1, -1}, {1, 1},
            {0, 2}, {-1, 1}, {-1, -1}
    };

    @Test
    void CreateNeighbors_OnStandardLandBoard_ThirtySixVerticesHaveThreeNeighborsAndEighteenTwo() {
        java.util.Map<Point, Vertex> vertices = new java.util.LinkedHashMap<>();
        Set<Edge> edges = new LinkedHashSet<>();

        for (Set<Point> endpoints : landEndpointPairs()) {
            for (Point point : endpoints) {
                vertices.computeIfAbsent(point, GameBoardTests::vertexAt);
            }
            edges.add(edgeJoining(endpoints));
        }

        assertEquals(54, vertices.size());
        assertEquals(72, edges.size());

        GameBoard board = boardWith(new LinkedHashSet<>(vertices.values()), edges);

        assertEquals(36, cornersWithNeighborCount(board, vertices.values(), 3));
        assertEquals(18, cornersWithNeighborCount(board, vertices.values(), 2));
    }

    private static long cornersWithNeighborCount(
            GameBoard board, java.util.Collection<Vertex> vertices, int count) {
        return vertices.stream()
                .filter(vertex -> board.getNeighbors(vertex).size() == count)
                .count();
    }

    private static Set<Set<Point>> landEndpointPairs() {
        int[][] hexCenters = {
                {-2, -6}, {0, -6}, {2, -6},
                {-3, -3}, {-1, -3}, {1, -3}, {3, -3},
                {-4,  0}, {-2,  0}, {0,  0}, {2,  0}, {4,  0},
                {-3,  3}, {-1,  3}, {1,  3}, {3,  3},
                {-2,  6}, {0,  6}, {2,  6}
        };

        Set<Set<Point>> pairs = new LinkedHashSet<>();
        for (int[] center : hexCenters) {
            addRingPairs(pairs, cornersOfHexAt(center[0], center[1]));
        }
        return pairs;
    }

    private static List<Point> cornersOfHexAt(int centerX, int centerY) {
        List<Point> corners = new ArrayList<>();
        for (int[] offset : CORNER_OFFSETS) {
            corners.add(new Point(centerX + offset[0], centerY + offset[1]));
        }
        return corners;
    }

    private static void addRingPairs(Set<Set<Point>> pairs, List<Point> corners) {
        for (int i = 0; i < corners.size(); i++) {
            Point first = corners.get(i);
            Point second = corners.get((i + 1) % corners.size());
            pairs.add(Set.of(first, second));
        }
    }

}
