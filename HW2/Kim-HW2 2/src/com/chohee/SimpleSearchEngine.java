package com.chohee;

import com.chohee.inverted_index_part1.Indexer;
import com.chohee.inverted_index_part1.Parser;
import com.chohee.inverted_index_part1.PositionLists;
import com.chohee.inverted_index_part1.PostingLists;
import com.chohee.query_evaluation_part2.QueryEvaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.*;


public class SimpleSearchEngine {

    public static final String FILE_PATH = Paths.get("").toAbsolutePath().toString() + "/com/chohee/documents.txt";

    public static void main(String[] args) {

        //Part3. a
        createFile();

        //Part3. b
        List<String> queries = new ArrayList<>();
        queries.add("nexus like love happy");
        queries.add("asus repair");
        queries.add("0(touch screen) fix repair");
        queries.add("1(great tablet) 2(tablet fast)");
        queries.add("tablet");



        try {

            PrintWriter printWriter = new PrintWriter("./result_file.txt", "UTF-8");

            for(String query: queries) {

                QueryEvaluation queryEvaluation = new QueryEvaluation(query);

                printWriter.println(query);
                System.out.println(query);

                for(String str : queryEvaluation.getScores()) {

                    printWriter.println(str);
                }

                System.out.println("\n");
                printWriter.write("\n");

            }

            printWriter.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }


    private static void createFile() {

        String fileName = FILE_PATH;
        Scanner sc = null;

        try {
            sc = new Scanner(new File(fileName));
            TreeMap<String, PostingLists> invertedIndex = new Indexer(new Parser(sc).getMyDocumentTreeSet()).getInvertedIndexList();
            writeInvertedIndexToFile(invertedIndex);

        } catch (FileNotFoundException e) {

            System.out.println("File not found");
            e.printStackTrace();
        }
    }


    private static void writeInvertedIndexToFile(TreeMap<String, PostingLists> invertedIndex) {

        PrintWriter invertedIndexWriter = null;

        try {
            invertedIndexWriter = new PrintWriter("./inverted_index_file.txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        for(String term : invertedIndex.keySet()) {

            PostingLists postingLists = invertedIndex.get(term);
            int docFrequency = postingLists.getDocFrequency();
            invertedIndexWriter.write("[" + term + ":" + docFrequency + "]\n");

            Map<Integer, PositionLists> positionMap = postingLists.getPositionMap();

            for(Integer docId : positionMap.keySet()) {

                PositionLists positionLists = positionMap.get(docId);
                invertedIndexWriter.write("->" + docId + "," + positionLists.getTermFrequency() + ":[" );
                List<Integer> positionListLists = positionLists.getPositions();

                StringBuffer str = new StringBuffer();


                for(Integer num : positionLists.getPositions()) {
                    str.append(num + ",");
                }

                str.deleteCharAt(str.length()-1);
                invertedIndexWriter.write(str.toString());

                invertedIndexWriter.println("],");
            }
            invertedIndexWriter.println("\n");

        }

        invertedIndexWriter.close();

    }
}
