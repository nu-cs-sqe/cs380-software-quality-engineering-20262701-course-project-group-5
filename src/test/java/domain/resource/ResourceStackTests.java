package domain.resource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceStackTests {
    @Test
    public void Combine_StackOfOne_WithNearMaxStack_ProducesMaxStack(){
        BrickResourceStack stackOfOne = new BrickResourceStack(1);
        BrickResourceStack stackOfNearMax = new BrickResourceStack(Integer.MAX_VALUE - 1);

        BrickResourceStack maxStack = ResourceStack.combine(stackOfNearMax, stackOfOne);

        int expected = Integer.MAX_VALUE;

        int actual = maxStack.getSize();

        assertEquals(expected, actual);
    }

    @Test
    public void Combine_EmptyStack_WithEmptyStack_ProducesEmptyStack(){
        BrickResourceStack firstStackOfEmpty = new BrickResourceStack(0);
        BrickResourceStack secondStackOfEmpty = new BrickResourceStack(0);

        BrickResourceStack maxStack = ResourceStack.combine(firstStackOfEmpty, secondStackOfEmpty);

        int expected = 0;

        int actual = maxStack.getSize();

        assertEquals(expected, actual);
    }

    @Test
    public void Equals_StackOfOne_WithStackOfOne_ReturnsTrue(){
        BrickResourceStack firstStackOfOne = new BrickResourceStack(1);
        BrickResourceStack secondStackOfOne = new BrickResourceStack(1);

        assertTrue(firstStackOfOne.equals(secondStackOfOne));
    }

    @Test
    public void Hash_StackOfOne_WithStackOfOne_ProducesSameHashCode(){
        BrickResourceStack firstStackOfOne = new BrickResourceStack(1);
        BrickResourceStack secondStackOfOne = new BrickResourceStack(1);

        int firstHash = firstStackOfOne.hashCode();
        int secondHash = secondStackOfOne.hashCode();

        assertEquals(firstHash, secondHash);
    }
}
