import org.lemurproject.kstem.KrovetzStemmer;

import java.util.*;

/**
 * Created by Chohee on 1/30/17.
 */

public class Indexer {

    private TreeMap<Integer, String> myDocumentTreeMap;
    private TreeMap<String,Value> invertedIndexList;
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
    private void updateInvertedIndexList(String currentTerm, Integer docId) {


        List<Integer> postingList;
        Value value;

        //When the invertedIndexList doesn't contain current term,
        // 1. Instantiate a new posting list
        // 2. Instantiate a value, with parameters - 0 and postingList
        if (!invertedIndexList.containsKey(currentTerm)) {
            postingList = new LinkedList<>();
            value = new Value(0, postingList);
        } else {

            value = invertedIndexList.get(currentTerm);
            postingList = value.getPostingList();

        }

        if(!postingList.contains(docId)) {
            postingList.add(docId);
            value.setdFreq(value.getdFreq() + 1);
        }
        invertedIndexList.put(currentTerm, value);
    }


    /**
     * Given scanner, read through the document and return the data structure that maps from its document id to tokenized terms.
     * @return
     */
    public TreeMap<String,Value> getInvertedIndexList() {


        //iterate each document and the text belong to document
        for(Integer docId : myDocumentTreeMap.keySet()) {

            String text = myDocumentTreeMap.get(docId);
            String[] tokens = tokenizer(text);


            for(String token : tokens) {

                String modifiedWord = modifyWord(token);

                if(isStopWord(modifiedWord) || modifiedWord.equals("")) continue;

                updateInvertedIndexList(token.toLowerCase(), docId);
                updateInvertedIndexList(modifiedWord, docId);

            }

        }

        return invertedIndexList;

    }


}
