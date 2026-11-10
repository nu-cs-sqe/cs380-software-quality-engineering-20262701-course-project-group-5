package domain.resource;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceStackTests {
    @Test
    public void Combine_EmptyStack_WithNearMaxStack_Success(){
        BrickResourceStack stackOfOneBrick = new BrickResourceStack(1);
        BrickResourceStack nearMaxStack = new BrickResourceStack(Integer.MAX_VALUE - 1);

        BrickResourceStack maxStack = stackOfOneBrick.combine(nearMaxStack, stackOfOneBrick);

        int expected = Integer.MAX_VALUE;

        int actual = maxStack.getSize();

        assertTrue(expected, actual);
    }
}
