/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author thiago
 */
public class SequenceResult {

    private final long startNum;
    private final List<Long> sequence;
    private final long stepsToOne;
    private final long peakNum;
    private final long stopTime;
    private final double avgGrowth;

    public SequenceResult(long startNum, List<Long> sequence, long stepsToOne, long peakNum, long stopTime, double avgGrowth) {
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

    public long startNum() {
        return startNum;
    }

    public List<Long> sequence() {
        return sequence;
    }

    public long stepsToReachOne() {
        return stepsToOne;
    }

    public long totalStoppingTime() {
        return stopTime;
    }

    public long peakNum() {
        return peakNum;
    }

    public double avgGrowth() {
        return avgGrowth;
    }
}
