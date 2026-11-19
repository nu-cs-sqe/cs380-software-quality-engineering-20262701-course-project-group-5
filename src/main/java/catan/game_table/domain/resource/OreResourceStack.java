package catan.game_table.domain.resource;

public class OreResourceStack extends ResourceStack {
    public OreResourceStack(int size){
        super(size);
    }

    @Override
    protected ResourceStack makeNewStackOfSameType(int size) {
        return new OreResourceStack(size);
    }
}
