package collatzapp;

import java.util.ArrayList;
import java.util.List;

/**
 * handles all the mathematical logic for the Collatz Conjecture
 * @author thiago
 */
public class CollatzModel {
    /**
     * generates the sequence, peak, steps, average growth, and stopping time
     * @param startNum the number inputted 
     * @return aSequenceResult object containing everything
     */
    public SequenceResult calculateSequence(int startNum) {
        //TODO: handling big numbers and special cases (e.g. 27)
        //avgGrowth, stopTimeColla
        int currentNum = startNum;
        int stepsToOne = 0;
        int peakNum = startNum;
        int stopTime = 0;
        List<Integer> sequence = new ArrayList<>();
        double avgGrowth = 0.0;
        
        sequence.add(startNum);
        
        while (currentNum != 1) {
            if (currentNum % 2 == 0) {
                currentNum /= 2;
            }
            else {
                currentNum = (currentNum * 3) + 1;
            }
            
            stepsToOne++;
            sequence.add(currentNum);
            
            if (currentNum > peakNum) {
                peakNum = currentNum;
            }         
        }
        
        return new SequenceResult(startNum, sequence, stepsToOne, peakNum, stopTime, avgGrowth);
    } 
}
