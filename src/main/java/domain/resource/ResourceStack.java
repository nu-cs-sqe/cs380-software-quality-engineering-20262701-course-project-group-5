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

    public ResourceStack add (ResourceStack otherStack) throws IllegalArgumentException{
        if (!(this.getClass().equals(otherStack.getClass()))){
            throw new IllegalArgumentException(String.format("Incompatible ResourceStack types: %s and %s", this.getClass().getSimpleName(), otherStack.getClass().getSimpleName()));
        }
        int totalSize = this.getSize() + otherStack.getSize();
        return this.makeNewStackOfSameType(totalSize);
    }

    protected abstract ResourceStack makeNewStackOfSameType(int size);
}
