package Model;

import Main.SequenceResult;
import java.util.List;
import org.junit.jupiter.api.Test; // JUnit 5 import
import static org.junit.jupiter.api.Assertions.*; // JUnit 5 assertions

public class CollatzModelTest {

    private final CollatzModel model = new CollatzModel();

    @Test
    public void testStandardSequence() {
        // Known case: 5 -> 16 -> 8 -> 4 -> 2 -> 1
        long input = 5;
        SequenceResult result = model.calculateSequence(input);
        
        assertEquals(5, result.stepsToReachOne());
        assertEquals(16, result.peakNum());
        
        List<Long> seq = result.sequence();
        assertEquals(Long.valueOf(16), seq.get(1)); // Check second number is 16
        assertEquals(Long.valueOf(1), seq.get(seq.size() - 1)); // Last is 1
    }

    @Test
    public void testBaseCase() {
        // Input 1 should have 0 steps and sequence should contain only [1]
        SequenceResult result = model.calculateSequence(1);
        
        assertEquals(0, result.stepsToReachOne());
        assertEquals(1, result.sequence().size());
        assertEquals(Long.valueOf(1), result.sequence().get(0));
    }

    @Test
    public void testLargeIntegerOverflow() {
        // Test a number that exceeds standard Integer.MAX_VALUE in the sequence
        // 113383 produces a peak of 2,482,111,348 which is > 2.1 billion.
        long input = 113383;
        SequenceResult result = model.calculateSequence(input);
        
        assertTrue(result.peakNum() > Integer.MAX_VALUE, "Peak should exceed integer limit");
        assertEquals(2482111348L, result.peakNum());
    }
}