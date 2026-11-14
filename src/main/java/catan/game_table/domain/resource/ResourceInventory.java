package catan.game_table.domain.resource;

import catan.game_table.domain.exception.NotEnoughInventoryException;

public interface ResourceInventory {
    public void addResources(ResourceStack stack);

    public boolean hasResources(ResourceStack stack);

    public void removeResources(ResourceStack stack) throws NotEnoughInventoryException;
}
