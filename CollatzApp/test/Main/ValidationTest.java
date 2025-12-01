package Main;

import java.util.List;
import org.junit.Test; // JUnit 4 Import
import static org.junit.Assert.*; // JUnit 4 Assertions

public class ValidationTest {

    @Test
    public void testValidMultipleInputs() {
        String input = "5, 10 20";
        List<Long> result = Validation.parseSeeds(input);
        
        assertEquals(3, result.size());
        assertEquals(Long.valueOf(5), result.get(0));
        assertEquals(Long.valueOf(10), result.get(1));
        assertEquals(Long.valueOf(20), result.get(2));
    }

    // JUnit 4 style: Expect the exception in the annotation
    @Test(expected = NumberFormatException.class)
    public void testStrictLetterRejection() {
        String input = "2727 h";
        Validation.parseSeeds(input); // This should crash
    }

    @Test(expected = NumberFormatException.class)
    public void testNegativeRejection() {
        String input = "-5";
        Validation.parseSeeds(input); // This should crash
    }

    @Test
    public void testEmptyInput() {
        List<Long> result = Validation.parseSeeds("");
        assertTrue(result.isEmpty());
        
        List<Long> resultNull = Validation.parseSeeds(null);
        assertTrue(resultNull.isEmpty());
    }
}