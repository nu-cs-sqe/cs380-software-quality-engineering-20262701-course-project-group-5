package domain.board;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void ProduceResourcesForToken_WhenOnlyLastHexMatches_OnlyLastHexProduces() {
        Hex first = hexNotExpectedToProduce(5);
        Hex second = hexNotExpectedToProduce(9);
        Hex third = hexExpectedToProduce(12);
        GameBoard board = boardOf(first, second, third);

        board.produceResourcesForToken(12);

        EasyMock.verify(first, second, third);
    }

    @Test
    void ProduceResourcesForToken_WhenOnlyMiddleHexMatches_OnlyMiddleHexProduces() {
        Hex first = hexNotExpectedToProduce(3);
        Hex second = hexExpectedToProduce(5);
        Hex third = hexNotExpectedToProduce(9);
        GameBoard board = boardOf(first, second, third);

        board.produceResourcesForToken(5);

        EasyMock.verify(first, second, third);
    }

    @Test
    void ProduceResourcesForToken_WhenTwoHexesShareTheToken_BothProduce() {
        Hex firstEight = hexExpectedToProduce(8);
        Hex other = hexNotExpectedToProduce(3);
        Hex secondEight = hexExpectedToProduce(8);
        GameBoard board = boardOf(firstEight, other, secondEight);

        board.produceResourcesForToken(8);

        EasyMock.verify(firstEight, other, secondEight);
    }

    @Test
    void ProduceResourcesForToken_BelowMinimumTokenValue_ThrowsIllegalArgumentException() {
        Hex first = hexNotExpectedToProduce(3);
        Hex second = hexNotExpectedToProduce(4);
        GameBoard board = boardOf(first, second);

        assertThrows(IllegalArgumentException.class, () -> board.produceResourcesForToken(1));
    }

    @Test
    void ProduceResourcesForToken_AboveMaximumTokenValue_ThrowsIllegalArgumentException() {
        Hex first = hexNotExpectedToProduce(3);
        Hex second = hexNotExpectedToProduce(4);
        GameBoard board = boardOf(first, second);

        assertThrows(IllegalArgumentException.class, () -> board.produceResourcesForToken(13));
    }

    @Test
    void ProduceResourcesForToken_WithRobberValueSeven_NoHexProduces() {
        Hex first = hexNotExpectedToProduce(6);
        Hex second = hexNotExpectedToProduce(8);
        GameBoard board = boardOf(first, second);

        board.produceResourcesForToken(7);

        EasyMock.verify(first, second);
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
