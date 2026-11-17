package catan.game_table.domain.board;

import catan.game_table.domain.Color;
import catan.game_table.domain.GamePhase;
import catan.game_table.domain.GamePhaseHolder;
import catan.game_table.domain.piece.Piece;
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
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(GamePhase.SETUP);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(Color.RED);
        Set<Vertex> expectedVertices = new HashSet<>(landVertices);

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

    }

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndEmptyBoard_ExpectNoVertices() {

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Edge edgeAB = EasyMock.createNiceMock(Edge.class);
        Edge edgeBC = EasyMock.createNiceMock(Edge.class);
        Edge edgeCD = EasyMock.createNiceMock(Edge.class);
        Edge edgeDE = EasyMock.createNiceMock(Edge.class);
        Edge edgeEF = EasyMock.createNiceMock(Edge.class);
        Edge edgeFA = EasyMock.createNiceMock(Edge.class);
        Set<Edge> landEdges = Set.of(edgeAB, edgeBC, edgeCD, edgeDE, edgeEF, edgeFA);

        GameBoard gameBoard = EasyMock.createMock(GameBoard.class);

        for (Vertex vertex : landVertices) {
            EasyMock.expect(vertex.getPieces()).andReturn(List.of());
            EasyMock.replay(vertex);
        }

        for (Edge edge : landEdges) {
            EasyMock.expect(edge.getPieces()).andReturn(List.of());
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
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(GamePhase.REGULAR);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(Color.RED);
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

        Piece settlement = EasyMock.createMock(Settlement.class);

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
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(GamePhase.SETUP);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(Color.RED);
        Set<Vertex> expectedVertices = Set.of(vertexC, vertexD, vertexE);

        assertEquals(expectedVertices, actualVertices);

        EasyMock.verify(gameBoard, gamePhaseHolder);

        for (Vertex vertex : landVertices) {
            EasyMock.verify(vertex);
        }

    }

}
