package domain.resource;

public class LumberResourceStack extends ResourceStack {
    public LumberResourceStack(int size){
        super(size);
    }

    @Override
    protected ResourceStack makeNewStackOfSameType(int size) {
        return new LumberResourceStack(size);
    }
}
