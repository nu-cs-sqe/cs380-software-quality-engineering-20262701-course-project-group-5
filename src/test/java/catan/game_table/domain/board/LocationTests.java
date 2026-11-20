package catan.game_table.domain.board;

import catan.game_table.domain.piece.Harbor;
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

public class LocationTests {

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

    @Test
    void HoldsPieceWithData_OnEmptyVertex_ReturnsFalse() {
        Vertex vertex = new Vertex(new Point(1, 2));
        PieceData settlementData = new PieceData("settlement");

        assertFalse(vertex.holdsPieceWithData(settlementData));
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
