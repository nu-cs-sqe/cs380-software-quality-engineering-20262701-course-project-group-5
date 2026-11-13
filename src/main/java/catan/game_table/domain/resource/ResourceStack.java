package catan.game_table.domain.resource;

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
    public boolean equals(Object other){
        if (!(other instanceof ResourceStack)){
            return false;
        }
        if(this.getClass() == other.getClass()){
            ResourceStack otherStack = (ResourceStack) other;
            return this.size == otherStack.size;
        }
        return false;
    }

    public ResourceStack add(ResourceStack otherStack){
        requireSameTypeAs(otherStack);
        return this.makeNewStackOfSameType(size + otherStack.size);
    }

    public ResourceStack subtract(ResourceStack otherStack){
        requireSameTypeAs(otherStack);
        return this.makeNewStackOfSameType(size - otherStack.size);
    }

    private void requireSameTypeAs(ResourceStack otherStack) throws IllegalArgumentException{
        if (this.getClass() != otherStack.getClass()){
            throw new IllegalArgumentException(String.format("Incompatible ResourceStack types: %s and %s", this.getClass().getSimpleName(), otherStack.getClass().getSimpleName()));
        }
    }

    protected abstract ResourceStack makeNewStackOfSameType(int size);
}
