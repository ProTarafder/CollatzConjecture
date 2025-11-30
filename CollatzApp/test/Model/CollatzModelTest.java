package Model;

import Main.SequenceResult;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author thiago
 */
public class CollatzModelTest {
    
    /**
     * test for number 6
     */
    @Test
    public void testCalculateSequenceInput6() {
        CollatzModel model = new CollatzModel();
        
        SequenceResult result = model.calculateSequence(6);

        // Expected sequence for 6
        List<Long> expected = Arrays.asList(6L, 3L, 10L, 5L, 16L, 8L, 4L, 2L, 1L );

        assertEquals(6L, result.startNum());
        assertEquals(expected, result.sequence());
        assertEquals(8, result.stepsToReachOne());
        assertEquals(16L, result.peakNum());
        
        // Average growth = sum / steps
        double sum = 6+3+10+5+16+8+4+2+1; // = 55
        double expectedAvg = sum / 8.0;

        assertEquals(expectedAvg, result.avgGrowth(), 0.0001);

        // Stopping time should be > 0
        assertTrue(result.totalStoppingTime() > 0);

        // Last element should be 1
        assertEquals(Long.valueOf(1), 
                     result.sequence().get(result.sequence().size() - 1));
    }
    
    /**
     * test for number 1
     */
    @Test
    public void testCalculateSequenceInput1() {
        CollatzModel model = new CollatzModel();

        SequenceResult result = model.calculateSequence(1);

        // Sequence should just contain [1]
        assertEquals(Arrays.asList(1L), result.sequence());
        assertEquals(0, result.stepsToReachOne());
        assertEquals(1L, result.peakNum());
        assertEquals(1L, result.startNum());
    }
}
    
