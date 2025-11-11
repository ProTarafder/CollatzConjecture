/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package collatzapp;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author thiago
 */
public class SequenceResult {
    private final int startNum;
    private final List<Integer> sequence;
    private final int stepsToOne;
    private final int peakNum;
    private final long stopTime;
    private final double avgGrowth;
    
    public SequenceResult(int startNum, List<Integer> sequence, int stepsToOne, int peakNum, long stopTime, double avgGrowth) {
        this.startNum = startNum;
        this.sequence = sequence;
        this.stepsToOne = stepsToOne;
        this.peakNum = peakNum;
        this.stopTime = stopTime;
        this.avgGrowth = avgGrowth;
    }

    @Override
    public String toString() {
        return "SequenceResult{" + "startNum=" + startNum + ", sequence=" + sequence + ", stepsToOne=" + stepsToOne + ", peakNum=" + peakNum + ", stopTime=" + stopTime + ", avgGrowth=" + avgGrowth + '}';
    }    

    public int startNum() { 
        return startNum; 
    }
    
    public List<Integer> sequence() { 
        return sequence; 
    }
    
    public int stepsToReachOne() {
        return stepsToOne;
    }

    public long totalStoppingTime() { 
        return stopTime; 
    }
    
    public int peakNum() {
        return peakNum; 
    }
    
    public double avgGrowth() { 
        return avgGrowth; 
    }
}
