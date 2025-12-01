package Main;

import java.util.List;
import org.junit.jupiter.api.Test; // JUnit 5 import
import static org.junit.jupiter.api.Assertions.*; // JUnit 5 assertions

public class ValidationTest {

    @Test
    public void testValidMultipleInputs() {
        // Test parsing comma and space separators
        String input = "5, 10 20";
        List<Long> result = Validation.parseSeeds(input);
        
        assertEquals(3, result.size());
        assertEquals(Long.valueOf(5), result.get(0));
        assertEquals(Long.valueOf(10), result.get(1));
        assertEquals(Long.valueOf(20), result.get(2));
    }

    @Test
    public void testStrictLetterRejection() {
        // Should throw NumberFormatException immediately on "h"
        String input = "2727 h";
        
        assertThrows(NumberFormatException.class, () -> {
            Validation.parseSeeds(input);
        });
    }

    @Test
    public void testNegativeRejection() {
        // Should throw NumberFormatException for negatives due to your strict check
        String input = "-5";
        
        assertThrows(NumberFormatException.class, () -> {
            Validation.parseSeeds(input);
        });
    }

    @Test
    public void testEmptyInput() {
        // Should return empty list, not crash
        List<Long> result = Validation.parseSeeds("");
        assertTrue(result.isEmpty());
        
        List<Long> resultNull = Validation.parseSeeds(null);
        assertTrue(resultNull.isEmpty());
    }
}