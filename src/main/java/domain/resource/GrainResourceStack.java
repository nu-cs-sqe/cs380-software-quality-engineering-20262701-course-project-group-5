package domain.resource;

public class GrainResourceStack extends  ResourceStack {
    public GrainResourceStack (int size){
        super(size);
    }

    @Override
    protected ResourceStack MakeNewStackOfSameType(int size) {
        return new GrainResourceStack(size);
    }
}
