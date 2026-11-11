package domain.resource;

public interface ResourceInventory {
    public void addResources(ResourceStack stack);

    public boolean hasResources(ResourceStack stack);

    public void removeResources(ResourceStack stack) throws Exception;
}
