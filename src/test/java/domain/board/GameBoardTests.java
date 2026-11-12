package domain.board;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GameBoardTests {

    @Test
    void ProduceResourcesForToken_OnBoardWithoutHexes_DoesNothing() {
        GameBoard board = boardOf();

        assertDoesNotThrow(() -> board.produceResourcesForToken(6));
    }

    @Test
    void ProduceResourcesForToken_OnBoardWithOneMatchingHex_AsksThatHexToProduce() {
        Hex hex = hexExpectedToProduce(8);
        GameBoard board = boardOf(hex);

        board.produceResourcesForToken(8);

        EasyMock.verify(hex);
    }

    @Test
    void ProduceResourcesForToken_WhenNoHexMatches_NoHexProduces() {
        Hex first = hexNotExpectedToProduce(3);
        Hex second = hexNotExpectedToProduce(4);
        Hex third = hexNotExpectedToProduce(9);
        GameBoard board = boardOf(first, second, third);

        board.produceResourcesForToken(6);

        EasyMock.verify(first, second, third);
    }

    @Test
    void ProduceResourcesForToken_WhenOnlyFirstHexMatches_OnlyFirstHexProduces() {
        Hex first = hexExpectedToProduce(2);
        Hex second = hexNotExpectedToProduce(5);
        Hex third = hexNotExpectedToProduce(9);
        GameBoard board = boardOf(first, second, third);

        board.produceResourcesForToken(2);

        EasyMock.verify(first, second, third);
    }

    private static GameBoard boardOf(Hex... hexes) {
        return new GameBoard(new LinkedHashSet<>(List.of(hexes)));
    }

    private static Hex hexExpectedToProduce(int tokenValue) {
        Hex hex = EasyMock.createMock(Hex.class);
        EasyMock.expect(hex.getTokenValue()).andStubReturn(tokenValue);
        hex.produceResources();
        EasyMock.expectLastCall();
        EasyMock.replay(hex);
        return hex;
    }

    private static Hex hexNotExpectedToProduce(int tokenValue) {
        Hex hex = EasyMock.createMock(Hex.class);
        EasyMock.expect(hex.getTokenValue()).andStubReturn(tokenValue);
        EasyMock.replay(hex);
        return hex;
    }
}
