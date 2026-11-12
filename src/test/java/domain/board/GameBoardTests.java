package domain.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GameBoardTests {
    @Test
    void ProduceResourcesForToken_OnBoardWithoutHexes_DoesNothing() {
        GameBoard board = new GameBoard();

        assertDoesNotThrow(() -> board.produceResourcesForToken(6));
    }
}
