package domain.board;

import java.util.Set;

public class GameBoard {
    private final Set<Hex> hexes;

    public GameBoard(Set<Hex> hexes) {
        this.hexes = hexes;
    }

    public void produceResourcesForToken(int tokenValue) {
        if (tokenValue == 1) {
            throw new IllegalArgumentException();
        }
        for (Hex hex : hexes) {
            if (hex.getTokenValue() == tokenValue) {
                hex.produceResources();
            }
        }
    }
}
