package domain.board;

import java.util.Set;

public class GameBoard {
    private final Set<Hex> hexes;

    public GameBoard(Set<Hex> hexes) {
        this.hexes = hexes;
    }

    public void produceResourcesForToken(int tokenValue) {
        for (Hex hex : hexes) {
            hex.produceResources();
        }
    }
}
