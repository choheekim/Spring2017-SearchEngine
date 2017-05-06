package com.chohee.query_evaluation_part2;

import com.chohee.SimpleSearchEngine;
import com.chohee.inverted_index_part1.Indexer;
import com.chohee.inverted_index_part1.Parser;
import com.chohee.inverted_index_part1.PositionLists;
import com.chohee.inverted_index_part1.PostingLists;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;


public class QueryEvaluation {

    private List<ProximityOperator> proximityOperatorList;
    private List<String> freeTerms;
    private String query;

    public QueryEvaluation(String query) {
        this.query = query;
        freeTerms = new ArrayList<>();
        proximityOperatorList = new ArrayList<>();
    }

    /**
     * Evalutate tf and idf and store it in a map - scores which maps from docId to scores
     * @return
     */
    public List<String> getScores() {

        //get term counter of the each query * (log 10/document frequency)
        Set<Integer>  documentSubset = filtering();
        TreeMap<String, PostingLists> invertedIndex = getInvertedIndexList();
        TreeMap<Integer, Double> scores = new TreeMap<>();

        List<String> freeTermsAndProximityTerms = new ArrayList<>();

        //add freeterms and proximity operators terms in freeTermAndProximityTerms
        for(String terms : freeTerms)
            freeTermsAndProximityTerms.add(terms);

        for(ProximityOperator proximityOperator: proximityOperatorList) {

            freeTermsAndProximityTerms.add(proximityOperator.getTerm1());
            freeTermsAndProximityTerms.add(proximityOperator.getTerm2());

        }

        //Evaluating scores for each terms
        for(String terms : freeTermsAndProximityTerms) {

            PostingLists postingLists = invertedIndex.get(terms);

            int docFrequency = postingLists.getDocFrequency();
            double idf = Math.log10(10.0/docFrequency);

            for(Integer docId : documentSubset) {


                if(postingLists.getPositionMap().get(docId) == null){
                    continue;
                }

                int termFrequency = postingLists.getPositionMap().get(docId).getTermFrequency();
                double tf = 1 + Math.log10(termFrequency);

                if(scores.containsKey(docId)) {

                    scores.put(docId, scores.get(docId) + tf*idf);

                }else {
                    scores.put(docId, tf*idf);
                }

            }

        }


        List<String> docId = entriesSortedByValues(scores);

        for(String each : docId)
            System.out.println(each);


        return docId;
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
        List<String> docIds = new LinkedList<>();

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
            docIds.add("Document #" + sortedEntry.getKey() + ", Score : " + formatDecimalNumber((Double) sortedEntry.getValue()));
        }

