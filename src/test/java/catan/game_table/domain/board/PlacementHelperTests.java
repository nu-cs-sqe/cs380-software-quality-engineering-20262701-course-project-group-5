package catan.game_table.domain.board;

import catan.game_table.domain.Color;
import catan.game_table.domain.GamePhase;
import catan.game_table.domain.GamePhaseHolder;
import catan.game_table.domain.exception.InvalidPlacementException;
import catan.game_table.domain.piece.*;
import org.easymock.EasyMock;
import org.easymock.IMockBuilder;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PlacementHelperTests {

    @Test
    public void GetAndCacheValidSpots_GivenRedSettlement_ExpectCorrectMethodCalledOnce_AndSettlementSpotsReturned() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece inputPiece = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        Set<String> expectedVertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidSettlementSpots(playerColor)).andReturn(expectedVertices).times(1);
        placementHelper.notifyObserversInternal();
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(placementHelper);

        Set<? extends Location> actualLocations = placementHelper.getAndCacheValidSpots(inputPiece);

        EasyMock.verify(placementHelper);
        assertEquals(expectedVertices, actualLocations);

    }

    @Test
    public void GetAndCacheValidSpots_GivenBlueRoad_ExpectCorrectMethodCalledOnce_AndRoadSpotsReturned() {

        Color playerColor = Color.BLUE;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece existingPiece = makePieceMock(PieceType.SETTLEMENT, playerColor);
        Piece inputPiece = makePieceMock(PieceType.ROAD, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of("A", List.of(existingPiece));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        Set<Set<String>> expectedEdgeNames = Set.of(
                Set.of("A", "B"),
                Set.of("F", "A")
        );
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidRoadSpots(playerColor)).andReturn(expectedEdges).times(1);
        placementHelper.notifyObserversInternal();
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(placementHelper);

        Set<? extends Location> actualLocations = placementHelper.getAndCacheValidSpots(inputPiece);

        EasyMock.verify(placementHelper);
        assertEquals(expectedEdges, actualLocations);

    }

    @Test
    public void GetAndCacheValidSpots_GivenWhiteCity_ExpectCorrectMethodCalledOnce_AndCitySpotsReturned() {

        Color playerColor = Color.WHITE;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece inputPiece = makePieceMock(PieceType.CITY, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidCitySpots(playerColor)).andReturn(expectedVertices).times(1);
        placementHelper.notifyObserversInternal();
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(placementHelper);

        Set<? extends Location> actualLocations = placementHelper.getAndCacheValidSpots(inputPiece);

        EasyMock.verify(placementHelper);
        assertEquals(expectedVertices, actualLocations);

    }

    @Test
    public void GetAndCacheValidSpots_GivenRobber_ExpectCorrectMethodCalledOnce_AndRobberSpotsReturned() {

        GamePhase gamePhase = GamePhase.REGULAR;

        Piece inputPiece = makePieceMock(PieceType.ROBBER, Color.NONE);

        Hex hex1 = EasyMock.createMock(Hex.class);
        EasyMock.expect(hex1.getPieces()).andStubReturn(List.of());
        Hex hex2 = EasyMock.createMock(Hex.class);
        EasyMock.expect(hex2.getPieces()).andStubReturn(List.of());
        EasyMock.replay(hex1, hex2);

        GameBoard gameBoard = makeGameBoardMock(Map.of(), Map.of(), false);
        EasyMock.expect(gameBoard.getLandHexes()).andStubReturn(Set.of(hex1, hex2));

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        Set<Hex> expectedHexes = Set.of(hex1, hex2);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidRobberSpots()).andReturn(expectedHexes).times(1);
        placementHelper.notifyObserversInternal();
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(placementHelper);

        Set<? extends Location> actualLocations = placementHelper.getAndCacheValidSpots(inputPiece);

        EasyMock.verify(placementHelper);
        assertEquals(expectedHexes, actualLocations);

    }

    @Test
    public void GetAndCacheValidSpots_GivenHarbor_ExpectIllegalArgumentException() {

        GamePhase gamePhase = GamePhase.REGULAR;

        Piece inputPiece = makePieceMock(PieceType.HARBOR, Color.NONE);

        GameBoard gameBoard = makeGameBoardMock(Map.of(), Map.of(), false);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        String expectedMessage = "No defined behavior exists for this piece type";

        Exception actualException = assertThrows(IllegalArgumentException.class, () -> placementHelper.getAndCacheValidSpots(inputPiece));
        assertEquals(expectedMessage, actualException.getMessage());

    }

    @Test
    public void GetAndCacheValidSpots_GivenRedSettlementTwice_ExpectCorrectMethodCalledOnce_AndSettlementSpotsReturned() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece inputPiece = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        Set<String> expectedVertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidSettlementSpots(playerColor)).andReturn(expectedVertices).times(1);
        placementHelper.notifyObserversInternal();
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(placementHelper);

        Set<? extends Location> actualLocations1 = placementHelper.getAndCacheValidSpots(inputPiece);
        Set<? extends Location> actualLocations2 = placementHelper.getAndCacheValidSpots(inputPiece);

        EasyMock.verify(placementHelper);
        assertEquals(expectedVertices, actualLocations1);
        assertEquals(expectedVertices, actualLocations2);

    }

    @Test
    public void GetAndCacheValidSpots_GivenRedSettlementThenBlueSettlement_ExpectCorrectMethodCalledTwice_AndSettlementSpotsReturned() {

        GamePhase gamePhase = GamePhase.SETUP;

        Piece inputPiece1 = makePieceMock(PieceType.SETTLEMENT, Color.RED);
        Piece inputPiece2 = makePieceMock(PieceType.SETTLEMENT, Color.BLUE);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        Set<String> expectedVertexNames1 = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices1 = filterVerticesWithNames(expectedVertexNames1, vertexMap);
        Set<String> expectedVertexNames2 = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices2 = filterVerticesWithNames(expectedVertexNames2, vertexMap);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidSettlementSpots(Color.RED)).andReturn(expectedVertices1).times(1);
        EasyMock.expect(placementHelper.getValidSettlementSpots(Color.BLUE)).andReturn(expectedVertices2).times(1);
        placementHelper.notifyObserversInternal();
        EasyMock.expectLastCall().times(2);
        EasyMock.replay(placementHelper);

        Set<? extends Location> actualLocations1 = placementHelper.getAndCacheValidSpots(inputPiece1);
        Set<? extends Location> actualLocations2 = placementHelper.getAndCacheValidSpots(inputPiece2);

        EasyMock.verify(placementHelper);
        assertEquals(expectedVertices1, actualLocations1);
        assertEquals(expectedVertices2, actualLocations2);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndSetupPhase_AndEmptyBoard_ExpectAllLandVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndRegularPhase_AndEmptyBoard_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndSetupPhase_AndRedSettlementPlaced_ExpectVerticesTwoAwayFromSettlement() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of("A", List.of(settlement));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("C", "D", "E");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndRegularPhase_AndRedRoadPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(PieceType.ROAD, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(Set.of("C", "D"), List.of(road));
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("C", "D");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndRegularPhase_AndRedRoadPlaced_AndBlueSettlementOnRoad_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece otherSettlement = makePieceMock(PieceType.SETTLEMENT, Color.BLUE);
        Piece ownRoad = makePieceMock(PieceType.ROAD, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of("C", List.of(otherSettlement));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(Set.of("C", "D"), List.of(ownRoad));
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndRegularPhase_AndTwoSeparateRedRoadsPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road1 = makePieceMock(PieceType.ROAD, playerColor);
        Piece road2 = makePieceMock(PieceType.ROAD, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(
                Set.of("C", "D"), List.of(road1),
                Set.of("A", "F"), List.of(road2)
        );
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("A", "C", "D", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndRegularPhase_AndTwoConnectedRedRoadsPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road1 = makePieceMock(PieceType.ROAD, playerColor);
        Piece road2 = makePieceMock(PieceType.ROAD, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(
                Set.of("C", "D"), List.of(road1),
                Set.of("D", "E"), List.of(road2)
        );
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("C", "D", "E");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndSetupPhase_AndNoOpenBuildingSpotsDueToDistanceRule_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement1 = makePieceMock(PieceType.SETTLEMENT, playerColor);
        Piece settlement2 = makePieceMock(PieceType.SETTLEMENT, Color.BLUE);
        Piece settlement3 = makePieceMock(PieceType.SETTLEMENT, Color.WHITE);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of(
                "A", List.of(settlement1),
                "C", List.of(settlement2),
                "E", List.of(settlement3)
        );
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndRegularPhase_AndBlueRoadPlaced_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(PieceType.ROAD, Color.BLUE);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(
                Set.of("E", "F"), List.of(road)
        );
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndRegularPhase_AndRedSettlementPlusTwoLinearRoadsPlaced_ExpectVertexAtEndOfRoads() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(PieceType.SETTLEMENT, playerColor);
        Piece road1 = makePieceMock(PieceType.ROAD, playerColor);
        Piece road2 = makePieceMock(PieceType.ROAD, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of("A", List.of(settlement));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(
                Set.of("A", "B"), List.of(road1),
                Set.of("B", "C"), List.of(road2)
        );
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("C");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndSetupPhase_AndRedRoadPlaced_ExpectAllLandVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece road = makePieceMock(PieceType.ROAD, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(
                Set.of("B", "C"), List.of(road)
        );
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenRed_AndSetupPhase_AndTwoConnectedRedRoadsPlaced_ExpectAllLandVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece road1 = makePieceMock(PieceType.ROAD, playerColor);
        Piece road2 = makePieceMock(PieceType.ROAD, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(
                Set.of("C", "D"), List.of(road1),
                Set.of("D", "E"), List.of(road2)
        );
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidSettlementSpots_GivenBlue_AndRegularPhase_AndBlueRoadPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.BLUE;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(PieceType.ROAD, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(
                Set.of("F", "A"), List.of(road)
        );
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidSettlementSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("A", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidCitySpots_GivenRed_AndSetupPhase_AndEmptyBoard_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidCitySpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidCitySpots_GivenRed_AndRegularPhase_AndEmptyBoard_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidCitySpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidCitySpots_GivenRed_AndSetupPhase_AndRedSettlementPlaced_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of("A", List.of(settlement));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidCitySpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidCitySpots_GivenRed_AndRegularPhase_AndRedSettlementPlaced_ExpectSettlementVertex() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of("A", List.of(settlement));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidCitySpots(playerColor);
        Set<String> expectedVertexNames = Set.of("A");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidCitySpots_GivenRed_AndRegularPhase_AndTwoRedSettlementPlaced_ExpectSettlementVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement1 = makePieceMock(PieceType.SETTLEMENT, playerColor);
        Piece settlement2 = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of(
                "B", List.of(settlement1),
                "D", List.of(settlement2)
        );
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidCitySpots(playerColor);
        Set<String> expectedVertexNames = Set.of("B", "D");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidCitySpots_GivenRed_AndRegularPhase_AndRedSettlementPlusBlueSettlementPlaced_ExpectRedSettlementVertex() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement1 = makePieceMock(PieceType.SETTLEMENT, playerColor);
        Piece settlement2 = makePieceMock(PieceType.SETTLEMENT, Color.BLUE);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of(
                "B", List.of(settlement1),
                "D", List.of(settlement2)
        );
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidCitySpots(playerColor);
        Set<String> expectedVertexNames = Set.of("B");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidCitySpots_GivenRed_AndRegularPhase_AndRedCityPlusBlueCityPlaced_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement1 = makePieceMock(PieceType.CITY, playerColor);
        Piece settlement2 = makePieceMock(PieceType.CITY, Color.BLUE);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Map<String, List<Piece>> vertexPieces = Map.of(
                "B", List.of(settlement1),
                "D", List.of(settlement2)
        );
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("F", "A")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidCitySpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndSetupPhase_AndRedSettlementPlaced_ExpectSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of("F", List.of(settlement));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Set<String>> expectedEdgeNames = Set.of(
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndSetupPhase_AndSecondRedSettlementJustPlaced_ExpectSecondSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement1 = makePieceMock(PieceType.SETTLEMENT, playerColor);
        Piece road1 = makePieceMock(PieceType.ROAD, playerColor);
        Piece settlement2 = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of(
                "C", List.of(settlement1),
                "F", List.of(settlement2)
        );
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(Set.of("B", "C"), List.of(road1));
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Set<String>> expectedEdgeNames = Set.of(
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndSetupPhase_AndEmptyBoard_ExpectNoEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Set<String>> expectedEdgeNames = Set.of();
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndEmptyBoard_ExpectNoEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Set<String>> expectedEdgeNames = Set.of();
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedSettlementPlaced_ExpectSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of("F", List.of(settlement));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Set<String>> expectedEdgeNames = Set.of(
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedRoadPlaced_ExpectRoadAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(PieceType.ROAD, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(Set.of("B", "C"), List.of(road));
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Set<String>> expectedEdgeNames = Set.of(
                Set.of("A", "B"),
                Set.of("C", "D")
        );
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndAllAdjacentEdgesOccupied_ExpectNoEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece ownSettlement = makePieceMock(PieceType.ROAD, playerColor);
        Piece ownRoad = makePieceMock(PieceType.ROAD, playerColor);
        Piece otherRoad1 = makePieceMock(PieceType.ROAD, Color.BLUE);
        Piece otherRoad2 = makePieceMock(PieceType.ROAD, Color.BLUE);
        Piece otherRoad3 = makePieceMock(PieceType.ROAD, Color.BLUE);
        Piece otherRoad4 = makePieceMock(PieceType.ROAD, Color.BLUE);
        Piece otherRoad5 = makePieceMock(PieceType.ROAD, Color.BLUE);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of(
                "F", List.of(ownSettlement)
        );
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(
                Set.of("A", "B"), List.of(otherRoad1),
                Set.of("B", "C"), List.of(otherRoad2),
                Set.of("D", "E"), List.of(otherRoad3),
                Set.of("E", "F"), List.of(ownRoad),
                Set.of("A", "F"), List.of(otherRoad4),
                Set.of("F", "G"), List.of(otherRoad5)
        );
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Set<String>> expectedEdgeNames = Set.of();
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedSettlementPlusBlueSettlementPlaced_ExpectRedSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece ownSettlement = makePieceMock(PieceType.SETTLEMENT, playerColor);
        Piece otherSettlement = makePieceMock(PieceType.SETTLEMENT, Color.BLUE);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of(
                "C", List.of(ownSettlement),
                "F", List.of(otherSettlement)
        );
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Set<String>> expectedEdgeNames = Set.of(
                Set.of("B", "C"),
                Set.of("C", "D")
        );
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedSettlementPlusTwoConnectingBlueRoadsPlaced_ExpectRemainingEdge() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece ownSettlement = makePieceMock(PieceType.SETTLEMENT, playerColor);
        Piece otherRoad1 = makePieceMock(PieceType.ROAD, Color.BLUE);
        Piece otherRoad2 = makePieceMock(PieceType.ROAD, Color.BLUE);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of("F", List.of(ownSettlement));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(
                Set.of("A", "F"), List.of(otherRoad1),
                Set.of("F", "G"), List.of(otherRoad2)
        );
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Set<String>> expectedEdgeNames = Set.of(Set.of("E", "F"));
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenBlue_AndRegularPhase_AndBlueSettlementPlaced_ExpectSettlementAdjacentEdges() {

        Color playerColor = Color.BLUE;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of("F", List.of(settlement));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Set<String>> expectedEdgeNames = Set.of(
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedRoadPlusAdjacentBlueSettlementPlaced_ExpectUnblockedEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece ownRoad = makePieceMock(PieceType.ROAD, playerColor);
        Piece otherSettlement = makePieceMock(PieceType.SETTLEMENT, Color.BLUE);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of("F", List.of(otherSettlement));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F"),
                Set.of("F", "G")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(Set.of("E", "F"), List.of(ownRoad));
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);
        EasyMock.replay(gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Set<String>> expectedEdgeNames = Set.of(Set.of("D", "E"));
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRobberSpots_GivenEmptyBoard_ExpectAllLandHexes() {

        Hex hex1 = EasyMock.createMock(Hex.class);
        EasyMock.expect(hex1.getPieces()).andStubReturn(List.of());
        Hex hex2 = EasyMock.createMock(Hex.class);
        EasyMock.expect(hex2.getPieces()).andStubReturn(List.of());
        EasyMock.replay(hex1, hex2);

        GameBoard gameBoard = makeGameBoardMock(Map.of(), Map.of(), false);
        EasyMock.expect(gameBoard.getLandHexes()).andStubReturn(Set.of(hex1, hex2));

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Hex> actualHexes = placementHelper.getValidRobberSpots();
        Set<Hex> expectedHexes = Set.of(hex1, hex2);

        assertEquals(expectedHexes, actualHexes);

    }

    @Test
    public void GetValidRobberSpots_GivenRobberPlacedOnHex_ExpectOtherLandHexes() {

        Piece robber = makePieceMock(PieceType.ROBBER, Color.NONE);

        Hex hex1 = EasyMock.createMock(Hex.class);
        EasyMock.expect(hex1.getPieces()).andStubReturn(List.of(robber));
        Hex hex2 = EasyMock.createMock(Hex.class);
        EasyMock.expect(hex2.getPieces()).andStubReturn(List.of());
        EasyMock.replay(hex1, hex2);

        GameBoard gameBoard = makeGameBoardMock(Map.of(), Map.of(), false);
        EasyMock.expect(gameBoard.getLandHexes()).andStubReturn(Set.of(hex1, hex2));

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Hex> actualHexes = placementHelper.getValidRobberSpots();
        Set<Hex> expectedHexes = Set.of(hex2);

        assertEquals(expectedHexes, actualHexes);

    }

    @Test
    public void Place_GivenSettlement_AndValidLocation_ExpectSettlementPlacedAtLocation() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;
        Piece pieceToPlace = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        Location destinationLocation = vertexMap.get("A");

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap, false);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        Set<String> expectedVertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidSettlementSpots(playerColor)).andReturn(expectedVertices).times(1);
        placementHelper.notifyObserversInternal();
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(placementHelper);

        gameBoard.addPieceAtLocation(pieceToPlace, destinationLocation);
        EasyMock.expectLastCall();

        EasyMock.replay(gameBoard, gamePhaseHolder);

        assertDoesNotThrow(() -> placementHelper.place(pieceToPlace, destinationLocation));
        EasyMock.verify(placementHelper, gameBoard);

    }

    @Test
    public void Place_GivenSettlement_AndInvalidLocation_ExpectInvalidPlacementException() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;
        Piece pieceToPlace = makePieceMock(PieceType.SETTLEMENT, playerColor);
        Piece existingPiece = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of("A", List.of(existingPiece));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        Location destinationLocation = vertexMap.get("B");

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap, false);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        Set<String> expectedVertexNames = Set.of("C", "D", "E");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidSettlementSpots(playerColor)).andReturn(expectedVertices).times(1);
        EasyMock.replay(placementHelper);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        String expectedMessage = "Cannot place settlement at vertex";

        Exception exception = assertThrows(InvalidPlacementException.class, () -> placementHelper.place(pieceToPlace, destinationLocation));
        assertEquals(expectedMessage, exception.getMessage());
        EasyMock.verify(placementHelper, gameBoard);

    }

    @Test
    public void Place_GivenCity_AndValidLocation_ExpectCityPlacedAtLocation_AndSettlementRemovedFromLocation() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;
        Piece pieceToPlace = makePieceMock(PieceType.CITY, playerColor);
        Piece existingPiece = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of("A", List.of(existingPiece));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        Location destinationLocation = vertexMap.get("A");

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap, false);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        Set<String> expectedVertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidCitySpots(playerColor)).andReturn(expectedVertices).times(1);
        placementHelper.notifyObserversInternal();
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(placementHelper);

        gameBoard.addPieceAtLocation(pieceToPlace, destinationLocation);
        EasyMock.expectLastCall();

        gameBoard.removePieceFromLocation(existingPiece.getPieceData(), destinationLocation);
        EasyMock.expectLastCall();

        EasyMock.replay(gameBoard, gamePhaseHolder);

        assertDoesNotThrow(() -> placementHelper.place(pieceToPlace, destinationLocation));
        EasyMock.verify(placementHelper, gameBoard);

    }

    @Test
    public void Place_GivenCity_AndInvalidLocation_ExpectInvalidPlacementException() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;
        Piece pieceToPlace = makePieceMock(PieceType.CITY, playerColor);
        Piece existingPiece = makePieceMock(PieceType.SETTLEMENT, Color.BLUE);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of("A", List.of(existingPiece));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        Location destinationLocation = vertexMap.get("A");

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap, false);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidCitySpots(playerColor)).andReturn(expectedVertices).times(1);
        EasyMock.replay(placementHelper);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        String expectedMessage = "Cannot place city at vertex";

        Exception exception = assertThrows(InvalidPlacementException.class, () -> placementHelper.place(pieceToPlace, destinationLocation));
        assertEquals(expectedMessage, exception.getMessage());
        EasyMock.verify(placementHelper, gameBoard);

    }

    @Test
    public void Place_GivenRoad_AndValidLocation_ExpectRoadPlacedAtLocation() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;
        Piece pieceToPlace = makePieceMock(PieceType.ROAD, playerColor);
        Piece existingPiece = makePieceMock(PieceType.SETTLEMENT, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of("A", List.of(existingPiece));
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of();
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        Location destinationLocation = edgeMap.get(Set.of("A", "B"));

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap, false);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        Set<Set<String>> expectedEdgeNames = Set.of(
                Set.of("A", "B"),
                Set.of("A", "F")
        );
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidRoadSpots(playerColor)).andReturn(expectedEdges).times(1);
        placementHelper.notifyObserversInternal();
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(placementHelper);

        gameBoard.addPieceAtLocation(pieceToPlace, destinationLocation);
        EasyMock.expectLastCall();

        EasyMock.replay(gameBoard, gamePhaseHolder);

        assertDoesNotThrow(() -> placementHelper.place(pieceToPlace, destinationLocation));
        EasyMock.verify(placementHelper, gameBoard);

    }

    @Test
    public void Place_GivenRoad_AndInvalidLocation_ExpectInvalidPlacementException() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;
        Piece pieceToPlace = makePieceMock(PieceType.ROAD, playerColor);
        Piece existingPiece = makePieceMock(PieceType.ROAD, playerColor);

        Set<String> vertexNames = Set.of("A", "B", "C", "D", "E", "F", "G");
        Map<String, List<Piece>> vertexPieces = Map.of();
        Map<String, Vertex> vertexMap = makeVertexMocks(vertexNames, vertexPieces);

        Set<Set<String>> edgeNamePairs = Set.of(
                Set.of("A", "B"),
                Set.of("B", "C"),
                Set.of("C", "D"),
                Set.of("D", "E"),
                Set.of("E", "F"),
                Set.of("A", "F")
        );
        Map<Set<String>, List<Piece>> edgePieces = Map.of(Set.of("A", "B"), List.of(existingPiece));
        Map<Set<String>, Edge> edgeMap = makeEdgeMocks(edgeNamePairs, edgePieces);

        Location destinationLocation = edgeMap.get(Set.of("A", "B"));

        GameBoard gameBoard = makeGameBoardMock(vertexMap, edgeMap, false);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        Set<Set<String>> expectedEdgeNames = Set.of(
                Set.of("B", "C"),
                Set.of("A", "F")
        );
        Set<Edge> expectedEdges = filterEdgesWithNames(expectedEdgeNames, edgeMap);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidRoadSpots(playerColor)).andReturn(expectedEdges).times(1);
        EasyMock.replay(placementHelper);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        String expectedMessage = "Cannot place road at edge";

        Exception exception = assertThrows(InvalidPlacementException.class, () -> placementHelper.place(pieceToPlace, destinationLocation));
        assertEquals(expectedMessage, exception.getMessage());
        EasyMock.verify(placementHelper, gameBoard);

    }

    @Test
    public void Place_GivenHarbor_ExpectHarborPlacedAtLocation() {

        GamePhase gamePhase = GamePhase.REGULAR;
        Piece pieceToPlace = makePieceMock(PieceType.HARBOR, Color.NONE);

        Location destinationLocation = EasyMock.createMock(Edge.class);
        EasyMock.expect(destinationLocation.getLocationType()).andStubReturn(LocationType.EDGE);
        EasyMock.expect(destinationLocation.getPieces()).andStubReturn(List.of());
        EasyMock.replay(destinationLocation);

        GameBoard gameBoard = makeGameBoardMock(new HashMap<>(), new HashMap<>(), false);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        placementHelper.notifyObserversInternal();
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(placementHelper);

        gameBoard.addPieceAtLocation(pieceToPlace, destinationLocation);
        EasyMock.expectLastCall();

        EasyMock.replay(gameBoard, gamePhaseHolder);

        assertDoesNotThrow(() -> placementHelper.place(pieceToPlace, destinationLocation));
        EasyMock.verify(placementHelper, gameBoard);

    }

    @Test
    public void Place_GivenRobber_AndValidLocation_ExpectRobberPlacedAtLocation() {

        GamePhase gamePhase = GamePhase.REGULAR;
        Piece pieceToPlace = makePieceMock(PieceType.ROBBER, Color.NONE);

        Location destinationLocation = EasyMock.createMock(Hex.class);
        EasyMock.expect(destinationLocation.getLocationType()).andStubReturn(LocationType.HEX);
        EasyMock.expect(destinationLocation.getPieces()).andStubReturn(List.of());
        EasyMock.replay(destinationLocation);

        Set<Hex> expectedHexes = Set.of((Hex) destinationLocation);

        GameBoard gameBoard = makeGameBoardMock(new HashMap<>(), new HashMap<>(), false);
        EasyMock.expect(gameBoard.getLandHexes()).andStubReturn(expectedHexes);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidRobberSpots()).andReturn(expectedHexes).times(1);
        placementHelper.notifyObserversInternal();
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(placementHelper);

        gameBoard.addPieceAtLocation(pieceToPlace, destinationLocation);
        EasyMock.expectLastCall();

        EasyMock.replay(gameBoard, gamePhaseHolder);

        assertDoesNotThrow(() -> placementHelper.place(pieceToPlace, destinationLocation));
        EasyMock.verify(placementHelper, gameBoard);

    }

    @Test
    public void Place_GivenRobber_AndInvalidLocation_ExpectInvalidPlacementException() {

        GamePhase gamePhase = GamePhase.REGULAR;
        Piece pieceToPlace = makePieceMock(PieceType.ROBBER, Color.NONE);

        Location destinationLocation = EasyMock.createMock(Vertex.class);
        EasyMock.expect(destinationLocation.getLocationType()).andStubReturn(LocationType.VERTEX);
        EasyMock.replay(destinationLocation);

        Set<Hex> expectedHexes = Set.of();

        GameBoard gameBoard = makeGameBoardMock(new HashMap<>(), new HashMap<>(), false);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = makePlacementHelperWithMockedGetters(gameBoard, gamePhaseHolder, false);
        EasyMock.expect(placementHelper.getValidRobberSpots()).andReturn(expectedHexes).times(1);
        EasyMock.replay(placementHelper);

        String expectedMessage = "Cannot place robber at vertex";

        Exception exception = assertThrows(InvalidPlacementException.class, () -> placementHelper.place(pieceToPlace, destinationLocation));
        assertEquals(expectedMessage, exception.getMessage());
        EasyMock.verify(placementHelper, gameBoard);

    }

    private PlacementHelper makePlacementHelperWithMockedGetters(GameBoard gameBoard, GamePhaseHolder gamePhaseHolder, boolean autoReplay) {

        IMockBuilder<PlacementHelper> builder = EasyMock.createMockBuilder(PlacementHelper.class);
        builder.addMockedMethod("getValidSettlementSpots");
        builder.addMockedMethod("getValidCitySpots");
        builder.addMockedMethod("getValidRoadSpots");
        builder.addMockedMethod("getValidRobberSpots");
        builder.addMockedMethod("notifyObserversInternal");
        builder.withConstructor(GameBoard.class, GamePhaseHolder.class);
        builder.withArgs(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = builder.createMock();

        if (autoReplay) {
            EasyMock.replay(placementHelper);
        }

        return placementHelper;

    }

    private Piece makePieceMock(PieceType pieceType, Color color) {

        Class<? extends Piece> pieceClass;
        switch (pieceType) {
            case SETTLEMENT -> pieceClass = Settlement.class;
            case CITY -> pieceClass = City.class;
            case ROAD -> pieceClass = Road.class;
            case HARBOR -> pieceClass = Harbor.class;
            case ROBBER -> pieceClass = Robber.class;
            default -> throw new RuntimeException();
        }
        
        Piece piece = EasyMock.createMock(pieceClass);
        PieceData pieceData = EasyMock.createMock(PieceData.class);

        EasyMock.expect(piece.getPieceData()).andStubReturn(pieceData);
        EasyMock.expect(pieceData.getColor()).andStubReturn(color);
        EasyMock.expect(pieceData.getPieceType()).andStubReturn(pieceType);
        EasyMock.replay(piece, pieceData);

        return piece;

    }

    private Map<String, Vertex> makeVertexMocks(Set<String> vertexNames, Map<String, List<Piece>> vertexPieces) {

        Map<String, Vertex> vertexMocks = new HashMap<>();

        for (String vertexName : vertexNames) {
            Vertex vertex = EasyMock.createMock(Vertex.class);
            vertexMocks.put(vertexName, vertex);

            List<Piece> expectedPieces;
            if (vertexPieces.containsKey(vertexName)) {
                expectedPieces = vertexPieces.get(vertexName);
            }
            else {
                expectedPieces = List.of();
            }
            EasyMock.expect(vertex.getPieces()).andStubReturn(expectedPieces);

            EasyMock.expect(vertex.getLocationType()).andStubReturn(LocationType.VERTEX);

            EasyMock.replay(vertex);
        }

        return vertexMocks;

    }

    private Map<Set<String>, Edge> makeEdgeMocks(Set<Set<String>> edgeNamePairs, Map<Set<String>, List<Piece>> edgePieces) {

        Map<Set<String>, Edge> edgeMocks = new HashMap<>();

        for (Set<String> edgeNamePair : edgeNamePairs) {
            Edge edge = EasyMock.createMock(Edge.class);
            edgeMocks.put(edgeNamePair, edge);

            List<Piece> expectedPieces;
            if (edgePieces.containsKey(edgeNamePair)) {
                expectedPieces = edgePieces.get(edgeNamePair);
            }
            else {
                expectedPieces = List.of();
            }
            EasyMock.expect(edge.getPieces()).andStubReturn(expectedPieces);

            EasyMock.expect(edge.getLocationType()).andStubReturn(LocationType.EDGE);

            EasyMock.replay(edge);
        }

        return edgeMocks;

    }

    private GameBoard makeGameBoardMock(Map<String, Vertex> vertexMap, Map<Set<String>, Edge> edgeMap) {
        return makeGameBoardMock(vertexMap, edgeMap, true);
    }

    private GameBoard makeGameBoardMock(Map<String, Vertex> vertexMap, Map<Set<String>, Edge> edgeMap, boolean autoReplay) {

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        Set<Vertex> landVertices = new HashSet<>(vertexMap.values());
        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        Set<Edge> landEdges = new HashSet<>(edgeMap.values());
        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        Set<String> vertexNames = vertexMap.keySet();
        Set<Set<String>> edgeNamePairs = edgeMap.keySet();

        createVertexConnectionExpectations(vertexMap, edgeMap, vertexNames, gameBoard);
        createEdgeConnectionExpectations(vertexMap, edgeMap, edgeNamePairs, gameBoard);

        if (autoReplay) {
            EasyMock.replay(gameBoard);
        }

        return gameBoard;

    }

    private Set<Vertex> filterVerticesWithNames(Set<String> vertexNames, Map<String, Vertex> vertexMap) {
        Set<Vertex> filteredVertices = new HashSet<>();
        for (String vertexName : vertexNames) {
            if (vertexMap.containsKey(vertexName)) {
                filteredVertices.add(vertexMap.get(vertexName));
            }
        }
        return filteredVertices;
    }

    private Set<Edge> filterEdgesWithNames(Set<Set<String>> edgeNamePairs, Map<Set<String>, Edge> edgeMap) {
        Set<Edge> filteredEdges = new HashSet<>();
        for (Set<String> edgeNamePair : edgeNamePairs) {
            if (edgeMap.containsKey(edgeNamePair)) {
                filteredEdges.add(edgeMap.get(edgeNamePair));
            }
        }
        return filteredEdges;
    }

    private void createVertexConnectionExpectations(Map<String, Vertex> vertexMap, Map<Set<String>, Edge> edgeMap, Set<String> vertexNames, GameBoard gameBoard) {
        for (String vertexName : vertexNames) {

            Vertex vertex = vertexMap.get(vertexName);

            Set<Vertex> neighboringVertices = new HashSet<>();
            Set<Edge> connectingEdges = new HashSet<>();

            constructSingleVertexConnectorSets(vertexMap, edgeMap, vertexName, neighboringVertices, connectingEdges);

            EasyMock.expect(gameBoard.getAdjacentVertices(vertex)).andStubReturn(neighboringVertices);
            EasyMock.expect(gameBoard.getIncidentEdges(vertex)).andStubReturn(connectingEdges);

        }
    }

    private void createEdgeConnectionExpectations(Map<String, Vertex> vertexMap, Map<Set<String>, Edge> edgeMap, Set<Set<String>> edgeNamePairs, GameBoard gameBoard) {
        for (Set<String> edgeNamePair : edgeNamePairs) {

            Edge edge = edgeMap.get(edgeNamePair);

            Set<Edge> connectingEdges = new HashSet<>();
            Set<Vertex> neighboringVertices = new HashSet<>();

            constructSingleEdgeConnectorSets(vertexMap, edgeMap, edgeNamePair, neighboringVertices, connectingEdges);

            EasyMock.expect(gameBoard.getEndpointVertices(edge)).andStubReturn(neighboringVertices);
            EasyMock.expect(gameBoard.getAdjacentEdges(edge)).andStubReturn(connectingEdges);

        }
    }

    private void constructSingleVertexConnectorSets(Map<String, Vertex> vertexMap, Map<Set<String>, Edge> edgeMap, String vertexName, Set<Vertex> neighboringVertices, Set<Edge> connectingEdges) {
        Vertex vertex = vertexMap.get(vertexName);

        for (Map.Entry<Set<String>, Edge> edgeEntry : edgeMap.entrySet()) {
            Set<String> endpointVertexNames = edgeEntry.getKey();
            addVertexConnectorsToSets(vertexMap, vertexName, neighboringVertices, connectingEdges, edgeEntry, endpointVertexNames, vertex);
        }
    }

    private void constructSingleEdgeConnectorSets(Map<String, Vertex> vertexMap, Map<Set<String>, Edge> edgeMap, Set<String> edgeNamePair, Set<Vertex> neighboringVertices, Set<Edge> connectingEdges) {
        Edge edge = edgeMap.get(edgeNamePair);

        for (String endpointVertexName : edgeNamePair) {
            addEdgeConnectorsToSets(vertexMap, edgeMap, neighboringVertices, connectingEdges, endpointVertexName, edge);
        }
    }

    private void addVertexConnectorsToSets(Map<String, Vertex> vertexMap, String vertexName, Set<Vertex> neighboringVertices, Set<Edge> connectingEdges, Map.Entry<Set<String>, Edge> edgeEntry, Set<String> endpointVertexNames, Vertex vertex) {
        if (endpointVertexNames.contains(vertexName)) {
            for (String endpointVertexName : endpointVertexNames) {
                neighboringVertices.add(vertexMap.get(endpointVertexName));
            }
            neighboringVertices.remove(vertex);

            connectingEdges.add(edgeEntry.getValue());
        }
    }

    private void addEdgeConnectorsToSets(Map<String, Vertex> vertexMap, Map<Set<String>, Edge> edgeMap, Set<Vertex> neighboringVertices, Set<Edge> connectingEdges, String endpointVertexName, Edge edge) {
        neighboringVertices.add(vertexMap.get(endpointVertexName));

        for (Map.Entry<Set<String>, Edge> edgeEntry : edgeMap.entrySet()) {
            Set<String> otherEdgeNamePair = edgeEntry.getKey();
            if (otherEdgeNamePair.contains(endpointVertexName)) {
                connectingEdges.add(edgeMap.get(otherEdgeNamePair));
            }
            connectingEdges.remove(edge);
        }
    }

}