package catan.game_table.domain.piece;

import catan.game_table.domain.resource.ResourceStack;

import java.util.List;

public abstract class Building extends Piece {
    public abstract void produceResources(List<ResourceStack> products);
}
