import java.util.*;

/**
 * Created by Chohee on 1/30/17.
 */

public class Query {

    private TreeMap<String, Value> invertedIndexList;
    private String[] searchTerms;


    /**
     * Default Constructor
     */
    public Query() {
        this(null);
    }


    /**
     * Constructor
     * @param searchTerms
     */
    public Query(String[] searchTerms) {
        this.searchTerms = searchTerms;
    }

    /**
     * Setting for invertedIndexList
     * @param invertedIndexList
     */
    public void setInvertedIndexList(TreeMap<String, Value> invertedIndexList) {
        this.invertedIndexList = invertedIndexList;
    }


    /**
     * Setting for serachTerm
     * @param searchTerms
     */
    public void setSearchTerm(String[] searchTerms) {
        this.searchTerms = searchTerms;
    }


    /**
     * List that contains documents that contain both given terms
     * @return documents that contain both given terms
     */
    public List<Integer> runQuery() {

        List<String> terms = regulateSearchTerm();
        List<Integer> result = new LinkedList<>();

        List<List<Integer>> postingLists = new ArrayList<>();


        //iterate each term
        for(String term : terms) {

            //when the given term is in the invertedIndexList
            if(invertedIndexList.containsKey(term)) {

                postingLists.add(invertedIndexList.get(term).getPostingList());

                //System.out.println("Found postingList and added");
            }else {
                return result;
            }
        }


        processANDOperator(result, postingLists);




        return result;

    }


    /**
     * Process AND operating with two given terms.
     * @param result
     * @param postingLists
     */
    private void processANDOperator(List<Integer> result, List<List<Integer>> postingLists) {


        int pnt0 = 0;
        int pnt1 = 0;

        while(pnt0 < postingLists.get(0).size() && pnt1 < postingLists.get(1).size()) {

            int currentDoc1 = postingLists.get(0).get(pnt0);
            int currentDoc2 = postingLists.get(1).get(pnt1);

            if(currentDoc1 == currentDoc2) {
                result.add(currentDoc1);
                pnt0++;
                pnt1++;
            }else if(currentDoc1 < currentDoc2) {
                pnt0++;
            }else {
                pnt1++;
            }

        }
    }


    /**
     * @return List that contains modified term
     */
    private List<String> regulateSearchTerm() {

        List<String> termList = new LinkedList<>();

        for (String term : searchTerms) {

            if(term.equals("AND")) {
                continue;
            }

            if (!isStopWord(term.toLowerCase())) {
                //System.out.println("Term : " + term);
                termList.add(term.toLowerCase());
            }

        }

        return termList;
    }

    /**
     * Return true when the given word is stopword, otherwise false
     * @param word
     * @return
     */
    private boolean isStopWord(String word) {

        return word.equals("the") || word.equals("a") || word.equals("at") || word.equals("is") || word.equals("and")
                || word.equals("of") || word.equals("on");
    }



}

