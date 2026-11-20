package catan.game_table.domain.board;

import catan.game_table.domain.piece.Harbor;
import catan.game_table.domain.piece.Road;
import catan.game_table.domain.piece.Settlement;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
