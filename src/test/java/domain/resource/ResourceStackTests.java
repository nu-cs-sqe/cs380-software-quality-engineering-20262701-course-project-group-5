package domain.resource;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceStackTests {
    @Test
    public void Combine_EmptyStack_WithNearMaxStack_Success(){
        BrickResourceStack stackOfOneBrick = new BrickResourceStack(1);
        BrickResourceStack nearMaxStack = new BrickResourceStack(Integer.MAX_VALUE - 1);

        stackOfOneBrick.combine(nearMaxStack);

        int expected = Integer.MAX_VALUE;

        int actual = stackOfOneBrick.getSize;

        assertTrue(expected, actual);
    }
}
