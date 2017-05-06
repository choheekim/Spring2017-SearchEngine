package com.chohee.query_evaluation_part2;

/**
 * ProximityOperator class contains n, term1, and term2. For a query that is in form of n(term1, term2).
 */
public class ProximityOperator {

    private int n;
    private String term1;
    private String term2;

    public ProximityOperator(int n, String term1, String term2) {
        this.n = n;
        this.term1 = term1;
        this.term2 = term2;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String getTerm1() {
        return term1;
    }

    public void setTerm1(String term1) {
        this.term1 = term1;
    }

    public String getTerm2() {
        return term2;
    }

    public void setTerm2(String term2) {
        this.term2 = term2;
    }
}
