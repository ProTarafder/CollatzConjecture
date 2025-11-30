package Main;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author thiago
 */
public class ValidationTest {
    
    /**
     * test for a valid number inputted
     */
    @Test
    public void testParseValidSingleNumber() {
        List<Long> result = Validation.parseSeeds("27");
        assertEquals(1, result.size());
        assertEquals(Long.valueOf(27), result.get(0));
    }
    
    /**
     * test for multiple numbers inputted
     */
    @Test
    public void testParseValidMultipleNumbers() {
        List<Long> result = Validation.parseSeeds("5, 10,  15");
        assertEquals(List.of(5L, 10L, 15L), result);
    }
    
    /**
     * test for an invalid input
     */
    @Test
    public void testParseInvalidLetters() {
        assertThrows(IllegalArgumentException.class, () -> {
            Validation.parseSeeds("27x");
        });
    }

    /**
     * test for a negative input
     */
    @Test
    public void testParseNegativeNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            Validation.parseSeeds("-5");
        });
    }

    /**
     * test for an empty input
     */
    @Test
    public void testParseEmptyString() {
        List<Long> result = Validation.parseSeeds("");
        assertTrue(result.isEmpty());
    }
    
    /**
     * 
     */
    @Test
    public void testValidateSeed() {
        String warning = Validation.validateSeed(100, 10000);
        assertNull(warning);   // no warning expected
    }

    /**
     * 
     */
    @Test
    public void testValidateSeedLargeWarning() {
        String warning = Validation.validateSeed(5000000, 10000);
        assertNotNull(warning);
        assertTrue(warning.contains("large"));
    }
    
    /**
     * 
     */
    @Test
    public void testValidateSeedVeryLargeWarning() {
        String warning = Validation.validateSeed(100000000000L, 10000);
        assertNotNull(warning);
    }
}
