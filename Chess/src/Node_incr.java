//import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

//for black moves 
public class Node_incr implements Comparator<Node_incr> {
	public int row_ofBoard;
    public int col_ofBoard;
    public int points;
    public int value;
    private Node_incr parent;
	private PriorityQueue<Node_incr> incr_priorQ;
	
	public Node_incr(int row_ofBoard, int col_ofBoard){
		this.row_ofBoard = row_ofBoard;
        this.col_ofBoard = col_ofBoard;
        this.points = 0;
        this.value = 0;
		//this.incr_priorQ = new PriorityQueue<Node_incr>(Collections.reverseOrder());
        this.incr_priorQ = new PriorityQueue<Node_incr>(new Node_incr());
	}
	
	public Node_incr() {
	}

	@Override
	public int compare(Node_incr node1, Node_incr node2){
        if (node1.points > node2.points)
            return -1;
        if (node1.points < node2.points)
            return 1;
        return 0;
    }
 
    @Override
    public boolean equals(Object obj){
        if (obj instanceof Node_incr){
        	Node_incr node = (Node_incr) obj;
            if (this.row_ofBoard == node.row_ofBoard && this.col_ofBoard == node.col_ofBoard){
                return true;
            }
        }
        return false;
    }
    
	public Node_incr getParent() {
		return parent;
	}
	
	public void setParent(Node_incr nd) {
		this.parent = nd;
	}

	public PriorityQueue<Node_incr> getIncr_priorQ() {
		return incr_priorQ;
	}
	public void setIncr_priorQ(PriorityQueue<Node_incr> incr_priorQ) {
		this.incr_priorQ = incr_priorQ;
	}
}