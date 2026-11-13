package catan.game_table.domain.resource;

public class GrainResourceStack extends  ResourceStack {
    public GrainResourceStack (int size){
        super(size);
    }

    @Override
    protected ResourceStack makeNewStackOfSameType(int size) {
        return new GrainResourceStack(size);
    }
}
