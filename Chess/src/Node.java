import java.util.Comparator;

class Node implements Comparator<Node>{	    
    public int row_ofBoard;
    public int col_ofBoard;
    public int points;
    public int value;
    private Node parent;
    //public List<Node> childrens;
    
    public Node(int row_ofBoard, int col_ofBoard){
        this.row_ofBoard = row_ofBoard;
        this.col_ofBoard = col_ofBoard;
        this.points = 0;
        this.value = 0;
        //this.childrens = new LinkedList<Node>();
    }
    
	@Override
    public int compare(Node node1, Node node2){
        if (node1.points < node2.points)
            return -1;
        if (node1.points > node2.points)
            return 1;
        return 0;
    }
 
    @Override
    public boolean equals(Object obj){
        if (obj instanceof Node){
            Node node = (Node) obj;
            if (this.row_ofBoard == node.row_ofBoard && this.col_ofBoard == node.col_ofBoard){
                return true;
            }
        }
        return false;
    }
    
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
}