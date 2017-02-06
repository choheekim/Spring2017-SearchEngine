import java.util.List;

/**
 * Created by Chohee on 1/30/17.
 */

/**
 * Value contains dFreq ( Document frequency and postingsList )
 */
public class Value {

    private int dFreq;
    private List<Integer> postingList;

    public Value(int dFreq , List<Integer> postingList) {

        this.dFreq = dFreq;
        this.postingList = postingList;
    }

    public int getdFreq() { return this.dFreq; }

    public List<Integer> getPostingList() { return this.postingList; }

    public void setdFreq(int dFreq) { this.dFreq = dFreq; }

    public void setPostingLise(List<Integer> postingList) { this.postingList = postingList;}

}
