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

    @Test
    public void GetValidBuildingSpots_GivenRed_AndRegularPhase_AndRedRoadPlaced_ExpectRoadAdjacentVertices() {

        Vertex vertexA = EasyMock.createMock(Vertex.class);
        Vertex vertexB = EasyMock.createMock(Vertex.class);
        Vertex vertexC = EasyMock.createMock(Vertex.class);
        Vertex vertexD = EasyMock.createMock(Vertex.class);
        Vertex vertexE = EasyMock.createMock(Vertex.class);
        Vertex vertexF = EasyMock.createMock(Vertex.class);
        Set<Vertex> landVertices = Set.of(vertexA, vertexB, vertexC, vertexD, vertexE, vertexF);

        Piece road = EasyMock.createMock(Road.class);
        PieceData roadData = EasyMock.createMock(PieceData.class);
        EasyMock.expect(road.getPieceData()).andReturn(roadData).anyTimes();
        EasyMock.expect(roadData.getColor()).andStubReturn(Color.RED);
        EasyMock.replay(road, roadData);

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
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(GamePhase.REGULAR);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(Color.RED);
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

        Piece settlement = EasyMock.createMock(Settlement.class);

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

        Piece road = EasyMock.createMock(Road.class);
        PieceData roadData = EasyMock.createMock(PieceData.class);
        EasyMock.expect(road.getPieceData()).andReturn(roadData).anyTimes();
        EasyMock.expect(roadData.getColor()).andStubReturn(Color.RED);
        EasyMock.replay(road, roadData);

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
        EasyMock.expect(gamePhaseHolder.getGamePhase()).andReturn(GamePhase.REGULAR);

        EasyMock.replay(gameBoard, gamePhaseHolder);

        PlacementHelper placementHelper = new PlacementHelper(gameBoard, gamePhaseHolder);

        Set<Vertex> actualVertices = placementHelper.getValidBuildingSpots(Color.RED);
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

}
