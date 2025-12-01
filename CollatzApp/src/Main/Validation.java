package Main;

import java.util.ArrayList;
import java.util.List;

/**
 * validates and parses user inputs for the Collatz sequence calculations
 * @author Admin
 */
public class Validation {
    
    private Validation(){}
    
    /**
     * 
     * Parses user's input string into list of positive integers
     * separates by commas and/or spaces
     */
    public static List<Long> parseSeeds(String input){ // RETURN TYPE CHANGED to List<Long>
        List<Long> out = new ArrayList<>();
        
        if (input == null || input.isBlank())
            return out;
        
        // Split by whitespace or commas (e.g., "5, 10 20")
        String[] tokens = input.trim().split("[,\\s]+");
        
        for(String t : tokens){
            // STRICT PARSING:
            // If 't' contains letters (e.g. "27h"), this line crashes immediately.
            long value = Long.parseLong(t.trim());
            
            // Check for non-positive numbers
            if(value <= 0) {
                throw new NumberFormatException("Negative or zero value: " + value);
            }
 
            out.add(value);
        }
        return out;
    }
    
    /**
     * Validation for single seed
     * Returns warning message if invalid, otherwise it will be null
     */
    public static String validateSeed(long seed, long recommendedMax){
        if (seed <= 0){
            return "Only positve integers are allwowed";
        }
        if(seed > recommendedMax){
            return "The input " + seed + " exceeds the recommended limit of " + recommendedMax;
        }
        return null;
    }
    
}
