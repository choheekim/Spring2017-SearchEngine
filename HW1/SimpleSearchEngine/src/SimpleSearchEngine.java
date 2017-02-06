import java.io.*;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Created by Chohee on 1/27/17.
 */
public class SimpleSearchEngine {

    public static Scanner sc;


    public static void main(String[] args) {


        SimpleSearchEngine simpleSearchEngine = new SimpleSearchEngine();

        String fileName = args[0];

        //open .txt
        try {

            sc = new Scanner(new File(fileName));

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }


        TreeMap<Integer, String> myDocuments = new Parser(sc).getMyDocumentTreeSet();
        TreeMap<String, Value> invertedIndexList = new Indexer(myDocuments).getInvertedIndexList();


        simpleSearchEngine.writeInvertedIndexListToFile(invertedIndexList);
        simpleSearchEngine.runSearch(invertedIndexList, myDocuments);



    }

    /**
     * Create and display on console the result_file that contains query-terms, search results, and text of documents.
     * @param invertedIndexList
     * @param myDocuments
     */
    private void runSearch(TreeMap<String, Value> invertedIndexList, TreeMap<Integer, String> myDocuments) {

        PrintWriter resultWriter = null;

        try{

            resultWriter = new PrintWriter("./result_file.txt", "UTF-8");

            System.out.println("Type '*exit' to end the search");
            System.out.println("Your search term should be in a form of - Term1 AND Term2.");

            Scanner reader = new Scanner(System.in);
            String searchTerm = "";

            Query result = new Query();
            result.setInvertedIndexList(invertedIndexList);



            while (true) {

                int matches = 0;
                String[] terms;

                if(searchTerm.equalsIgnoreCase("*exit")) break;


                while(true) {
                    System.out.print("Search>> ");


                    searchTerm = reader.nextLine();
                    terms = searchTerm.split(" ");

                    if(isRightFrom(terms)) break;

                    System.out.println("Your search term should be in a form of - Term1 AND Term2.");
                }

                resultWriter.write("Term you searched : " + searchTerm);
                resultWriter.println();

                result.setSearchTerm(terms);

                resultWriter.print("Result : ");

                for(Integer docId : result.runQuery()) {
                    System.out.print("Doc " + docId + " ");
                    resultWriter.print("Doc " + docId + " ");

                    matches++;
                }

                resultWriter.println();

                for(Integer docId : result.runQuery()) {
                    resultWriter.print("<Doc " + docId + ">" + "\n" + myDocuments.get(docId) + "\n");
                }


                if(matches == 0) {
                    resultWriter.print("No document(s) matched");
                    System.out.printf("No document(s) matched");
                }

                System.out.println();
                resultWriter.flush();

            }

            resultWriter.close();



        } catch (IOException e) {
            // do something
            System.out.printf("Something happened....");
        }



    }


    /**
     * Check if the given terms are in a form of 'TERM1 AND TERM2'
     * @param terms
     * @return
     */
    private boolean isRightFrom(String[] terms) {

        if(terms.length != 3 || !terms[1].equals("AND")) return false;

        return true;
    }


    /**
     * Create a file that contains Term, dFreq(Document Frequency), and postings list.
     * @param invertedIndexList
     */
    private void writeInvertedIndexListToFile(TreeMap<String, Value> invertedIndexList) {

        PrintWriter invertedIndexWriter = null;

        try {
            invertedIndexWriter = new PrintWriter("./inverted_index_file.txt", "UTF-8");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        int cnt = 1;

        for(String key : invertedIndexList.keySet()) {


            Value currentValue = invertedIndexList.get(key);

            invertedIndexWriter.write("------------------------------------------\n" +
                    "#" + (cnt++) + ". " +key + "\nDocument frequency : " + currentValue.getdFreq() + "\n| ");



            for(Integer docId : currentValue.getPostingList()) {
                invertedIndexWriter.write("Document " + docId + " | ");
            }

            invertedIndexWriter.write("\n");


        }

        invertedIndexWriter.close();

    }

}
