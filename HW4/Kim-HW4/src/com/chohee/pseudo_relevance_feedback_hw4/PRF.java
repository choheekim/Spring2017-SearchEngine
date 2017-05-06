package com.chohee.pseudo_relevance_feedback_hw4;

import com.chohee.SimpleSearchEngine;
import com.chohee.inverted_index_part1.Parser;
import com.chohee.inverted_index_part1.PostingLists;
import com.chohee.query_evaluation_part2.QueryEvaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Chohee on 4/9/17.
 */
public class PRF extends QueryEvaluation{

    private int x;
    private String query;

    public PRF(String query, int x) {
        super(query);
        this.query = query;
        this.x = x;

    }

    /**
     * if a list of documents is empty return -1
     * else return top ranked document
     * @return top ranked documents
     */
    public Integer getTopRankedDocId() {
        if(super.getScores().size() == 0)
            return -1;
         else
             return super.getScores().firstKey();

    }

    /**
     * It returns string of the top ranked document contains.
     * @return text in the top ranked document
     */
    public String getDocumentText() {

        TreeMap<String, PostingLists> invertedIndexList = super.getInvertedIndexList();

        String fileName = SimpleSearchEngine.FILE_PATH;
        Scanner sc = null;
        String documentText = null;

        try {
            sc = new Scanner(new File(fileName));
            documentText = new Parser(sc).getMyDocumentTreeSet().get(getTopRankedDocId());

        } catch (FileNotFoundException e) {

            System.out.println("File not found");
            e.printStackTrace();
        }

        return documentText;


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
     * Find the first x term(s) that are in ascending order by tf-idf
     * @return list of top 10 ranked documents
     */
    public String getNewQuery() {

        //get term counter of the each query * (log 10/document frequency)
        int docId = getTopRankedDocId();

        //if docId is -1 then there are no relevant documents.
        if(docId == -1)
            return null;

        TreeMap<String, PostingLists> invertedIndex = getInvertedIndexList();
        TreeMap<String, Double> wordCounter = new TreeMap<>();
        String[] documentText = tokenizer(getDocumentText());



        //Evaluating scores for each terms in the document text
        for(String term : documentText) {

            term = term.toLowerCase();

            if(!invertedIndex.containsKey(term) || term.equals("")) {
                continue;
            }
            PostingLists postingLists = invertedIndex.get(term);

            int docFrequency = postingLists.getDocFrequency();
            double idf = Math.log10(10.0/(double)docFrequency);



            if(postingLists.getPositionMap().get(docId) == null){
                continue;
            }

            int termFrequency = postingLists.getPositionMap().get(docId).getTermFrequency();
            double tf = 1 + Math.log10(termFrequency);

            if(!wordCounter.containsKey(term.toLowerCase())) {
                wordCounter.put(term, tf*idf);

            }

        }

        List<String> wordList = entriesSortedByValues(wordCounter);
        StringJoiner result = new StringJoiner(" ");

        Set<String> wordChecking = new HashSet<>();

        wordChecking.add(this.query);
        result.add(this.query);

        int count = 0;

        for(int i = 0; count < this.x; i++) {

            if(wordChecking.contains(wordList.get(i))) {
                continue;
            }
            result.add(wordList.get(i));
            count++;
        }

        return result.toString();
    }


    /**
     * Sorting Map by Scores
     * @param map
     * @param <K>
     * @param <V>
     * @return List of documents Id in descending order by scores
     */
    private <K,V extends Comparable<? super V>> List<String> entriesSortedByValues(Map<K,V> map) {

        List<Map.Entry<K,V>> sortedEntries = new ArrayList<Map.Entry<K,V>>(map.entrySet());
        List<String> words = new LinkedList<>();

        Collections.sort(sortedEntries,
                new Comparator<Map.Entry<K,V>>() {
                    @Override
                    public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        return e2.getValue().compareTo(e1.getValue());
                    }
                }
        );


        for(Map.Entry sortedEntry : sortedEntries) {

            //System.out.println(sortedEntry.getKey() + " , " + sortedEntry.getValue());
            //docIds.add("Document #" + sortedEntry.getKey() + ", Score : " + formatDecimalNumber((Double) sortedEntry.getValue()));
            words.add(((String) sortedEntry.getKey()).toLowerCase());

        }

        return words;
    }


}
