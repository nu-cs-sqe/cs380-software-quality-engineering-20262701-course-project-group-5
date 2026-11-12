package domain.board;

import java.util.Set;

public class GameBoard {
    private final Set<Hex> hexes;
    private static final int MIN_TOKEN_VALUE = 2;
    private static final int MAX_TOKEN_VALUE = 12;

    public GameBoard(Set<Hex> hexes) {
        this.hexes = hexes;
    }

    public void produceResourcesForToken(int tokenValue) {
        requireValidTokenValue(tokenValue);
        for (Hex hex : hexes) {
            if (hex.getTokenValue() == tokenValue) {
                hex.produceResources();
            }
        }
    }

    private static void requireValidTokenValue(int tokenValue) {
        if (tokenValue < MIN_TOKEN_VALUE || tokenValue > MAX_TOKEN_VALUE) {
            throw new IllegalArgumentException(String.format(
                    "Token value %d is outside the valid range [%d, %d]",
                    tokenValue, MIN_TOKEN_VALUE, MAX_TOKEN_VALUE));
        }
    }
}
