package domain.resource;

import java.util.Objects;

public abstract class ResourceStack {
    private final int size;

    protected ResourceStack(int size){
        this.size = size;
    }

    public final int getSize(){
        return size;
    }

    @Override
    public int hashCode(){
        return Objects.hash(getClass(), size);
    }

    @Override
    public boolean equals(Object o){
        if (o == null){
            return false;
        }

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
        return stack1.makeNewStackOfSameType(totalSize);
    }

    protected abstract ResourceStack makeNewStackOfSameType(int size);
}
