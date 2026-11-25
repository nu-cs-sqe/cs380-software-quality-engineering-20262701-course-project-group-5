package catan.game_table.domain.bank;

import catan.game_table.domain.exception.NotEnoughInventoryException;
import catan.game_table.domain.resource.ResourceInventory;
import catan.game_table.domain.resource.ResourceStack;
import catan.mvc_util.Observed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank extends Observed implements ResourceInventory {

    private final Map<Class<? extends ResourceStack>, ResourceStack> resourcesByType = new HashMap<>();

    public Bank(List<ResourceStack> startingResources) {
        startingResources.forEach(this::storeResources);
    }

    @Override
    public void addResources(ResourceStack stack) {
        storeResources(stack);
    }

    @Override
    public boolean hasResources(ResourceStack stack) {
        return getResourceCount(stack.getClass()) >= stack.getSize();
    }

    @Override
    public void removeResources(ResourceStack stack) throws NotEnoughInventoryException {
        if (!hasResources(stack)) {
            throw new NotEnoughInventoryException();
        }
        resourcesByType.computeIfPresent(stack.getClass(), (type, stored) -> stored.subtract(stack));
    }

    public int getResourceCount(Class<? extends ResourceStack> resourceType) {
        ResourceStack stored = resourcesByType.get(resourceType);
        return stored == null ? 0 : stored.getSize();
    }

    private void storeResources(ResourceStack stack) {
        resourcesByType.merge(stack.getClass(), stack, ResourceStack::add);
    }
}
