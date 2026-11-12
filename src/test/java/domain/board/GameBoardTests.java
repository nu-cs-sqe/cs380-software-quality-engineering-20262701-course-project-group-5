package domain.board;

import domain.piece.*;
import mvc_util.Observer;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void RemovePieceFromLocation_OnlyPieceOnVertex_VertexRemovesItAndObserversNotified() {
        assertPieceIsRemovedAndObserversNotified(
                EasyMock.createMock(Vertex.class), new PieceData("settlement"));
    }

    private static void assertPieceIsRemovedAndObserversNotified(Location location, PieceData data) {
        EasyMock.expect(location.holdsPieceWithData(data)).andStubReturn(true);
        location.removePieceWithData(data);
        EasyMock.expectLastCall().once();
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
                EasyMock.createMock(Edge.class), new PieceData("road"));
    }

    private static void assertLocationIsToldToRemove(Location location, PieceData data) {
        EasyMock.expect(location.holdsPieceWithData(data)).andStubReturn(true);
        location.removePieceWithData(data);
        EasyMock.expectLastCall().once();
        EasyMock.replay(location);

        boardOf().removePieceFromLocation(data, location);

        EasyMock.verify(location);
    }

    @Test
    void RemovePieceFromLocation_RobberOnHex_HexRemovesIt() {
        assertLocationIsToldToRemove(
                EasyMock.createMock(Hex.class), new PieceData("robber"));
    }

    @Test
    void RemovePieceFromLocation_OnVertexWithNoPieces_NothingIsRemoved() {
        assertNothingRemoved(
                EasyMock.createMock(Vertex.class), new PieceData("settlement"));
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
                EasyMock.createMock(Vertex.class), new PieceData("road"));
    }
}