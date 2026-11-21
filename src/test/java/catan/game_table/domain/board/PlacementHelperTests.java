package catan.game_table.domain.board;

import catan.game_table.domain.Color;
import catan.game_table.domain.GamePhase;
import catan.game_table.domain.GamePhaseHolder;
import catan.game_table.domain.exception.InvalidPlacementException;
import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceData;
import catan.game_table.domain.piece.Road;
import catan.game_table.domain.piece.Settlement;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PlacementHelperTests {

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndEmptyBoard_ExpectAllLandVertices() {

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndEmptyBoard_ExpectNoVertices() {

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndRedSettlementPlaced_ExpectVerticesTwoAwayFromSettlement() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement = makePieceMock(Settlement.class, playerColor);

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("C", "D", "E");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndRedRoadPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(Road.class, playerColor);

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("C", "D");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndRedRoadPlaced_AndBlueSettlementOnRoad_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece otherSettlement = makePieceMock(Settlement.class, Color.BLUE);
        Piece ownRoad = makePieceMock(Road.class, playerColor);

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndTwoSeparateRedRoadsPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece road2 = makePieceMock(Road.class, playerColor);

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("A", "C", "D", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndTwoConnectedRedRoadsPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece road2 = makePieceMock(Road.class, playerColor);

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("C", "D", "E");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndNoOpenBuildingSpotsDueToDistanceRule_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement1 = makePieceMock(Settlement.class, playerColor);
        Piece settlement2 = makePieceMock(Settlement.class, Color.BLUE);
        Piece settlement3 = makePieceMock(Settlement.class, Color.WHITE);

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndBlueRoadPlaced_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(Road.class, Color.BLUE);

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of();
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndRedSettlementPlusTwoLinearRoadsPlaced_ExpectVertexAtEndOfRoads() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(Settlement.class, playerColor);
        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece road2 = makePieceMock(Road.class, playerColor);

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("C");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndRedRoadPlaced_ExpectAllLandVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece road = makePieceMock(Road.class, playerColor);

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndTwoConnectedRedRoadsPlaced_ExpectAllLandVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece road2 = makePieceMock(Road.class, playerColor);

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("A", "B", "C", "D", "E", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenBlue_AndRegularPhase_AndBlueRoadPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.BLUE;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(Road.class, playerColor);

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

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<String> expectedVertexNames = Set.of("A", "F");
        Set<Vertex> expectedVertices = filterVerticesWithNames(expectedVertexNames, vertexMap);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndSetupPhase_AndRedSettlementPlaced_ExpectSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement = makePieceMock(Settlement.class, playerColor);

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

        Piece settlement1 = makePieceMock(Settlement.class, playerColor);
        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece settlement2 = makePieceMock(Settlement.class, playerColor);

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

        Piece settlement = makePieceMock(Settlement.class, playerColor);

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

        Piece road = makePieceMock(Road.class, playerColor);

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

        Piece ownSettlement = makePieceMock(Road.class, playerColor);
        Piece ownRoad = makePieceMock(Road.class, playerColor);
        Piece otherRoad1 = makePieceMock(Road.class, Color.BLUE);
        Piece otherRoad2 = makePieceMock(Road.class, Color.BLUE);
        Piece otherRoad3 = makePieceMock(Road.class, Color.BLUE);
        Piece otherRoad4 = makePieceMock(Road.class, Color.BLUE);
        Piece otherRoad5 = makePieceMock(Road.class, Color.BLUE);

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

        Piece ownSettlement = makePieceMock(Settlement.class, playerColor);
        Piece otherSettlement = makePieceMock(Settlement.class, Color.BLUE);

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

        Piece ownSettlement = makePieceMock(Settlement.class, playerColor);
        Piece otherRoad1 = makePieceMock(Road.class, Color.BLUE);
        Piece otherRoad2 = makePieceMock(Road.class, Color.BLUE);

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

        Piece settlement = makePieceMock(Settlement.class, playerColor);

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

        Piece ownRoad = makePieceMock(Road.class, playerColor);
        Piece otherSettlement = makePieceMock(Settlement.class, Color.BLUE);

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
    public void Place_GivenSettlement_AndValidVertex_ExpectSettlementPlacedAtVertex() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;
        Piece pieceToPlace = makePieceMock(Settlement.class, playerColor);

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

        gameBoard.addPieceAtLocation(pieceToPlace, destinationLocation);
        EasyMock.expectLastCall();

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        assertDoesNotThrow(() -> placementHelper.place(pieceToPlace, destinationLocation));
        EasyMock.verify(gameBoard);

    }

    @Test
    public void Place_GivenSettlement_AndVertexInvalidUnderDistanceRule_ExpectInvalidPlacementException() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;
        Piece pieceToPlace = makePieceMock(Settlement.class, playerColor);
        Piece existingPiece = makePieceMock(Settlement.class, playerColor);

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

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        String expectedMessage = String.format(
                "Cannot place %s at %s: too close to an existing building",
                "building",
                "vertex"
        );

        Exception actualException = assertThrows(InvalidPlacementException.class, () -> placementHelper.place(pieceToPlace, destinationLocation));
        assertEquals(expectedMessage, actualException.getMessage());

        EasyMock.verify(gameBoard);

    }

    @Test
    public void Place_GivenSettlement_AndVertexAlreadyHasSettlement_ExpectInvalidPlacementException() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;
        Piece pieceToPlace = makePieceMock(Settlement.class, playerColor);
        Piece existingPiece = makePieceMock(Settlement.class, playerColor);

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

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        String exceptionMessage = String.format(
                "Cannot place %s at %s: %s already holds a %s",
                "settlement",
                "vertex",
                "vertex",
                "settlement"
        );

        Exception actualException = assertThrows(InvalidPlacementException.class, () -> placementHelper.place(pieceToPlace, destinationLocation));
        assertEquals(exceptionMessage, actualException.getMessage());

        EasyMock.verify(gameBoard);

    }

    private Piece makePieceMock(Class<? extends Piece> pieceClass, Color color) {

        Piece piece = EasyMock.createMock(pieceClass);
        PieceData pieceData = EasyMock.createMock(PieceData.class);

        EasyMock.expect(piece.getPieceData()).andStubReturn(pieceData);
        EasyMock.expect(pieceData.getColor()).andStubReturn(color);
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

            EasyMock.expect(gameBoard.getNeighboringVertices(vertex)).andStubReturn(neighboringVertices);
            EasyMock.expect(gameBoard.getConnectingEdges(vertex)).andStubReturn(connectingEdges);

        }
    }

    private void createEdgeConnectionExpectations(Map<String, Vertex> vertexMap, Map<Set<String>, Edge> edgeMap, Set<Set<String>> edgeNamePairs, GameBoard gameBoard) {
        for (Set<String> edgeNamePair : edgeNamePairs) {

            Edge edge = edgeMap.get(edgeNamePair);

            Set<Edge> connectingEdges = new HashSet<>();
            Set<Vertex> neighboringVertices = new HashSet<>();

            constructSingleEdgeConnectorSets(vertexMap, edgeMap, edgeNamePair, neighboringVertices, connectingEdges);

            EasyMock.expect(gameBoard.getNeighboringVertices(edge)).andStubReturn(neighboringVertices);
            EasyMock.expect(gameBoard.getConnectingEdges(edge)).andStubReturn(connectingEdges);

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