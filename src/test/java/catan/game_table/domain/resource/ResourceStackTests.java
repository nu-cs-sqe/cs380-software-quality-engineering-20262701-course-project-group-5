package catan.game_table.domain.resource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ResourceStackTests {
    @Test
    public void Add_StackOfOne_WithNearMaxStack_ProducesMaxStack(){
        BrickResourceStack stackOfOne = new BrickResourceStack(1);
        BrickResourceStack stackOfNearMax = new BrickResourceStack(Integer.MAX_VALUE - 1);

        ResourceStack resultStack = stackOfNearMax.add(stackOfOne);

        int expected = Integer.MAX_VALUE;

        int actual = resultStack.getSize();

        assertEquals(expected, actual);

        assertInstanceOf(BrickResourceStack.class, resultStack);
    }

    @Test
    public void Add_EmptyStack_WithEmptyStack_ProducesEmptyStack(){
        BrickResourceStack firstStackOfEmpty = new BrickResourceStack(0);
        BrickResourceStack secondStackOfEmpty = new BrickResourceStack(0);

        ResourceStack resultStack = firstStackOfEmpty.add(secondStackOfEmpty);

        int expected = 0;

        int actual = resultStack.getSize();

        assertEquals(expected, actual);

        assertInstanceOf(BrickResourceStack.class, resultStack);
    }

    @Test
    public void Add_EmptyStackLumber_WithEmptyStackBrick_ThrowsIllegalArgumentException(){
        LumberResourceStack stackOfEmptyLumber = new LumberResourceStack(0);
        BrickResourceStack stackOfEmptyBrick = new BrickResourceStack(0);

        Exception combineException = assertThrows(IllegalArgumentException.class, () -> stackOfEmptyLumber.add(stackOfEmptyBrick));

        assertTrue(combineException.getMessage().contains("Incompatible ResourceStack types: LumberResourceStack and BrickResourceStack"));
    }

    @Test
    public void Subtract_MaxStack_WithStackOfOne_ProducesMaxMinusOneStack(){
        BrickResourceStack stackOfMax = new BrickResourceStack(Integer.MAX_VALUE);
        BrickResourceStack stackOfOne = new BrickResourceStack(1);

        ResourceStack resultStack = stackOfMax.subtract(stackOfOne);

        int expected = Integer.MAX_VALUE-1;

        int actual = resultStack.getSize();

        assertEquals(expected, actual);

        assertInstanceOf(BrickResourceStack.class, resultStack);
    }

    @Test
    public void Subtract_StackOfOne_WithStackOfOne_ProducesEmptyStack(){
        BrickResourceStack firstStackOfOne = new BrickResourceStack(1);
        BrickResourceStack secondStackOfOne = new BrickResourceStack(1);

        ResourceStack resultStack = firstStackOfOne.subtract(secondStackOfOne);

        int expected = 0;

        int actual = resultStack.getSize();

        assertEquals(expected, actual);

        assertInstanceOf(BrickResourceStack.class, resultStack);
    }

    @Test
    public void Subtract_StackOfMax_WithStackOfMax_ProducesEmptyStack(){
        BrickResourceStack firstStackOfOne = new BrickResourceStack(Integer.MAX_VALUE);
        BrickResourceStack secondStackOfOne = new BrickResourceStack(Integer.MAX_VALUE);

        ResourceStack resultStack = firstStackOfOne.subtract(secondStackOfOne);

        int expected = 0;

        int actual = resultStack.getSize();

        assertEquals(expected, actual);

        assertInstanceOf(BrickResourceStack.class, resultStack);
    }

    @Test
    public void Subtract_EmptyStackLumber_WithEmptyStackBrick_ThrowsIllegalArgumentException(){
        LumberResourceStack stackOfEmptyLumber = new LumberResourceStack(0);
        BrickResourceStack stackOfEmptyBrick = new BrickResourceStack(0);

        Exception combineException = assertThrows(IllegalArgumentException.class, () -> stackOfEmptyLumber.subtract(stackOfEmptyBrick));

        assertTrue(combineException.getMessage().contains("Incompatible ResourceStack types: LumberResourceStack and BrickResourceStack"));
    }

    @Test
    public void Equals_StackOfOne_WithStackOfOne_ReturnsTrue(){
        BrickResourceStack firstStackOfOne = new BrickResourceStack(1);
        BrickResourceStack secondStackOfOne = new BrickResourceStack(1);

        assertTrue(firstStackOfOne.equals(secondStackOfOne));
    }

    @Test
    public void Equals_StackOfOne_WithStackOfTwo_ReturnsFalse(){
        BrickResourceStack stackOfOne = new BrickResourceStack(1);
        BrickResourceStack stackOfTwo = new BrickResourceStack(2);

        assertFalse(stackOfOne.equals(stackOfTwo));
    }

    @Test
    public void Equals_StackOfOneLumber_WithStackOfOneBrick_ReturnsFalse(){
        LumberResourceStack stackOfOneLumber = new LumberResourceStack(1);
        BrickResourceStack stackOfOneBrick = new BrickResourceStack(1);

        assertFalse(stackOfOneLumber.equals(stackOfOneBrick));
    }

    @Test
    public void Equals_StackOfOneBrick_WithStackOfOneLumber_ReturnsFalse(){
        BrickResourceStack stackOfOneBrick = new BrickResourceStack(1);
        LumberResourceStack stackOfOneLumber = new LumberResourceStack(1);

        assertFalse(stackOfOneBrick.equals(stackOfOneLumber));
    }

    @Test
    public void Equals_StackOfOne_WithThis_ReturnsTrue(){
        BrickResourceStack firstStackOfOne = new BrickResourceStack(1);

        assertTrue(firstStackOfOne.equals(firstStackOfOne));
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
