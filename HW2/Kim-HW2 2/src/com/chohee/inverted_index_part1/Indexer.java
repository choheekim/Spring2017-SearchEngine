package com.chohee.inverted_index_part1;

import org.lemurproject.kstem.KrovetzStemmer;

import java.util.TreeMap;

/**
 * Created by Chohee on 1/30/17.
 */

public class Indexer {

    private TreeMap<Integer, String> myDocumentTreeMap;
    private TreeMap<String, PostingLists> invertedIndexList;
    private KrovetzStemmer krovetzStemmer;


    /**
     * Constructor
     * @param myDocumentTreeMap
     */
    public Indexer(TreeMap<Integer, String> myDocumentTreeMap) {
        this.myDocumentTreeMap = myDocumentTreeMap;
        invertedIndexList = new TreeMap<>();
        krovetzStemmer = new KrovetzStemmer();
    }


    /**
     * Return true when the given word is stopword, otherwise false
     * @param token
     * @return
     */
    private boolean isStopWord(String token) {

        return token.equals("the") || token.equals("a") || token.equals("at") || token.equals("is") || token.equals("and")
                || token.equals("of") || token.equals("on");
    }


    /**
     * it normalized the given input. Exclude punctuation and makes it to be in lower case
     * @param token
     * @return modified token => term
     */
    private String modifyWord(String token) {


        return krovetzStemmer.stem(token.toLowerCase());

    }

    /**
     * Given a text, which is a whole document, tokenize it.
     * @param text
     * @return tokenized text
     */
    private String[] tokenizer(String text) {

        //replace all new line to be empty string
        text = text.replaceAll("\n", "");

        //replace all punctuation character to be a space.
        text = text.replaceAll("[^a-zA-Z0-9\\s]", " ");

        //Split the given text by the space.
        return text.split(" ");

    }

    /**
     * Update inverted index.
     * if given term exists in the invertedIndexList, then add docId in postings list and increase dFreq(Document Frequency) by 1.
     * else create new key for the given term
     * @param currentTerm
     * @param docId
     */
    private void updateInvertedIndexList(String currentTerm, Integer docId, int position) {

        PostingLists postingLists;

        if (!invertedIndexList.containsKey(currentTerm)) {
            postingLists = new PostingLists();
        } else {
            postingLists = invertedIndexList.get(currentTerm);
            if(!postingLists.getPositionMap().containsKey(docId))
                postingLists.increaseDocFrequency();
        }

        postingLists.updatePositionMap(docId, position);


        invertedIndexList.put(currentTerm, postingLists);
    }






    /**
     * Given scanner, read through the document and return the data structure that maps from its document id to tokenized terms.
     * @return
     */
    public TreeMap<String, PostingLists> getInvertedIndexList() {


        //iterate each document and the text belong to document
        for(Integer docId : myDocumentTreeMap.keySet()) {

            String text = myDocumentTreeMap.get(docId);
            String[] tokens = tokenizer(text);
            int position = 1;

            for(String token : tokens) {


                if(isStopWord(token.toLowerCase()) || token.equals("")) {
                    position += 1;
                    continue;
                }

                updateInvertedIndexList(token.toLowerCase(), docId, position);

                String stemmedWord = krovetzStemmer.stem(token.toLowerCase());
                if(!stemmedWord.equals(token.toLowerCase()) && !isStopWord(stemmedWord))
                    updateInvertedIndexList(krovetzStemmer.stem(token.toLowerCase()), docId, position);
                position += 1;

            }

        }

        return invertedIndexList;

    }


}
