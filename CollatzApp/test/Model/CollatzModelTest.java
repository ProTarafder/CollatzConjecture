package Model;

import Main.SequenceResult;
import java.util.List;
import org.junit.Test; // JUnit 4 Import
import static org.junit.Assert.*; // JUnit 4 Assertions

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
        assertEquals(Long.valueOf(16), seq.get(1)); 
        assertEquals(Long.valueOf(1), seq.get(seq.size() - 1)); 
    }

    @Test
    public void testBaseCase() {
        SequenceResult result = model.calculateSequence(1);
        assertEquals(0, result.stepsToReachOne());
        assertEquals(1, result.sequence().size());
        assertEquals(Long.valueOf(1), result.sequence().get(0));
    }

    @Test
    public void testLargeIntegerOverflow() {
        long input = 113383;
        SequenceResult result = model.calculateSequence(input);
        
        // JUnit 4 message is the first argument
        assertTrue("Peak should exceed integer limit", result.peakNum() > Integer.MAX_VALUE);
        assertEquals(2482111348L, result.peakNum());
    }
}