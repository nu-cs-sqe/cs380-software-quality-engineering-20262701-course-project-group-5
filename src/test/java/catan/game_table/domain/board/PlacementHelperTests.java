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

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PlacementHelperTests {

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndEmptyBoard_ExpectAllLandVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);
        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andStubReturn(List.of());
            EasyMock.replay(vertex);
        }
        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = new HashSet<>(landVertices);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndEmptyBoard_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        Edge edgeBC = EasyMock.createMock(Edge.class);
        Edge edgeCD = EasyMock.createMock(Edge.class);
        Edge edgeDE = EasyMock.createMock(Edge.class);
        Edge edgeEF = EasyMock.createMock(Edge.class);
        Edge edgeFA = EasyMock.createMock(Edge.class);
        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andStubReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.expect(edge.getPieces()).andStubReturn(List.of());
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andStubReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andStubReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andStubReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andStubReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andStubReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = new HashSet<>();

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndRedSettlementPlaced_ExpectVerticesTwoAwayFromSettlement() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement = makePieceMock(Settlement.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of(settlement));

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        EasyMock.expect(gameBoard.getNeighboringVertices(vertexA)).andStubReturn(Set.of(vertexF, vertexB));

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC, vertexD, vertexE);

        assertEquals(expectedVertices, actualVertices);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndRedRoadPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of(road));

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andStubReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andStubReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andStubReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andStubReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andStubReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andStubReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC, vertexD);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndRedRoadPlaced_AndBlueSettlementOnRoad_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(Settlement.class, Color.BLUE);
        Piece road = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of(settlement));

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of(road));

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        EasyMock.expect(gameBoard.getNeighboringVertices(vertexD)).andStubReturn(Set.of(vertexC, vertexE));

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andStubReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andStubReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of();

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndTwoSeparateRedRoadsPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece road2 = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of(road1));

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of(road2));

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andStubReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andStubReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andStubReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andStubReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andStubReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andStubReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC, vertexD, vertexA, vertexF);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndTwoConnectedRedRoadsPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece road2 = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of(road1));

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of(road2));

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andStubReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andStubReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andStubReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andStubReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andStubReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andStubReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC, vertexD, vertexE);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndNoOpenBuildingSpotsDueToDistanceRule_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement1 = makePieceMock(Settlement.class, playerColor);
        Piece settlement2 = makePieceMock(Settlement.class, Color.BLUE);
        Piece settlement3 = makePieceMock(Settlement.class, Color.WHITE);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of(settlement1));

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of(settlement2));

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of(settlement3));

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        EasyMock.expect(gameBoard.getNeighboringVertices(vertexA)).andStubReturn(Set.of(vertexF, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(vertexC)).andStubReturn(Set.of(vertexB, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(vertexE)).andStubReturn(Set.of(vertexD, vertexF));

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of();

        assertEquals(expectedVertices, actualVertices);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndBlueRoadPlaced_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(Road.class, Color.BLUE);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of(road));

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andStubReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andStubReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andStubReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andStubReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andStubReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andStubReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of();

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndRedSettlementPlusTwoLinearRoadsPlaced_ExpectVertexAtEndOfRoads() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(Settlement.class, playerColor);
        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece road2 = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of(settlement));

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of(road1));

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of(road2));

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        EasyMock.expect(gameBoard.getNeighboringVertices(vertexA)).andStubReturn(Set.of(vertexF, vertexB));

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andStubReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andStubReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andStubReturn(Set.of(edgeDE, edgeEF));

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndRedRoadPlaced_ExpectAllLandVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece road = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of(road));

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andStubReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndTwoConnectedRedRoadsPlaced_ExpectAllLandVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece road2 = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of(road1));

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of(road2));

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andStubReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidBuildingSpots_GivenBlue_AndRegularPhase_AndBlueRoadPlaced_ExpectRoadAdjacentVertices() {

        Color playerColor = Color.BLUE;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of(road));

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andStubReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andStubReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andStubReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andStubReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andStubReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andStubReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC, vertexD);

        assertEquals(expectedVertices, actualVertices);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndSetupPhase_AndRedSettlementPlaced_ExpectSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement = makePieceMock(Settlement.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of(settlement));

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andStubReturn(Set.of(vertexA, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andStubReturn(Set.of(vertexB, vertexC));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andStubReturn(Set.of(vertexC, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andStubReturn(Set.of(vertexD, vertexE));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andStubReturn(Set.of(vertexE, vertexF));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andStubReturn(Set.of(vertexF, vertexG));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andStubReturn(Set.of(vertexF, vertexA));

        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeEF, edgeFA, edgeFG));

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeEF, edgeFA, edgeFG);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndSetupPhase_AndSecondRedSettlementJustPlaced_ExpectSecondSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement1 = makePieceMock(Settlement.class, playerColor);
        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece settlement2 = makePieceMock(Settlement.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of(settlement1));

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of(settlement2));

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of(road1));

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andStubReturn(Set.of(vertexA, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andStubReturn(Set.of(vertexB, vertexC));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andStubReturn(Set.of(vertexC, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andStubReturn(Set.of(vertexD, vertexE));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andStubReturn(Set.of(vertexE, vertexF));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andStubReturn(Set.of(vertexF, vertexG));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andStubReturn(Set.of(vertexF, vertexA));

        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andStubReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeEF, edgeFA, edgeFG));

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeEF, edgeFA, edgeFG);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndSetupPhase_AndEmptyBoard_ExpectNoEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of());

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andStubReturn(Set.of(vertexA, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andStubReturn(Set.of(vertexB, vertexC));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andStubReturn(Set.of(vertexC, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andStubReturn(Set.of(vertexD, vertexE));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andStubReturn(Set.of(vertexE, vertexF));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andStubReturn(Set.of(vertexF, vertexG));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andStubReturn(Set.of(vertexF, vertexA));

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeEF)).andStubReturn(Set.of(edgeDE, edgeFA, edgeFG));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFA)).andStubReturn(Set.of(edgeEF, edgeFG, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFG)).andStubReturn(Set.of(edgeFA, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of();

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndEmptyBoard_ExpectNoEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of());

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andStubReturn(Set.of(vertexA, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andStubReturn(Set.of(vertexB, vertexC));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andStubReturn(Set.of(vertexC, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andStubReturn(Set.of(vertexD, vertexE));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andStubReturn(Set.of(vertexE, vertexF));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andStubReturn(Set.of(vertexF, vertexG));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andStubReturn(Set.of(vertexF, vertexA));

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeEF)).andStubReturn(Set.of(edgeDE, edgeFA, edgeFG));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFA)).andStubReturn(Set.of(edgeEF, edgeFG, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFG)).andStubReturn(Set.of(edgeFA, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of();

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedSettlementPlaced_ExpectSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(Settlement.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of(settlement));

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andStubReturn(Set.of(vertexA, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andStubReturn(Set.of(vertexB, vertexC));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andStubReturn(Set.of(vertexC, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andStubReturn(Set.of(vertexD, vertexE));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andStubReturn(Set.of(vertexE, vertexF));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andStubReturn(Set.of(vertexF, vertexG));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andStubReturn(Set.of(vertexF, vertexA));

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));

        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeEF, edgeFA, edgeFG));

        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeEF, edgeFA, edgeFG);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedRoadPlaced_ExpectRoadAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of());

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of(road));

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andStubReturn(Set.of(vertexA, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andStubReturn(Set.of(vertexB, vertexC));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andStubReturn(Set.of(vertexC, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andStubReturn(Set.of(vertexD, vertexE));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andStubReturn(Set.of(vertexE, vertexF));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andStubReturn(Set.of(vertexF, vertexG));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andStubReturn(Set.of(vertexF, vertexA));

        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeEF)).andStubReturn(Set.of(edgeDE, edgeFA, edgeFG));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFA)).andStubReturn(Set.of(edgeEF, edgeFG, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFG)).andStubReturn(Set.of(edgeFA, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeFA, edgeBC);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndAllEdgesOccupied_ExpectNoEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece ownSettlement = makePieceMock(Road.class, playerColor);
        Piece ownRoad = makePieceMock(Road.class, playerColor);
        Piece otherRoad1 = makePieceMock(Road.class, Color.BLUE);
        Piece otherRoad2 = makePieceMock(Road.class, Color.BLUE);
        Piece otherRoad3 = makePieceMock(Road.class, Color.BLUE);
        Piece otherRoad4 = makePieceMock(Road.class, Color.BLUE);
        Piece otherRoad5 = makePieceMock(Road.class, Color.BLUE);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of(ownSettlement));

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of(otherRoad1));

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of(otherRoad2));

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of(otherRoad3));

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of(ownRoad));

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of(otherRoad4));

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andStubReturn(List.of(otherRoad5));

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andStubReturn(Set.of(vertexA, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andStubReturn(Set.of(vertexB, vertexC));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andStubReturn(Set.of(vertexC, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andStubReturn(Set.of(vertexD, vertexE));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andStubReturn(Set.of(vertexE, vertexF));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andStubReturn(Set.of(vertexF, vertexG));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andStubReturn(Set.of(vertexF, vertexA));

        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));

        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of();

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedSettlementPlusBlueSettlementPlaced_ExpectRedSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece ownSettlement = makePieceMock(Settlement.class, playerColor);
        Piece otherSettlement = makePieceMock(Settlement.class, Color.BLUE);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of(otherSettlement));

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of(ownSettlement));

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andStubReturn(Set.of(vertexA, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andStubReturn(Set.of(vertexB, vertexC));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andStubReturn(Set.of(vertexC, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andStubReturn(Set.of(vertexD, vertexE));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andStubReturn(Set.of(vertexE, vertexF));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andStubReturn(Set.of(vertexF, vertexA));

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeEF, edgeFA);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedSettlementPlusTwoConnectingBlueRoadsPlaced_ExpectRemainingEdge() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece ownSettlement = makePieceMock(Settlement.class, playerColor);
        Piece otherRoad1 = makePieceMock(Road.class, Color.BLUE);
        Piece otherRoad2 = makePieceMock(Road.class, Color.BLUE);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of(ownSettlement));

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of(otherRoad1));

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andStubReturn(List.of(otherRoad2));

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andStubReturn(Set.of(vertexA, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andStubReturn(Set.of(vertexB, vertexC));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andStubReturn(Set.of(vertexC, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andStubReturn(Set.of(vertexD, vertexE));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andStubReturn(Set.of(vertexE, vertexF));

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeEF);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenBlue_AndRegularPhase_AndBlueSettlementPlaced_ExpectSettlementAdjacentEdges() {

        Color playerColor = Color.BLUE;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(Settlement.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of(settlement));

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of());

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andStubReturn(Set.of(vertexA, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andStubReturn(Set.of(vertexB, vertexC));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andStubReturn(Set.of(vertexC, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andStubReturn(Set.of(vertexD, vertexE));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andStubReturn(Set.of(vertexE, vertexF));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andStubReturn(Set.of(vertexF, vertexG));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andStubReturn(Set.of(vertexF, vertexA));

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));

        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeEF, edgeFA, edgeFG));

        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeEF, edgeFA, edgeFG);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedRoadPlusAdjacentBlueSettlementPlaced_ExpectUnblockedEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece ownRoad = makePieceMock(Road.class, playerColor);
        Piece otherSettlement = makePieceMock(Settlement.class, Color.BLUE);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of(otherSettlement));

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andStubReturn(List.of());

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andStubReturn(List.of());

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andStubReturn(List.of());

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andStubReturn(List.of());

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andStubReturn(List.of(ownRoad));

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andStubReturn(List.of());

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andStubReturn(List.of());

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andStubReturn(Set.of(vertexA, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andStubReturn(Set.of(vertexB, vertexC));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andStubReturn(Set.of(vertexC, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andStubReturn(Set.of(vertexD, vertexE));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andStubReturn(Set.of(vertexF, vertexA));
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andStubReturn(Set.of(vertexF, vertexG));

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFA)).andStubReturn(Set.of(edgeEF, edgeFG, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFG)).andStubReturn(Set.of(edgeFA, edgeEF));

        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andStubReturn(Set.of(edgeFA, edgeEF, edgeFG));

        EasyMock.expect(gameBoard.getLandEdges()).andStubReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeDE);

        assertEquals(expectedEdges, actualEdges);

    }

    @Test
    public void Place_GivenSettlement_AndValidVertex_ExpectSettlementPlacedAtVertex() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;
        Piece pieceToPlace = makePieceMock(Settlement.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Location destinationLocation = vertexA;

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);
        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andStubReturn(List.of());
            EasyMock.replay(vertex);
        }
        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);

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

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andStubReturn(List.of(existingPiece));

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andStubReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andStubReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andStubReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andStubReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andStubReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Location destinationLocation = vertexB;

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        EasyMock.expect(gameBoard.getLandVertices()).andStubReturn(landVertices);
        EasyMock.expect(gameBoard.getNeighboringVertices(vertexA)).andStubReturn(Set.of(vertexF, vertexB));

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andStubReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        String exceptionMessage = String.format("Cannot place %s at %s: too close to an existing building", Settlement.class.getSimpleName(), Vertex.class.getSimpleName());

        assertThrows(InvalidPlacementException.class, () -> placementHelper.place(pieceToPlace, destinationLocation), exceptionMessage);
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

}