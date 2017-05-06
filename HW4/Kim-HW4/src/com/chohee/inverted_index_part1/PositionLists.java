package com.chohee.inverted_index_part1;

import java.util.List;

/**
 * The PositionLists class contains termFrequency and positions as a list.
 */
public class PositionLists {

    private int termFrequency;
    private List<Integer> positions;

    public PositionLists(int termFrequency, List<Integer> positions) {
        this.termFrequency = termFrequency;
        this.positions = positions;
    }

    public int getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(int termFrequency) {
        this.termFrequency = termFrequency;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    /**
     * Adding a given position to positions list.
     * @param position
     */
    public void addPosition(int position) {
        positions.add(position);
    }

    /**
     * increase term frequency by 1.
     */
    public void increaseTermFrequency() {
        this.termFrequency++;
    }
}
