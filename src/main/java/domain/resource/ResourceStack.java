package domain.resource;

import java.util.Objects;

public abstract class ResourceStack {
    private int size;

    public ResourceStack(int size){
        this.size = size;
    }

    public final int getSize(){
        return size;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.getClass().getName()) * (this.size + 1);
    }

    @Override
    public boolean equals(Object o){
        if ( this.getClass().equals(o.getClass()) ){
            ResourceStack otherStack = (ResourceStack) o;
            if (this.size == otherStack.size){
                return true;
            }
        }
        return false;
    }

    public static ResourceStack combine (ResourceStack stack1, ResourceStack stack2) throws IllegalArgumentException{
        if (! (stack1.getClass().equals(stack2.getClass())) ){
            throw new IllegalArgumentException(String.format("Incompatible ResourceStack types: %s and %s", stack1.getClass(), stack2.getClass()) );
        }
        int totalSize = stack1.getSize() + stack2.getSize();
        return MakeNewStackOfSameType(stack1, totalSize);
    }

    private static ResourceStack MakeNewStackOfSameType(ResourceStack stack1, int size){
        Class<?> stackClass = stack1.getClass();
        if (stackClass.equals(BrickResourceStack.class)){
            return new BrickResourceStack(size);
        }
        else if (stackClass.equals(GrainResourceStack.class)){
            return new GrainResourceStack(size);
        }
        else if (stackClass.equals(LumberResourceStack.class)){
            return new LumberResourceStack(size);
        }
        else if (stackClass.equals(OreResourceStack.class)){
            return new OreResourceStack(size);
        }
        else{
            return new WoolResourceStack(size);
        }
    }
}
