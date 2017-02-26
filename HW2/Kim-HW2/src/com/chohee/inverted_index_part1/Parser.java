package com.chohee.inverted_index_part1;

import java.util.Scanner;
import java.util.TreeMap;

/**
 * Created by Chohee on 2/1/17.
 */

/**
 *
 */
public class Parser {

    private Scanner textReader;
    private TreeMap<Integer, String> myDocumentTreeMap; //docId -> text


    public Parser(Scanner textReader) {
        this.textReader = textReader;
        myDocumentTreeMap = new TreeMap<>();

    }

    /**
     * Return true when the given word indicates the end of the document, otherwise false.
     * @param token
     * @return
     */
    private boolean isEndOfDoc(String token) {

        return token.startsWith("</");

    }

    /**
     * Return true when given word is the word that indicates start of the document, otherwise false.
     * @param token
     * @return true or false
     */
    private boolean isStartOfDoc(String token) {

        return token.startsWith("<") && !token.startsWith("</");
    }


    /**
     * Given a <DOC #> form, return just the #.
     * @param line
     * @return docID
     */
    private Integer getDocId(String line) {

        String[] docTerm = line.replace("<", "")
                .replace(">", "")
                .split(" ");

        return Integer.parseInt(docTerm[1]);
    }


    /**
     * Given scanner, read through the document and return the data structure that maps from its docId (document id) to text.
     * @return
     */
    public TreeMap<Integer, String> getMyDocumentTreeSet() {


        while(textReader.hasNextLine()) {

            StringBuffer text = new StringBuffer();
            String line = textReader.nextLine();

            //when the line indicates the starting of the document.
            if(isStartOfDoc(line)) {

                Integer docId = getDocId(line);

                //this while loop will be looping until it files the end of the doc
                while(true) {

                    line = textReader.nextLine();

                    if(isEndOfDoc(line))
                        break;

                    //new line doesn't have to be contained
                    if(line.equals(""))
                        continue;

                    text.append(line + "\n");

                }

                myDocumentTreeMap.put(docId,  text.toString());
            }
        }

        return myDocumentTreeMap;
    }



}
