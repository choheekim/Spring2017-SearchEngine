package com.chohee.inverted_index_part1;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * PositionLists class contains document frequency and positionMap,
 * which maps from document id to position list, containing position of a term.
 */
public class PostingLists {

    private int docFrequency;
    //map from docId to positionList
    private TreeMap<Integer, PositionLists> positionMap;

    public PostingLists() {
        docFrequency = 1;
        positionMap = new TreeMap<Integer, PositionLists>();

    }

    /**
     * Document frequency getter
     * @return document frequency
     */
    public int getDocFrequency() {
        return docFrequency;
    }

    /**
     * Document frequency setter
     * @param docFrequency
     */
    public void setDocFrequency(int docFrequency) {
        this.docFrequency = docFrequency;
    }

    /**
     * PositionMap getter
     * @return position map
     */
    public TreeMap<Integer, PositionLists> getPositionMap() {
        return positionMap;
    }

    /**
     * PositionMap setter
     * @param positionMap
     */
    public void setPositionMap(TreeMap<Integer, PositionLists> positionMap) {
        this.positionMap = positionMap;
    }

    /**
     * Increease document frequency by 1.
     */
    public void increaseDocFrequency() {
        this.docFrequency++;
    }


    /**
     *
     * @param docId
     * @param position
     */
    public void updatePositionMap(int docId, int position) {

        PositionLists positionLists;

        //if positionMap already contains document_id, then assigns positionLists as value of the key - docId.
        if(positionMap.containsKey(docId)) {
            positionLists = positionMap.get(docId);
        }else {

            //if positionMap doesn't contain document_id, then initialize positionLists with default values.
            List<Integer> positions = new ArrayList<Integer>();
            positionLists = new PositionLists(0 ,positions );

        }

        //update data
        positionLists.addPosition(position);
        positionLists.increaseTermFrequency();
        positionMap.put(docId, positionLists);
    }


}
