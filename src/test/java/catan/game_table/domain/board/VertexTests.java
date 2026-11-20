package catan.game_table.domain.board;

import catan.game_table.domain.piece.Settlement;
import catan.game_table.domain.resource.BrickResourceStack;
import catan.game_table.domain.resource.ResourceStack;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class VertexTests {

    @Test
    void ProduceResources_OnEmptyVertex_DoesNothing() {
        Vertex vertex = new Vertex(new Point(1, 2));
        List<ResourceStack> products = List.of(new BrickResourceStack(1));

        assertDoesNotThrow(() -> vertex.produceResources(products));
    }

    @Test
    void ProduceResources_WithSettlement_ForwardsOriginalListOnce() {
        Vertex vertex = new Vertex(new Point(1, 2));
        List<ResourceStack> products = List.of(new BrickResourceStack(1));
        Settlement settlement = EasyMock.createMock(Settlement.class);
        settlement.produceResources(EasyMock.same(products));
        EasyMock.expectLastCall().once();
        EasyMock.replay(settlement);
        vertex.addPiece(settlement);

        vertex.produceResources(products);

        EasyMock.verify(settlement);
    }
}
