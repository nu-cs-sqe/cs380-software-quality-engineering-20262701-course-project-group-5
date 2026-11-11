package domain.resource;

public interface ResourceInventory {
    BrickResourceStack brickInventory = new BrickResourceStack(0);
    GrainResourceStack grainInventory = new GrainResourceStack(0);
    LumberResourceStack lumberInventory = new LumberResourceStack(0);
    OreResourceStack oreInventory = new OreResourceStack(0);
    WoolResourceStack woolInventory = new WoolResourceStack(0);

    public void addResources(ResourceStack stack);

    public boolean hasResources(ResourceStack stack);

    public void removeResources(ResourceStack stack) throws Exception;
}
