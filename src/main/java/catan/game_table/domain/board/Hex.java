package catan.game_table.domain.board;

public abstract class Hex extends Location {
    public abstract void produceResources();
    public abstract int getTokenValue();
}
