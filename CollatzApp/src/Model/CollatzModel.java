package Model;

import Main.SequenceResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all the mathematical logic for the Collatz Conjecture
 * @author thiago
 */
public class CollatzModel {
    /**
     * Generates the sequence, peak, steps, average growth, and stopping time
     * @param startNum the number inputted 
     * @return aSequenceResult object containing everything
     */
    public SequenceResult calculateSequence(long startNum) {
        //TODO: handling big numbers
        long startTime = System.nanoTime();
        long currentNum = startNum;
        long stepsToOne = 0;
        long peakNum = startNum;
        List<Long> sequence = new ArrayList<>();
        double sum = startNum;
        sequence.add(startNum);
        
        while (currentNum != 1) {
            if (currentNum % 2 == 0) {
                currentNum /= 2;
            }
            else {
                currentNum = (currentNum * 3) + 1;
            }
            
            sum += currentNum;
            stepsToOne++;
            sequence.add(currentNum);
            
            if (currentNum > peakNum) {
                peakNum = currentNum;
            }         
        }
        
        long endTime = System.nanoTime();
        long stopTime = endTime - startTime;
        double avgGrowth = sum / stepsToOne;
        
        return new SequenceResult(startNum, sequence, stepsToOne, peakNum, stopTime, avgGrowth);
    } 
}
