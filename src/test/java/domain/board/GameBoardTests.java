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
        EasyMock.expect(hex.getTokenValue()).andStubReturn(8);
        EasyMock.replay(hex);
        GameBoard board = new GameBoard(new LinkedHashSet<>(List.of(hex)));

        board.produceResourcesForToken(8);

        EasyMock.verify(hex);
    }

    @Test
    void ProduceResourcesForToken_WhenNoHexMatches_NoHexProduces() {
        Hex first = EasyMock.createMock(Hex.class);
        EasyMock.expect(first.getTokenValue()).andStubReturn(3);
        EasyMock.replay(first);
        Hex second = EasyMock.createMock(Hex.class);
        EasyMock.expect(second.getTokenValue()).andStubReturn(4);
        EasyMock.replay(second);
        Hex third = EasyMock.createMock(Hex.class);
        EasyMock.expect(third.getTokenValue()).andStubReturn(9);
        EasyMock.replay(third);
        GameBoard board = new GameBoard(new LinkedHashSet<>(List.of(first, second, third)));

        board.produceResourcesForToken(6);

        EasyMock.verify(first, second, third);
    }
}
