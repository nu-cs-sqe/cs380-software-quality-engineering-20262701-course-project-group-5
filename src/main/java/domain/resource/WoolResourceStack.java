package domain.resource;

public class WoolResourceStack extends ResourceStack {
    public  WoolResourceStack(int size){
        super(size);
    }

    @Override
    protected ResourceStack MakeNewStackOfSameType(int size) {
        return new WoolResourceStack(size);
    }
}
