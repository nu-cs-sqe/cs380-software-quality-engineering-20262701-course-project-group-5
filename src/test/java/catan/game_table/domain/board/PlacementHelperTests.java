package catan.game_table.domain.board;

import catan.game_table.domain.Color;
import catan.game_table.domain.GamePhase;
import catan.game_table.domain.GamePhaseHolder;
import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceData;
import catan.game_table.domain.piece.Road;
import catan.game_table.domain.piece.Settlement;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
            EasyMock.expect(vertex.getPieces()).andReturn(List.of());
            EasyMock.replay(vertex);
        }
        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = new HashSet<>(landVertices);

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

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
            EasyMock.expect(vertex.getPieces()).andReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.expect(edge.getPieces()).andReturn(List.of()).anyTimes();
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = new HashSet<>();

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndRedSettlementPlaced_ExpectVerticesTwoAwayFromSettlement() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement = makePieceMock(Settlement.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of(settlement));

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        EasyMock.expect(gameBoard.getNeighboringVertices(vertexA)).andReturn(Set.of(vertexF, vertexB));

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC, vertexD, vertexE);

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

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
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of(road)).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC, vertexD);

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndRedRoadPlaced_AndBlueSettlementOnRoad_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(Settlement.class, Color.BLUE);
        Piece road = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of());

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of(settlement));

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of(road)).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        EasyMock.expect(gameBoard.getNeighboringVertices(vertexD)).andReturn(Set.of(vertexC, vertexE));

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of();

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

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
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of(road1)).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of(road2)).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC, vertexD, vertexA, vertexF);

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

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
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of(road1)).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of(road2)).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC, vertexD, vertexE);

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndSetupPhase_AndNoOpenBuildingSpotsDueToDistanceRule_ExpectNoVertices() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement1 = makePieceMock(Settlement.class, playerColor);
        Piece settlement2 = makePieceMock(Settlement.class, Color.BLUE);
        Piece settlement3 = makePieceMock(Settlement.class, Color.WHITE);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of(settlement1));

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of(settlement2));

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of(settlement3));

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        EasyMock.expect(gameBoard.getNeighboringVertices(vertexA)).andReturn(Set.of(vertexF, vertexB));
        EasyMock.expect(gameBoard.getNeighboringVertices(vertexC)).andReturn(Set.of(vertexB, vertexD));
        EasyMock.expect(gameBoard.getNeighboringVertices(vertexE)).andReturn(Set.of(vertexD, vertexF));

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of();

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

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
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of(road)).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of();

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndRedSettlementPlusTwoLinearRoadsPlaced_ExpectVertexAtEndOfRoads() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(Settlement.class, playerColor);
        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece road2 = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of(settlement));

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of());

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of());

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of());

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of());

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of());

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of(road1)).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of(road2)).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        EasyMock.expect(gameBoard.getNeighboringVertices(vertexA)).andReturn(Set.of(vertexF, vertexB));

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andReturn(Set.of(edgeDE, edgeEF));

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC);

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

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
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of(road)).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

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
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of(road1)).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of(road2)).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

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
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of(road)).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getConnectingEdges(vertexA)).andReturn(Set.of(edgeFA, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexB)).andReturn(Set.of(edgeAB, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andReturn(Set.of(edgeBC, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexD)).andReturn(Set.of(edgeCD, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexE)).andReturn(Set.of(edgeDE, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andReturn(Set.of(edgeEF, edgeFA));

        EasyMock.expect(gameBoard.getLandVertices()).andReturn(landVertices);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(playerColor);
        Set<Vertex> expectedVertices = Set.of(vertexC, vertexD);

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndSetupPhase_AndRedSettlementPlaced_ExpectSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement = makePieceMock(Settlement.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of(settlement)).anyTimes();

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andReturn(Set.of(vertexA, vertexB)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andReturn(Set.of(vertexB, vertexC)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andReturn(Set.of(vertexC, vertexD)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andReturn(Set.of(vertexD, vertexE)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andReturn(Set.of(vertexE, vertexF)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andReturn(Set.of(vertexF, vertexG)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andReturn(Set.of(vertexF, vertexA)).anyTimes();

        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andReturn(Set.of(edgeEF, edgeFA, edgeFG)).anyTimes();

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeEF, edgeFA, edgeFG);

        assertEquals(expectedEdges, actualEdges);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndSetupPhase_AndSecondRedSettlementJustPlaced_ExpectSecondSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Piece settlement1 = makePieceMock(Settlement.class, playerColor);
        Piece road1 = makePieceMock(Road.class, playerColor);
        Piece settlement2 = makePieceMock(Settlement.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of(settlement1)).anyTimes();

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of(settlement2)).anyTimes();

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of(road1)).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andReturn(Set.of(vertexA, vertexB)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andReturn(Set.of(vertexB, vertexC)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andReturn(Set.of(vertexC, vertexD)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andReturn(Set.of(vertexD, vertexE)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andReturn(Set.of(vertexE, vertexF)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andReturn(Set.of(vertexF, vertexG)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andReturn(Set.of(vertexF, vertexA)).anyTimes();

        EasyMock.expect(gameBoard.getConnectingEdges(vertexC)).andReturn(Set.of(edgeBC, edgeCD)).anyTimes();
        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andReturn(Set.of(edgeEF, edgeFA, edgeFG)).anyTimes();

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeEF, edgeFA, edgeFG);

        assertEquals(expectedEdges, actualEdges);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndSetupPhase_AndEmptyBoard_ExpectNoEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.SETUP;

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andReturn(Set.of(vertexA, vertexB)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andReturn(Set.of(vertexB, vertexC)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andReturn(Set.of(vertexC, vertexD)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andReturn(Set.of(vertexD, vertexE)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andReturn(Set.of(vertexE, vertexF)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andReturn(Set.of(vertexF, vertexG)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andReturn(Set.of(vertexF, vertexA)).anyTimes();

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeEF)).andStubReturn(Set.of(edgeDE, edgeFA, edgeFG));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFA)).andStubReturn(Set.of(edgeEF, edgeFG, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFG)).andStubReturn(Set.of(edgeFA, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of();

        assertEquals(expectedEdges, actualEdges);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndEmptyBoard_ExpectNoEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andReturn(Set.of(vertexA, vertexB)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andReturn(Set.of(vertexB, vertexC)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andReturn(Set.of(vertexC, vertexD)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andReturn(Set.of(vertexD, vertexE)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andReturn(Set.of(vertexE, vertexF)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andReturn(Set.of(vertexF, vertexG)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andReturn(Set.of(vertexF, vertexA)).anyTimes();

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andReturn(Set.of(edgeCD, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeEF)).andReturn(Set.of(edgeDE, edgeFA, edgeFG));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFA)).andReturn(Set.of(edgeEF, edgeFG, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFG)).andReturn(Set.of(edgeFA, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of();

        assertEquals(expectedEdges, actualEdges);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedSettlementPlaced_ExpectSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece settlement = makePieceMock(Settlement.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of(settlement)).anyTimes();

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andReturn(Set.of(vertexA, vertexB)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andReturn(Set.of(vertexB, vertexC)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andReturn(Set.of(vertexC, vertexD)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andReturn(Set.of(vertexD, vertexE)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andReturn(Set.of(vertexE, vertexF)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andReturn(Set.of(vertexF, vertexG)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andReturn(Set.of(vertexF, vertexA)).anyTimes();

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));

        EasyMock.expect(gameBoard.getConnectingEdges(vertexF)).andReturn(Set.of(edgeEF, edgeFA, edgeFG)).anyTimes();

        EasyMock.expect(gameBoard.getLandEdges()).andReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeEF, edgeFA, edgeFG);

        assertEquals(expectedEdges, actualEdges);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedRoadPlaced_ExpectRoadAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece road = makePieceMock(Road.class, playerColor);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of(road)).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andReturn(Set.of(vertexA, vertexB)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andReturn(Set.of(vertexB, vertexC)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andReturn(Set.of(vertexC, vertexD)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andReturn(Set.of(vertexD, vertexE)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andReturn(Set.of(vertexE, vertexF)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andReturn(Set.of(vertexF, vertexG)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andReturn(Set.of(vertexF, vertexA)).anyTimes();

        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andReturn(Set.of(edgeCD, edgeEF));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeEF)).andReturn(Set.of(edgeDE, edgeFA, edgeFG));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFA)).andReturn(Set.of(edgeEF, edgeFG, edgeAB));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeFG)).andReturn(Set.of(edgeFA, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeFA, edgeBC);

        assertEquals(expectedEdges, actualEdges);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

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
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of(ownSettlement)).anyTimes();

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of(otherRoad1)).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of(otherRoad2)).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of(otherRoad3)).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of(ownRoad)).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of(otherRoad4)).anyTimes();

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andReturn(List.of(otherRoad5)).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andReturn(Set.of(vertexA, vertexB)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andReturn(Set.of(vertexB, vertexC)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andReturn(Set.of(vertexC, vertexD)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andReturn(Set.of(vertexD, vertexE)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andReturn(Set.of(vertexE, vertexF)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFG)).andReturn(Set.of(vertexF, vertexG)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andReturn(Set.of(vertexF, vertexA)).anyTimes();

        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andReturn(Set.of(edgeBC, edgeDE));

        EasyMock.expect(gameBoard.getLandEdges()).andReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of();

        assertEquals(expectedEdges, actualEdges);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedSettlementPlusBlueSettlementPlaced_ExpectRedSettlementAdjacentEdges() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece ownSettlement = makePieceMock(Settlement.class, playerColor);
        Piece otherSettlement = makePieceMock(Settlement.class, Color.BLUE);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of(otherSettlement)).anyTimes();

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of(ownSettlement)).anyTimes();

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of()).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andReturn(Set.of(vertexA, vertexB)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andReturn(Set.of(vertexB, vertexC)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andReturn(Set.of(vertexC, vertexD)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andReturn(Set.of(vertexD, vertexE)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andReturn(Set.of(vertexE, vertexF)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeFA)).andReturn(Set.of(vertexF, vertexA)).anyTimes();

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeEF, edgeFA);

        assertEquals(expectedEdges, actualEdges);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    @Test
    public void GetValidRoadSpots_GivenRed_AndRegularPhase_AndRedSettlementPlusTwoConnectingBlueRoadsPlaced_ExpectRemainingEdge() {

        Color playerColor = Color.RED;
        GamePhase gamePhase = GamePhase.REGULAR;

        Piece ownSettlement = makePieceMock(Settlement.class, playerColor);
        Piece otherRoad1 = makePieceMock(Road.class, Color.BLUE);
        Piece otherRoad2 = makePieceMock(Road.class, Color.BLUE);

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexA.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexB = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexB.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexC = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexC.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexD = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexD.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexE = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexE.getPieces()).andReturn(List.of()).anyTimes();

        Vertex vertexF = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexF.getPieces()).andReturn(List.of(ownSettlement)).anyTimes();

        Vertex vertexG = EasyMock.createMock(Vertex.class);
        EasyMock.expect(vertexG.getPieces()).andReturn(List.of()).anyTimes();

        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF, vertexG);

        Edge edgeAB = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeAB.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeBC = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeBC.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeCD = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeCD.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeDE = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeDE.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeEF = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeEF.getPieces()).andReturn(List.of()).anyTimes();

        Edge edgeFA = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFA.getPieces()).andReturn(List.of(otherRoad1)).anyTimes();

        Edge edgeFG = EasyMock.createMock(Edge.class);
        EasyMock.expect(edgeFG.getPieces()).andReturn(List.of(otherRoad2)).anyTimes();

        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA, edgeFG);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.replay(edge);
        }

        EasyMock.expect(gameBoard.getNeighboringVertices(edgeAB)).andReturn(Set.of(vertexA, vertexB)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeBC)).andReturn(Set.of(vertexB, vertexC)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeCD)).andReturn(Set.of(vertexC, vertexD)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeDE)).andReturn(Set.of(vertexD, vertexE)).anyTimes();
        EasyMock.expect(gameBoard.getNeighboringVertices(edgeEF)).andReturn(Set.of(vertexE, vertexF)).anyTimes();

        EasyMock.expect(gameBoard.getConnectingEdges(edgeAB)).andStubReturn(Set.of(edgeFA, edgeBC));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeBC)).andStubReturn(Set.of(edgeAB, edgeCD));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeCD)).andStubReturn(Set.of(edgeBC, edgeDE));
        EasyMock.expect(gameBoard.getConnectingEdges(edgeDE)).andStubReturn(Set.of(edgeCD, edgeEF));

        EasyMock.expect(gameBoard.getLandEdges()).andReturn(landEdges);

        GamePhaseHolder gamePhaseHolder = EasyMock.createMock(GamePhaseHolder.class);
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(gamePhase);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Edge> actualEdges = placementHelper.getValidRoadSpots(playerColor);
        Set<Edge> expectedEdges = Set.of(edgeEF);

        assertEquals(expectedEdges, actualEdges);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.verify(edge);
        }

    }

    private Piece makePieceMock(Class<? extends Piece> pieceClass, Color color) {

        Piece piece = EasyMock.createMock(pieceClass);
        PieceData pieceData = EasyMock.createMock(PieceData.class);

        EasyMock.expect(piece.getPieceData()).andReturn(pieceData).anyTimes();
        EasyMock.expect(pieceData.getColor()).andStubReturn(color);
        EasyMock.replay(piece, pieceData);

        return piece;

    }

}