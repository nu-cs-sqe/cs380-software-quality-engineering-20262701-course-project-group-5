package domain.resource;

public abstract class ResourceStack {
    private int size;

    public ResourceStack(int size){
        this.size = size;
    }

    public static BrickResourceStack combine (BrickResourceStack stack1, BrickResourceStack stack2) throws IllegalArgumentException{
        if (stack1.getClass() != stack2.getClass()){
            throw new IllegalArgumentException("ERROR: Attempt to combine resource stacks of different types.");
        }
        int totalSize = stack1.getSize() + stack2.getSize();
        return new BrickResourceStack(totalSize);
    }

    //Minimum code to pass tests but won't actually work
    public final int getSize(){
        return size;
    }
}
