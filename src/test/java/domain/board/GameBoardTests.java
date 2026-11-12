package domain.board;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GameBoardTests {
    @Test
    void ProduceResourcesForToken_OnBoardWithoutHexes_DoesNothing() {
        GameBoard board = new GameBoard(new LinkedHashSet<>());

        assertDoesNotThrow(() -> board.produceResourcesForToken(6));
    }

    @Test
    void ProduceResourcesForToken_OnBoardWithOneMatchingHex_AsksThatHexToProduce() {
        Hex hex = EasyMock.createMock(Hex.class);
        hex.produceResources();
        EasyMock.expectLastCall();
        EasyMock.replay(hex);
        GameBoard board = new GameBoard(new LinkedHashSet<>(List.of(hex)));

        board.produceResourcesForToken(8);

        EasyMock.verify(hex);
    }
}
