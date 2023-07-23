import java.util.Comparator;
import java.util.PriorityQueue;

//for white nodes
public class Node_decr implements Comparator<Node_decr> {
	public int row_ofBoard;
    public int col_ofBoard;
    public int points;
    public int value;
    private Node_decr parent;
	private PriorityQueue<Node_decr> decr_priorQ;
	
	public Node_decr(int row_ofBoard, int col_ofBoard){
		this.row_ofBoard = row_ofBoard;
        this.col_ofBoard = col_ofBoard;
        this.points = 0;
        this.value = 0;
		this.decr_priorQ = new PriorityQueue<Node_decr>( new Node_decr() );
	}
	
    public Node_decr() {
	}

    @Override
	public int compare(Node_decr node1, Node_decr node2){
        if (node1.points < node2.points)
            return -1;
        if (node1.points > node2.points)
            return 1;
        return 0;
    }
 
    @Override
    public boolean equals(Object obj){
        if (obj instanceof Node_decr){
        	Node_decr node = (Node_decr) obj;
            if (this.row_ofBoard == node.row_ofBoard && this.col_ofBoard == node.col_ofBoard){
                return true;
            }
        }
        return false;
    }
    
	public Node_decr getParent() {
		return parent;
	}
	
	public void setParent(Node_decr current_position) {
		this.parent = current_position;
	}
	
	public PriorityQueue<Node_decr> getDecr_priorQ() {
		return decr_priorQ;
	}
	public void setDecr_priorQ(PriorityQueue<Node_decr> decr_priorQ) {
		this.decr_priorQ = decr_priorQ;
	}
}