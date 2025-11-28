/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class Validation {
    
    private Validation(){}
    
    /**
     * 
     * Parses user's input string into list of positive integers
     * separates by commas and/or spaces
     */
    public static List<Integer> parseSeeds(String input){
        List<Integer> out = new ArrayList<>();
        
        if (input == null || input.isBlank())
            return out;
        
        String[] tokens = input.trim().split("[\\s,]+");
        
        for(String t : tokens){
            try{
                int value = Integer.parseInt(t);
                if( value> 0) out.add(value);
            }catch (NumberFormatException ignored){
                //skips invalid entries
            }
        }
        
        return out;
    }
    
    /**
     * Validation for single seed
     * Returns warning msg if invalid, otherwise it will be null
     */
    public static String validateSeed(int seed, int recommendedMax){
        if (seed <= 0){
            return "Only positve integers are allwowed";
        }
        if(seed > recommendedMax){
            return "The input " + seed + " exceeds the limit of " + recommendedMax;
        }
        return null;
    }
    
}
