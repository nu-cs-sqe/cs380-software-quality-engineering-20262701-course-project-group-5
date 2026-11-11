package domain.resource;

public class OreResourceStack extends ResourceStack {
    public OreResourceStack(int size){
        super(size);
    }

    @Override
    protected ResourceStack MakeNewStackOfSameType(int size) {
        return new OreResourceStack(size);
    }
}