        return docIds;
    }


    /**
     * formatDecimalNumber() formats double to have only two decimal places
     * @param score
     * @return two decimal place double
     */
    private String formatDecimalNumber(Double score) {

        DecimalFormat df = new DecimalFormat("#.000");
        String angleFormated = df.format(score);
        return angleFormated;
    }


    /**
     * filtering() filters documentation.
     * if the query doesn't have proximity operator at all, then it will return all document_ids from 1-10, as considering OR operator.
     * Otherwise, return the subset of document_ids that match proximity operator
     * @return
     */
    public Set<Integer> filtering() {

        TreeMap<String, PostingLists> invertedIndex = getInvertedIndexList();
        Set<Integer> documentSubset = new HashSet<>();

        //when the query has proximity operator
        if(containsProximityOperator(query)) {

            findProximityOperator(query);

            //when there is only one proximity operator
            if(proximityOperatorList.size() == 1) {
                return getContainsList(invertedIndex, proximityOperatorList.get(0));


            }else {
            //when there are more than one proximity operator
                Set<Integer> result = new HashSet<>();
                Set<Integer> secondQueryPosition = getContainsList(invertedIndex, proximityOperatorList.get(1));

                for(Integer docId : getContainsList(invertedIndex, proximityOperatorList.get(0))) {
                    if(secondQueryPosition.contains(docId))
                    result.add(docId);
                }

              /**  for(Integer docId : secondQueryPosition)
                    result.add(docId);
                **/
                return result;
            }


        }else {

           // System.out.println("There is no proximity operator.");
            for(String term : query.split(" ")) {
                freeTerms.add(term);
            }

            for(int i = 1; i < 11; i++)
                documentSubset.add(i);
        }

        return documentSubset;

    }

    /**
     * getContainsList() finds subset of documents that matche proximity operator
     * @param invertedIndex
     * @param proximityOperator
     * @return subset of documents that match with proximity operator
     */
    private Set<Integer> getContainsList(TreeMap<String, PostingLists> invertedIndex,ProximityOperator proximityOperator ) {

        //The set will contain the subset of documents that have given query terms.
        Set<Integer> result = new HashSet<>();

        int n = proximityOperator.getN();
        String term1 = proximityOperator.getTerm1();
        String term2 = proximityOperator.getTerm2();

        TreeMap<Integer, PositionLists> term1List = invertedIndex.get(term1).getPositionMap();
        TreeMap<Integer, PositionLists> term2List = invertedIndex.get(term2).getPositionMap();

        for(Integer docId : term1List.keySet()) {

            if(!term2List.containsKey(docId))
                continue;

            List<Integer> term1PositionList = term1List.get(docId).getPositions();
            List<Integer> term2PositionList = term2List.get(docId).getPositions();

            int pointer1 = 0;
            int pointer2 = 0;

            while(pointer1 < term1PositionList.size() && pointer2 < term2PositionList.size()) {

                int term1Current = term1PositionList.get(pointer1);
                int term2Current = term2PositionList.get(pointer2);

                //term1 position is bigger than term2 position -> not in order so break.
                if(term1Current > term2Current ) {
                    break;
                }
                else if((term2Current - term1Current) <= n+1 ){
                    result.add(docId);
                    break;
                }else {
                    pointer2++;
                }
            }

        }
        return result;

    }

    /**
     * When a given query contains proximity operator, find n, term1, and term2 and add to a proximityOperatorList for further evaluation.
     * When it is not proximity operator, add to freeterm List.
     * @param query
     */
    private void findProximityOperator(String query) {

        String[] terms = query.split(" ");

        for(int i = 0; i < terms.length; i++) {

            //if the term contains (, it indicates starting of proximity operator
            if(terms[i].contains("(")) {

                //As the form is n(term1, term2) first char is n, following term is term1 and the last element is term2.
                Integer n = Character.getNumericValue(terms[i].charAt(0));
                String term1 = terms[i].substring(1, terms[i].length()).replace("(", "");
                String term2 = terms[++i].replace(")", "");

                ProximityOperator proximityOperator = new ProximityOperator(n, term1, term2);
                proximityOperatorList.add(proximityOperator);

            }else {
                //otherwise, it is freeterm, so add to freeterm list.
                freeTerms.add(terms[i]);
            }

        }

    }

    /**
     * It checks if a query contains proximity operator.
     * @param query
     * @return Return true if given query contains proximity operator. Otherwise, false.
     */
    private boolean containsProximityOperator(String query) {

        return query.contains("(") && query.contains(")");
    }

    /**
     * Simply getting inverted_index from Indexer class
     * @return inverted_index
     */
    private TreeMap<String, PostingLists> getInvertedIndexList() {

        String fileName = SimpleSearchEngine.FILE_PATH;
        Scanner sc = null;
        TreeMap<String, PostingLists> invertedIndex = null;

        try {
            sc = new Scanner(new File(fileName));
            invertedIndex = new Indexer(new Parser(sc).getMyDocumentTreeSet()).getInvertedIndexList();

        } catch (FileNotFoundException e) {

            System.out.println("File not found");
            e.printStackTrace();
        }

        return invertedIndex;
    }


}
