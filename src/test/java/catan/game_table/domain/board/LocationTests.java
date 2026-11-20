package catan.game_table.domain.board;

import catan.game_table.domain.Color;
import catan.game_table.domain.piece.Harbor;
import catan.game_table.domain.piece.Piece;
import catan.game_table.domain.piece.PieceData;
import catan.game_table.domain.piece.Robber;
import catan.game_table.domain.piece.Road;
import catan.game_table.domain.piece.Settlement;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationTests {

    // tests for addPiece()
    @Test
    void AddPiece_OnEmptyVertex_StoresSettlement() {
        Vertex vertex = new Vertex(new Point(1, 2));
        Settlement settlement = EasyMock.createMock(Settlement.class);
        EasyMock.replay(settlement);

        vertex.addPiece(settlement);

        assertEquals(List.of(settlement), vertex.getPieces());
    }

    @Test
    void AddPiece_OnEdgeWithRoad_AppendsHarbor() {
        Edge edge = new Edge(new Point(0, 0), new Point(1, 0));
        Road road = EasyMock.createMock(Road.class);
        Harbor harbor = EasyMock.createMock(Harbor.class);
        EasyMock.replay(road, harbor);
        edge.addPiece(road);

        edge.addPiece(harbor);

        assertEquals(List.of(road, harbor), edge.getPieces());
    }

    @Test
    void AddPiece_OnEmptyHex_StoresRobber() {
        Hex hex = emptyHex();
        Robber robber = EasyMock.createMock(Robber.class);
        EasyMock.replay(robber);

        hex.addPiece(robber);

        assertEquals(List.of(robber), hex.getPieces());
    }

    // tests for holdsPieceWithData()

    @Test
    void HoldsPieceWithData_OnEmptyVertex_ReturnsFalse() {
        Vertex vertex = new Vertex(new Point(1, 2));
        PieceData settlementData = new PieceData(Color.RED, "settlement");

        assertFalse(vertex.holdsPieceWithData(settlementData));
    }

    @Test
    void HoldsPieceWithData_OnVertexWithMatchingSettlement_ReturnsTrue() {
        PieceData settlementData = new PieceData(Color.RED, "settlement");
        Vertex vertex = vertexWithSettlement(settlementData);

        assertTrue(vertex.holdsPieceWithData(settlementData));
    }

    @Test
    void HoldsPieceWithData_WhenFirstPieceMatches_ReturnsTrue() {
        PieceData roadData = new PieceData(Color.RED, "road");
        Edge edge = edgeWithRoadThenHarbor(roadData, new PieceData(Color.NONE, "harbor"));

        assertTrue(edge.holdsPieceWithData(roadData));
    }

    @Test
    void HoldsPieceWithData_WhenLastPieceMatches_ReturnsTrue() {
        PieceData harborData = new PieceData(Color.NONE, "harbor");
        Edge edge = edgeWithRoadThenHarbor(new PieceData(Color.RED, "road"), harborData);

        assertTrue(edge.holdsPieceWithData(harborData));
    }

    @Test
    void HoldsPieceWithData_WhenNoPieceMatches_ReturnsFalse() {
        Edge edge = edgeWithRoadThenHarbor(
                new PieceData(Color.RED, "road"),
                new PieceData(Color.NONE, "harbor"));
        PieceData settlementData = new PieceData(Color.RED, "settlement");

        assertFalse(edge.holdsPieceWithData(settlementData));
    }

    @Test
    void HoldsPieceWithData_WhenDistinctDataHasSameTypeAndColor_ReturnsTrue() {
        PieceData settlementData = new PieceData(Color.RED, "settlement");
        Vertex vertex = vertexWithSettlement(settlementData);
        PieceData query = new PieceData(Color.RED, "settlement");

        assertTrue(vertex.holdsPieceWithData(query));
    }

    @Test
    void HoldsPieceWithData_WhenRoadColorDiffers_ReturnsFalse() {
        Edge edge = new Edge(new Point(0, 0), new Point(1, 0));
        edge.addPiece(roadWithData(new PieceData(Color.RED, "road")));
        PieceData query = new PieceData(Color.BLUE, "road");

        assertFalse(edge.holdsPieceWithData(query));
    }

    @Test
    void HoldsPieceWithData_WhenPieceTypeDiffers_ReturnsFalse() {
        Vertex vertex = vertexWithSettlement(new PieceData(Color.RED, "settlement"));
        PieceData query = new PieceData(Color.RED, "city");

        assertFalse(vertex.holdsPieceWithData(query));
    }

    // tests for removePieceWithData()

    @Test
    void RemovePieceWithData_OnEmptyVertex_LeavesVertexEmpty() {
        Vertex vertex = new Vertex(new Point(1, 2));
        PieceData settlementData = new PieceData(Color.RED, "settlement");

        vertex.removePieceWithData(settlementData);

        assertTrue(vertex.getPieces().isEmpty());
    }

    @Test
    void RemovePieceWithData_WithOneMatchingSettlement_LeavesVertexEmpty() {
        PieceData settlementData = new PieceData(Color.RED, "settlement");
        Vertex vertex = vertexWithSettlement(settlementData);

        vertex.removePieceWithData(settlementData);

        assertTrue(vertex.getPieces().isEmpty());
    }

    @Test
    void RemovePieceWithData_WhenRoadMatchesFirst_LeavesOnlyHarbor() {
        PieceData roadData = new PieceData(Color.RED, "road");
        Edge edge = edgeWithRoadThenHarbor(roadData, new PieceData(Color.NONE, "harbor"));
        Piece harbor = edge.getPieces().getLast();

        edge.removePieceWithData(roadData);

        assertEquals(List.of(harbor), edge.getPieces());
    }

    @Test
    void RemovePieceWithData_WhenHarborMatchesLast_LeavesOnlyRoad() {
        PieceData harborData = new PieceData(Color.NONE, "harbor");
        Edge edge = edgeWithRoadThenHarbor(new PieceData(Color.RED, "road"), harborData);
        Piece road = edge.getPieces().getFirst();

        edge.removePieceWithData(harborData);

        assertEquals(List.of(road), edge.getPieces());
    }

    @Test
    void RemovePieceWithData_WhenNeitherPieceMatches_LeavesBothPieces() {
        Edge edge = edgeWithRoadThenHarbor(
                new PieceData(Color.RED, "road"),
                new PieceData(Color.NONE, "harbor"));
        List<Piece> originalPieces = edge.getPieces();
        PieceData settlementData = new PieceData(Color.RED, "settlement");

        edge.removePieceWithData(settlementData);

        assertEquals(originalPieces, edge.getPieces());
    }

    @Test
    void RemovePieceWithData_WhenDistinctDataMatches_LeavesVertexEmpty() {
        PieceData settlementData = new PieceData(Color.RED, "settlement");
        Vertex vertex = vertexWithSettlement(settlementData);
        PieceData query = new PieceData(Color.RED, "settlement");

        vertex.removePieceWithData(query);

        assertTrue(vertex.getPieces().isEmpty());
    }

    @Test
    void RemovePieceWithData_WhenRobberMatches_LeavesHexEmpty() {
        Hex hex = emptyHex();
        PieceData robberData = new PieceData(Color.NONE, "robber");
        Robber robber = EasyMock.createMock(Robber.class);
        EasyMock.expect(robber.getPieceData()).andStubReturn(robberData);
        EasyMock.replay(robber);
        hex.addPiece(robber);

        hex.removePieceWithData(robberData);

        assertTrue(hex.getPieces().isEmpty());
    }

    @Test
    void RemovePieceWithData_WhenRoadColorDiffers_LeavesRoadInPlace() {
        Edge edge = new Edge(new Point(0, 0), new Point(1, 0));
        Road road = roadWithData(new PieceData(Color.RED, "road"));
        edge.addPiece(road);
        PieceData query = new PieceData(Color.BLUE, "road");

        edge.removePieceWithData(query);

        assertEquals(List.of(road), edge.getPieces());
    }

    private static Road roadWithData(PieceData roadData) {
        Road road = EasyMock.createMock(Road.class);
        EasyMock.expect(road.getPieceData()).andStubReturn(roadData);
        EasyMock.replay(road);
        return road;
    }

    private static Vertex vertexWithSettlement(PieceData settlementData) {
        Vertex vertex = new Vertex(new Point(1, 2));
        Settlement settlement = EasyMock.createMock(Settlement.class);
        EasyMock.expect(settlement.getPieceData()).andStubReturn(settlementData);
        EasyMock.replay(settlement);
        vertex.addPiece(settlement);

        return vertex;
    }

    private static Edge edgeWithRoadThenHarbor(PieceData roadData, PieceData harborData) {
        Edge edge = new Edge(new Point(0, 0), new Point(1, 0));
        Road road = EasyMock.createMock(Road.class);
        Harbor harbor = EasyMock.createMock(Harbor.class);
        EasyMock.expect(road.getPieceData()).andStubReturn(roadData);
        EasyMock.expect(harbor.getPieceData()).andStubReturn(harborData);
        EasyMock.replay(road, harbor);
        edge.addPiece(road);
        edge.addPiece(harbor);

        return edge;
    }

    private static Hex emptyHex() {
        List<Point> corners = List.of(
                new Point(1, 0),
                new Point(2, 0),
                new Point(3, 1),
                new Point(2, 2),
                new Point(1, 2),
                new Point(0, 1));
        Set<Vertex> vertices = new HashSet<>();
        Set<Edge> edges = new HashSet<>();

        for (int index = 0; index < corners.size(); index++) {
            Point corner = corners.get(index);
            Point nextCorner = corners.get((index + 1) % corners.size());
            vertices.add(new Vertex(corner));
            edges.add(new Edge(corner, nextCorner));
        }

        return new DesertHex(vertices, edges);
    }
}
