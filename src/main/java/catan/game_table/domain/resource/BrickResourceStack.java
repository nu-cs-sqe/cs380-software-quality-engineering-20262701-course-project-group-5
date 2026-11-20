package catan.game_table.domain.resource;

public class BrickResourceStack extends ResourceStack {
    public BrickResourceStack(int size){
        super(size);
    }

    @Override
    protected ResourceStack makeNewStackOfSameType(int size) {
        return new BrickResourceStack(size);
    }
}
