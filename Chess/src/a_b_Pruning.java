import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class a_b_Pruning {
	private World minimax_World;
	//private LinkedList<World> list_Boards;
	private LinkedList<Integer> whitePoints ;
	private int counterW = 0;
	private LinkedList<Integer> blackPoints ;
	private int counterB = 0;
	
	public a_b_Pruning(String[][] currentboard){
		this.minimax_World = new World();
		 minimax_World.setBoard(currentboard); 
		 this.whitePoints = new LinkedList<>();
		 whitePoints.add(0);
		 this.blackPoints = new LinkedList<>();
		 blackPoints.add(0);
	}
	
	public String minimax_function(ArrayList<String> availableMoves, int depth, int maximizingPlayer) {		
		if(maximizingPlayer==1){//black nodes
			minimax_World.separate_Moves_incr(availableMoves);
			
			int val = min_Value(minimax_World, depth, -1000, +1000);
			
			while (!minimax_World.queueBlack.isEmpty())
			{
				Node_incr h1 = minimax_World.queueBlack.poll();
				if( h1.value== val){
					
					while (!h1.getIncr_priorQ().isEmpty())
					{	
						Node_incr incr = h1.getIncr_priorQ().poll();
						if(incr.points==val) {
							int pos_row = h1.row_ofBoard;
							int pos_col = h1.col_ofBoard;
							int nextMoveint_row=incr.row_ofBoard;
							int nextMoveint_col=incr.col_ofBoard;
							String nextMoveStr1 = Integer.toString(pos_row)+ Integer.toString(pos_col)+Integer.toString(nextMoveint_row)+ Integer.toString(nextMoveint_col);
							
							return nextMoveStr1;
						}
					}
				}
			}
			return null;
		}
		else{  //white nodes
			minimax_World.separate_Moves_decr(availableMoves);
			
			int val2 = max_Value(minimax_World, depth, -1000, +1000);
			
			while (!minimax_World.queueWhite.isEmpty())
			{
				Node_decr h2 = minimax_World.queueWhite.poll();
				if( h2.value== val2){
					
					while (!h2.getDecr_priorQ().isEmpty())
					{
						
						Node_decr decr = h2.getDecr_priorQ().poll();
						if(decr.points==val2) {
							int pos_row = h2.row_ofBoard;
							int pos_col = h2.col_ofBoard;
							int nextMoveint_row=decr.row_ofBoard;
							int nextMoveint_col=decr.col_ofBoard;
							String nextMoveStr2 = Integer.toString(pos_row)+ Integer.toString(pos_col)+Integer.toString(nextMoveint_row)+ Integer.toString(nextMoveint_col);
							
							return nextMoveStr2;
						}
					}
				}
			}
			return null;
		}
	}

	public int min_Value(World world, int depth, int alpha, int beta){ //if myColor = 1 I am the black player 
		if (depth==0 || world.checkEnd()==true) {
			//System.out.println("black CheckEnd  depth, world.checkEnd() : " +depth +world.checkEnd());
			return 0;
		}
		World newWorld = new World();
		newWorld.setBoard(world.getBoard());
		newWorld.blackMoves();
		
		if (newWorld.getAvailableMoves().size()==0 ) {
			//System.out.println("black Check end no more moves ");
			return 0;
		}
		int minEval = 1000;	
		int eval=0;
		counterB++;
		
		//newWorld.printAvailableMoves(newWorld.getAvailableMoves());
		newWorld.separate_Moves_incr(newWorld.getAvailableMoves());
		PriorityQueue<Node_incr> queueB = new PriorityQueue<Node_incr>( new Node_incr() );
		for(Node_incr nd : newWorld.queueBlack) {
			queueB.add(nd);	
		}
		while(!newWorld.queueBlack.isEmpty()) {
			newWorld.queueBlack.remove();
		}
		
		while(!queueB.isEmpty())
		{
		    Node_incr nd_incr = new Node_incr(0,0);
		    nd_incr = queueB.poll(); 
		    Node_incr helper_nd_d = new Node_incr(nd_incr.row_ofBoard, nd_incr.col_ofBoard);
		    nd_incr.value = +1000;
		    helper_nd_d.value = +1000;
		    
		    PriorityQueue<Node_incr> nd_incr_PriorQ = new PriorityQueue<Node_incr>(new Node_incr() );	
		    
		    for(Node_incr nd :  nd_incr.getIncr_priorQ()) {
		    	nd_incr_PriorQ.add(nd);	
			}
		    while (!nd_incr_PriorQ.isEmpty())
			{
				//nd_incr.getIncr_priorQ().poll();
				Node_incr child = new Node_incr(0,0);
				child = nd_incr_PriorQ.poll();
				int new_point = newWorld.calc_points(1, child.row_ofBoard, child.col_ofBoard);
				String prev_pawn1 = newWorld.find_the_pawn(1, child.row_ofBoard, child.col_ofBoard);
				String prev_pawn2 = newWorld.find_the_pawn(0, nd_incr.row_ofBoard, nd_incr.col_ofBoard);
				
				//make move
				newWorld.makeMove(nd_incr.row_ofBoard, nd_incr.col_ofBoard,child.row_ofBoard, child.col_ofBoard, 0, 0);
				this.blackPoints.add(new_point+blackPoints.get(counterB-1));
				child.points = new_point + blackPoints.get(counterB-1); 				
				
				eval = max_Value(newWorld, depth-1, alpha, beta);
				//unmake move
				newWorld.makeMoveBack(1,child.row_ofBoard, child.col_ofBoard,nd_incr.row_ofBoard, nd_incr.col_ofBoard, prev_pawn1, prev_pawn2);
				
				
				child.value = eval;
				nd_incr.value = min(nd_incr.value,child.points);
				helper_nd_d.value = nd_incr.value;
				helper_nd_d.getIncr_priorQ().add(child);
				beta = min(beta, eval);
				if (beta<= alpha){
					break;
				}
			}
			newWorld.queueBlack.add(helper_nd_d);
			minEval = min(minEval, nd_incr.value);
		}
		while(!newWorld.queueBlack.isEmpty()) {
			world.queueBlack.add(newWorld.queueBlack.poll());
		}
		
		return minEval;			
	}
		
	public int max_Value(World world, int depth, int alpha, int beta){//if myColor = 0 I am the white player 
		if (depth==0 || world.checkEnd()==true) {
			System.out.println("white CheckEnd depth,world.checkEnd() : " + depth+ world.checkEnd());
			return 0;
		}
		World newWorld = new World();
		newWorld.setBoard(world.getBoard()) ;
		newWorld.whiteMoves();
		
		if (newWorld.getAvailableMoves().size()==0 ) {
			System.out.println("white Check end no more moves");
			return 0;
		}
		int maxEval = -1000;
		int eval=0;
		counterW++;
		
		//newWorld.printAvailableMoves(newWorld.getAvailableMoves());
		newWorld.separate_Moves_decr(newWorld.getAvailableMoves());
		PriorityQueue<Node_decr> queueW = new PriorityQueue<Node_decr>(new Node_decr());
		for(Node_decr nd : newWorld.queueWhite) {
			queueW.add(nd);	
		}
		while(!newWorld.queueWhite.isEmpty()) {
			newWorld.queueWhite.remove();
		}
		while(!queueW.isEmpty())
		{
			System.out.println("size of queueW"+queueW.size());
			
			System.out.println("size of newWorld.queueWhite 111"+newWorld.queueWhite.size());
		    Node_decr nd_decr = new Node_decr(0,0);
		    nd_decr = queueW.poll(); 
		    System.out.println("after size of queue"+nd_decr.row_ofBoard+ nd_decr.col_ofBoard);
			Node_decr helper_nd_d = new Node_decr(nd_decr.row_ofBoard, nd_decr.col_ofBoard);
		    nd_decr.value = -1000;
		    helper_nd_d.value = -1000;
		    System.out.println("white nd_decr points" +nd_decr.points);
		    PriorityQueue<Node_decr> nd_decr_PriorQ = new PriorityQueue<Node_decr>( new Node_decr() );
		    for(Node_decr nd2 : nd_decr.getDecr_priorQ()) {
		    	nd_decr_PriorQ.add(nd2);	
			}
		   
		   // int size_nd_decr_PQ = nd_decr_PriorQ.size();
		    while (!nd_decr_PriorQ.isEmpty())
			{
		    	System.out.println("white nd_decr" +nd_decr_PriorQ.peek().row_ofBoard +"/"+nd_decr_PriorQ.peek().col_ofBoard);
		    	//System.out.println("SIZE OF CURRENT ND_DECR_PQUEUE"+ nd_decr_PriorQ.size());
				//nd_decr.getDecr_priorQ().poll();
				Node_decr child = new Node_decr(0,0);
				child = nd_decr_PriorQ.poll();
				int new_point = newWorld.calc_points(0, child.row_ofBoard, child.col_ofBoard);
				String prev_pawn1 = newWorld.find_the_pawn( 0,child.row_ofBoard, child.col_ofBoard);
				String prev_pawn2 = newWorld.find_the_pawn( 1,nd_decr.row_ofBoard, nd_decr.col_ofBoard);
				System.out.println("HEEEEEEEEEEEEEEEEEEEEEEEEY 22222222222222white make move " +nd_decr.row_ofBoard + nd_decr.col_ofBoard+child.row_ofBoard+ child.col_ofBoard);
				//make move
				newWorld.makeMove(nd_decr.row_ofBoard, nd_decr.col_ofBoard,child.row_ofBoard, child.col_ofBoard, 0, 0);
				//System.out.println(counterW-1+" "+this.whitePoints.get(counterW-1)+" "+new_point);			
				this.whitePoints.add(new_point+ whitePoints.get(counterW-1));
				//System.out.println("white whitePoints, counterW : " +whitePoints.get(counterW)+counterW);
				child.points = new_point + whitePoints.get(counterW-1); 
				
				eval = min_Value(newWorld, depth-1, alpha, beta);
				//move back
				System.out.println("HEeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeEEY 2222222white make back " +nd_decr.row_ofBoard + nd_decr.col_ofBoard+child.row_ofBoard+ child.col_ofBoard);
				newWorld.makeMoveBack(0, child.row_ofBoard, child.col_ofBoard,nd_decr.row_ofBoard, nd_decr.col_ofBoard, prev_pawn1, prev_pawn2);

				//maxEval = max(maxEval, eval);
				System.out.println("yeeeeeeeeeeees");
				child.value = eval;///////////////////////////////////
				nd_decr.value = max(nd_decr.value,child.points);//maxEval
				helper_nd_d.value = nd_decr.value;
				helper_nd_d.getDecr_priorQ().add(child);
				alpha = max(alpha, eval);
				if (beta<= alpha){
					break;
				}	
			}
		    System.out.println("size of newWorld.queueWhite 222"+newWorld.queueWhite.size());
			newWorld.queueWhite.add(helper_nd_d);
			maxEval =max(maxEval, nd_decr.value);
		}
		while(!newWorld.queueWhite.isEmpty()) {
			world.queueWhite.add(newWorld.queueWhite.poll());
		}
		//world.queueWhite = newWorld.queueWhite;
		System.out.println(" maxEval : "+maxEval);
		return maxEval;		// max value of the nd_decr nodes
	}

	

/*	private int calc_points(World newWorld, int row_ofBoard, int col_ofBoard) {
		if(newWorld.getBoard()[row_ofBoard][col_ofBoard]=="BR") {
			return 3;
		}
		else if(newWorld.getBoard()[row_ofBoard][col_ofBoard]=="BK") {
			return 8;
		}
		else if(newWorld.getBoard()[row_ofBoard][col_ofBoard]=="BP") {
			return 1;
		}
		else if(newWorld.getBoard()[row_ofBoard][col_ofBoard]=="P") {
			return 1;
		}
		else {
			return 0;
		}
		
		
	}
*/	
	public int min(int a, int b){
		if (a<b){
			return a;
		}
		return b;
	}
	
	public int max(int a, int b){
		if (a>b){
			return a;
		}
		return b;
	}
	
//	public class Tree{
//  public Node root;
//
//  public Tree(Node nd) {
//      this.root = new Node(nd.row_ofBoard, nd.col_ofBoard, nd.cost);
//      //root.childrens = new ArrayList<Node>();
//  }
//}
	
	
//	public static void increaseScore(int player, int points)
//	{
//		// 0 == the white player, 1 == black player
//		if(player == 0)
//			scoreWhite += points;
//		else
//			scoreBlack += points;
//	}
	
}
