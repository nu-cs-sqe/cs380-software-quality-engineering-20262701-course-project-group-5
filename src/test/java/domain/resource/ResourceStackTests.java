package domain.resource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceStackTests {
    @Test
    public void Combine_EmptyStack_WithNearMaxStack_ProducesMaxStack(){
        BrickResourceStack stackOfOneBrick = new BrickResourceStack(1);
        BrickResourceStack nearMaxStack = new BrickResourceStack(Integer.MAX_VALUE - 1);

        BrickResourceStack maxStack = ResourceStack.combine(nearMaxStack, stackOfOneBrick);

        int expected = Integer.MAX_VALUE;

        int actual = maxStack.getSize();

        assertEquals(expected, actual);
    }

    @Test
    public void Combine_EmptyStack_WithEmptyStack_ProducesEmptyStack(){
        BrickResourceStack emptyStackOne = new BrickResourceStack(0);
        BrickResourceStack emptyStackTwo = new BrickResourceStack(0);

        BrickResourceStack maxStack = ResourceStack.combine(emptyStackOne, emptyStackTwo);

        int expected = 0;

        int actual = maxStack.getSize();

        assertEquals(expected, actual);
    }
}
