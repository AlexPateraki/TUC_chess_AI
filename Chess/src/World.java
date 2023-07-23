import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;

public class World
{
	private String[][] board = null;
	public int rows = 7;
	public int columns = 5;
	private int myColor = 0;
	private ArrayList<String> availableMoves = null;
	private int rookBlocks = 3;		// rook can move towards <rookBlocks> blocks in any vertical or horizontal direction
	private int nTurns = 0;
	private int nBranches = 0;
	private int noPrize = 9;
	private long startingTime;
	private double timeLimit = 6.0;
	private int dipth=5 ;
	private int[] helper_move;
	public PriorityQueue<Node_decr> queueWhite;
	public PriorityQueue<Node_incr> queueBlack;
	private int begin=0;
	private int number;
	
//	public int run =0;// total runs of current rollout
//	public int wins =0;//wins of current rollout
//	int depthOfTree=0;//in which depth of tree is this current rollout
//	
//	//count the leftmost unvisited index of a node to propagate afterwards
//	private int leftmostUnvisited=0;
//	//setting children and tree
//	public Tree tree;
//	public LinkedList<Node> children=new LinkedList<>();
//	//the current node of the tree
//	public Node current;
//	//a variable ned to help for finding best uct
//	public int col=-1;
//	//a list of visited nodes to update t, n, uct
//	public LinkedList<Node> visited=new LinkedList<>();
//	//list to find the max uct
//	public LinkedList<Node> visitedA=new LinkedList<>();
	
	public World(long startingTime)
	{
		this.startingTime = startingTime;
		this.queueBlack = new PriorityQueue<Node_incr>( new Node_incr() );
		this.queueWhite = new PriorityQueue<Node_decr>( new Node_decr() );
		this.helper_move = new int[4];  
		board = new String[rows][columns];
		
		/* represent the board
		
		BP|BR|BK|BR|BP
		BP|BP|BP|BP|BP
		--|--|--|--|--
		P |P |P |P |P 
		--|--|--|--|--
		WP|WP|WP|WP|WP
		WP|WR|WK|WR|WP
		*/
		
		// initialization of the board
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				board[i][j] = " ";
		
		// setting the black player's chess parts
		
		// black pawns
		for(int j=0; j<columns; j++)
			board[1][j] = "BP";
		
		board[0][0] = "BP";
		board[0][columns-1] = "BP";
		
		// black rooks
		board[0][1] = "BR";
		board[0][columns-2] = "BR";
		
		// black king
		board[0][columns/2] = "BK";
		
		// setting the white player's chess parts
		
		// white pawns
		for(int j=0; j<columns; j++)
			board[rows-2][j] = "WP";
		
		board[rows-1][0] = "WP";
		board[rows-1][columns-1] = "WP";
		
		// white rooks
		board[rows-1][1] = "WR";
		board[rows-1][columns-2] = "WR";
		
		// white king
		board[rows-1][columns/2] = "WK";
		
		// setting the prizes
		for(int j=0; j<columns; j++)
			board[rows/2][j] = "P";
		
		availableMoves = new ArrayList<String>();
	}
	public World()
	{
		this.queueBlack = new PriorityQueue<Node_incr>(new Node_incr());
		this.queueWhite = new PriorityQueue<Node_decr>(new Node_decr() );
		this.helper_move = new int[4]; 
		board = new String[rows][columns];
		
		/* represent the board
		
		BP|BR|BK|BR|BP
		BP|BP|BP|BP|BP
		--|--|--|--|--
		P |P |P |P |P 
		--|--|--|--|--
		WP|WP|WP|WP|WP
		WP|WR|WK|WR|WP
		*/
		
		// initialization of the board
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				board[i][j] = " ";
		
		// setting the black player's chess parts
		
		// black pawns
		for(int j=0; j<columns; j++)
			board[1][j] = "BP";
		
		board[0][0] = "BP";
		board[0][columns-1] = "BP";
		
		// black rooks
		board[0][1] = "BR";
		board[0][columns-2] = "BR";
		
		// black king
		board[0][columns/2] = "BK";
		
		// setting the white player's chess parts
		
		// white pawns
		for(int j=0; j<columns; j++)
			board[rows-2][j] = "WP";
		
		board[rows-1][0] = "WP";
		board[rows-1][columns-1] = "WP";
		
		// white rooks
		board[rows-1][1] = "WR";
		board[rows-1][columns-2] = "WR";
		
		// white king
		board[rows-1][columns/2] = "WK";
		
		// setting the prizes
		for(int j=0; j<columns; j++)
			board[rows/2][j] = "P";
		
		availableMoves = new ArrayList<String>();
	}
	
	public void setMyColor(int myColor)
	{
		this.myColor = myColor;
	}
	
	
	
	public String[][] getBoard() {
		return board;
	}

	public void setBoard(String[][] board) {
		this.board = board;
	}

	public ArrayList<String> getAvailableMoves() {
		return availableMoves;
	}

	public void setAvailableMoves(ArrayList<String> availableMoves) {
		this.availableMoves = availableMoves;
	}

	public int getMyColor() {
		return myColor;
	}
	public void whiteMoves()
	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
				
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a white chess part in this position then keep on searching
				if(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i-1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-1) + Integer.toString(j);
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j-1].charAt(0));						
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i-1) + Integer.toString(j-1);
								
							availableMoves.add(move);
						}											
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=0)
					{
						firstLetter = Character.toString(board[i-1][j+1].charAt(0));
						if(!(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i-1) + Integer.toString(j+1);							
							availableMoves.add(move);
						}
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("W"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("B") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("W"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							availableMoves.add(move);	
						}
					}
				}			
			}	
		}
	}

	
	public void blackMoves()
	{
		String firstLetter = "";
		String secondLetter = "";
		String move = "";
				
		for(int i=0; i<rows; i++)
		{
			for(int j=0; j<columns; j++)
			{
				firstLetter = Character.toString(board[i][j].charAt(0));
				
				// if it there is not a black chess part in this position then keep on searching
				if(firstLetter.equals("W") || firstLetter.equals(" ") || firstLetter.equals("P"))
					continue;
				
				// check the kind of the white chess part
				secondLetter = Character.toString(board[i][j].charAt(1));
				
				if(secondLetter.equals("P"))	// it is a pawn
				{
					
					// check if it can move one vertical position ahead
					firstLetter = Character.toString(board[i+1][j].charAt(0));
					
					if(firstLetter.equals(" ") || firstLetter.equals("P"))
					{
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+1) + Integer.toString(j);
						
						availableMoves.add(move);
					}
					
					// check if it can move crosswise to the left
					if(j!=0 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j-1].charAt(0));
						
						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i+1) + Integer.toString(j-1);
								
							availableMoves.add(move);
						}																	
					}
					
					// check if it can move crosswise to the right
					if(j!=columns-1 && i!=rows-1)
					{
						firstLetter = Character.toString(board[i+1][j+1].charAt(0));
						
						if(!(firstLetter.equals("B") || firstLetter.equals(" ") || firstLetter.equals("P"))) {
							move = Integer.toString(i) + Integer.toString(j) + 
									   Integer.toString(i+1) + Integer.toString(j+1);
								
							availableMoves.add(move);
						}
							
						
						
					}
				}
				else if(secondLetter.equals("R"))	// it is a rook
				{
					// check if it can move upwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i-(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i-(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move downwards
					for(int k=0; k<rookBlocks; k++)
					{
						if((i+(k+1)) == rows)
							break;
						
						firstLetter = Character.toString(board[i+(k+1)][j].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i+(k+1)) + Integer.toString(j);
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check if it can move on the left
					for(int k=0; k<rookBlocks; k++)
					{
						if((j-(k+1)) < 0)
							break;
						
						firstLetter = Character.toString(board[i][j-(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j-(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
					
					// check of it can move on the right
					for(int k=0; k<rookBlocks; k++)
					{
						if((j+(k+1)) == columns)
							break;
						
						firstLetter = Character.toString(board[i][j+(k+1)].charAt(0));
						
						if(firstLetter.equals("B"))
							break;
						
						move = Integer.toString(i) + Integer.toString(j) + 
							   Integer.toString(i) + Integer.toString(j+(k+1));
						
						availableMoves.add(move);
						
						// prevent detouring a chesspart to attack the other
						if(firstLetter.equals("W") || firstLetter.equals("P"))
							break;
					}
				}
				else // it is the king
				{
					// check if it can move upwards
					if((i-1) >= 0)
					{
						firstLetter = Character.toString(board[i-1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i-1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move downwards
					if((i+1) < rows)
					{
						firstLetter = Character.toString(board[i+1][j].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i+1) + Integer.toString(j);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the left
					if((j-1) >= 0)
					{
						firstLetter = Character.toString(board[i][j-1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j-1);
								
							availableMoves.add(move);	
						}
					}
					
					// check if it can move on the right
					if((j+1) < columns)
					{
						firstLetter = Character.toString(board[i][j+1].charAt(0));
						
						if(!firstLetter.equals("B"))
						{
							move = Integer.toString(i) + Integer.toString(j) + 
								   Integer.toString(i) + Integer.toString(j+1);
								
							availableMoves.add(move);	
						}
					}
				}			
			}	
		}
	}
	
	public String selectAction()
	{
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		availableMoves = new ArrayList<String>();
				
		if(myColor == 0)		// I am the white player
			this.whiteMoves();
		else					// I am the black player
			this.blackMoves();
		
		// keeping track of the branch factor
		nTurns++;
		nBranches += availableMoves.size();
		if(this.begin == 0) {
			System.out.println("Choose Algoritm");
			System.out.println("Press 1: MinimaxAlgorithm");
			System.out.println("Press 2: a_b_Pruning");
			//System.out.println("Press 3: MonteCarlo");
			System.out.println("Else   : RandomAction");
			this.number = input.nextInt();
			this.begin=1;
		}
		
		if(number==1) {
			return this.selectMinimaxAlgorithm();
		}
		if(number==2) {
			return this.selecta_b_Pruning();
		}
//		if(number==3) {
//			return select_MonteCarlo();
//		}
		else {
			return this.selectRandomAction();
		}
		
	}

	
	private String selectMinimaxAlgorithm()
	{	
		minimax minmax = new minimax(this.board);
		String best_action = minmax.minimax_function(availableMoves, dipth, myColor);
		
		return best_action;
	}
	private String selecta_b_Pruning()
	{	
		a_b_Pruning abPrun = new a_b_Pruning(this.board);
		String best_action = abPrun.minimax_function(availableMoves, dipth, myColor);
		
		return best_action;
	}
//	private String select_MonteCarlo(){
//		String action;
//		if (myColor==0) {
//			findMaxUCT();
//			//myColor=2;
//			action = makeTree( myColor, run,depthOfTree);
//			this.run=this.run+1;
//			return action;
//			
//		}
//		else { 
//			return action = selectAction();
//		}
//	}
	public String selectAction2()
	{
		availableMoves = new ArrayList<String>();
				
		if(myColor == 0)		// I am the white player
			this.whiteMoves();
		else					// I am the black player
			this.blackMoves();
		
		// keeping track of the branch factor
		nTurns++;
		nBranches += availableMoves.size();
		
		return this.selectRandomAction();
	}
	private String selectRandomAction()
	{		
		Random ran = new Random();
		int x = ran.nextInt(availableMoves.size());
		
		return availableMoves.get(x);
	}
	
	public boolean checkEnd()
	{
		int count_kings = 0;
		int chess_parts = 0;
		for (int x=0; x<rows; x++)
		{
			for(int y=0; y<columns; y++)
			{
				if (this.board[x][y]=="BK" || this.board[x][y]=="WK"){
					count_kings = count_kings+1;
				}
				if (this.board[x][y]!="BK" && this.board[x][y]!="WK" && this.board[x][y]!=" "){
					chess_parts = chess_parts +1;
				}
			}
		}
		// if there is only one king in the game or two chessparts (=the two kings) the game has ended
		if(count_kings==1 || chess_parts==0){
			return true;
		}
		
		// check time limit
		long elapsedTime = System.nanoTime() - this.startingTime;
		double minutes = (double)elapsedTime / (10000000000000.0 * 60);
		if(minutes > this.timeLimit){
			System.out.println("minutes END: " +minutes);
			return true;
		}		
		return false;
	}

	
	public double getAvgBFactor()
	{
		return nBranches / (double) nTurns;
	}
	
	// update the board every time a move of the opponent has been made 
	// or a prize has been added in the game
	public void makeMove(int x1, int y1, int x2, int y2, int prizeX, int prizeY)
	{
		//String chesspart = Character.toString(board[x1][y1].charAt(1));
		String chesspart = board[x1][y1];
		boolean pawnLastRow = false;
		
		// check if it is a move that has made a move to the last line
		if(chesspart.equals("P"))
			if( (x1==rows-2 && x2==rows-1) || (x1==1 && x2==0) )
			{
				board[x2][y2] = " ";	// in a case an opponent's chess part has just been captured
				board[x1][y1] = " ";
				pawnLastRow = true;
			}
		
		// otherwise
		if(!pawnLastRow)
		{
			board[x2][y2] = board[x1][y1];
			board[x1][y1] = " ";
		}
		
		// check if a prize has been added in the game
		if(prizeX != noPrize)
			board[prizeX][prizeY] = "P";
	}
	
	// black player
	//return a reversedOrder queue(increasing order) of the available positions(Node_incr)
	//of the black player that have different moves...these moves(every Node_incr) of each position(of each Node_incr) 
	//are saved in increasing order in the priority queue(of increasing order) of each position
	public  void separate_Moves_incr(ArrayList<String> availableMoves) {
		LinkedList<Node_incr> Nodes_Moves = new LinkedList<>();
		
		for(int k1=0; k1<availableMoves.size(); k1++)
		{
			boolean permit = true;//permission to add to the queueBlack 
			String str = availableMoves.get(k1);
			getIntegersof_availMove(str);
			Node_incr current_position = new Node_incr(this.helper_move[0], this.helper_move[1]);
			for(Node_incr nd :Nodes_Moves)
			{	
				if( nd.equals(current_position) ){
					permit = false;
					Node_incr child_node_ofCurrent = new Node_incr( this.helper_move[2], this.helper_move[3]);
					int new_point = calc_points(1,child_node_ofCurrent.row_ofBoard, child_node_ofCurrent.col_ofBoard);
					child_node_ofCurrent.points = new_point;
					child_node_ofCurrent.setParent(nd);
					nd.getIncr_priorQ().add(child_node_ofCurrent);
					break;
				}
			}
			if( permit == true ){			
				Node_incr child_node_ofCurrent = new Node_incr( this.helper_move[2], this.helper_move[3]);
				int new_point = calc_points(1,child_node_ofCurrent.row_ofBoard, child_node_ofCurrent.col_ofBoard);
				child_node_ofCurrent.points = new_point;
				child_node_ofCurrent.setParent(current_position);
				current_position.getIncr_priorQ().add(child_node_ofCurrent);	
				Nodes_Moves.add(current_position);
			}	
		}
		//System.out.println("separate available Moves");
		for (int i=0; i<Nodes_Moves.size(); i++)
		{
			//System.out.println(Nodes_Moves.get(i).row_ofBoard +" "+ Nodes_Moves.get(i).col_ofBoard+" "+Nodes_Moves.get(i).getIncr_priorQ().size());
			queueBlack.add(Nodes_Moves.get(i));
		}
	}
	
	// white player
	//return a queue(decreasing order) of the available positions(Node_decr)
	//of the white player that have different moves...these moves(every Node_decr) of each position(of each Node_decr) 
	//are saved in decreasing order in the priority queue(of decreasing order) of each position
	public  void separate_Moves_decr(ArrayList<String> availableMoves) {
		LinkedList<Node_decr> Nodes_Moves = new LinkedList<>();
		
		for(int k1=0; k1<availableMoves.size(); k1++)
		{
			boolean permit = true;//permission to add to the queueWhtite
			String str = availableMoves.get(k1);
			getIntegersof_availMove(str);
			Node_decr current_position = new Node_decr(helper_move[0], helper_move[1]);
			for(Node_decr nd :Nodes_Moves)
			{	
				if( nd.equals(current_position) ){
					permit = false;
					Node_decr child_node_ofCurrent = new Node_decr( helper_move[2], helper_move[3]);
					int new_point = calc_points(0,child_node_ofCurrent.row_ofBoard, child_node_ofCurrent.col_ofBoard);
					child_node_ofCurrent.points = new_point;
					child_node_ofCurrent.setParent(nd);
					nd.getDecr_priorQ().add(child_node_ofCurrent);
					break;
				}
			}
			if( permit == true ){			
				Node_decr child_node_ofCurrent = new Node_decr( helper_move[2], helper_move[3]);
				int new_point = calc_points(0,child_node_ofCurrent.row_ofBoard, child_node_ofCurrent.col_ofBoard);
				child_node_ofCurrent.points = new_point;
				child_node_ofCurrent.setParent(current_position);
				current_position.getDecr_priorQ().add(child_node_ofCurrent);
				Nodes_Moves.add(current_position);
			}	
		}
		//System.out.println("separate available Moves");
		for (int i=0; i<Nodes_Moves.size(); i++)
		{
			//System.out.println(Nodes_Moves.get(i));
			queueWhite.add(Nodes_Moves.get(i));
			
		}
		
		
	}
	
	public int calc_points(int player, int row_ofBoard, int col_ofBoard) {
		if(player==0){//white player
			if(this.getBoard()[row_ofBoard][col_ofBoard]=="BR") {
				return 3;
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="BK") {
				return 8;
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="BP") {
				return 1;
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="P") {
				return 1;
			}
			else if((row_ofBoard==0 && col_ofBoard==0)|| (row_ofBoard==0 && col_ofBoard==1)||(row_ofBoard==0 && col_ofBoard==2)||
					(row_ofBoard==0 && col_ofBoard==3) || (row_ofBoard==0 && col_ofBoard==4)) {
				return 1;
			}
			else {
				return 0;
			}	
		}
		else {
			if(this.getBoard()[row_ofBoard][col_ofBoard]=="WR") {
				return 3;
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="WK") {
				return 8;
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="WP") {
				return 1;
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="P") {
				return 1;
			}
			else if((row_ofBoard==6 && col_ofBoard==0)|| (row_ofBoard==6 && col_ofBoard==1)||(row_ofBoard==6 && col_ofBoard==2)||
					(row_ofBoard==6 && col_ofBoard==3) || (row_ofBoard==6 && col_ofBoard==4)) {
				return 1;
			}
			else {
				return 0;
			}		
		}
	}
	
	private void getIntegersof_availMove(String str){
		int integer = Integer.parseInt(str);
		helper_move[0] = integer/1000;
		helper_move[1]= (integer-helper_move[0]*1000)/100;
		helper_move[2] = (integer-helper_move[0]*1000-helper_move[1]*100)/10;
		helper_move[3] = (integer-helper_move[0]*1000-helper_move[1]*100-helper_move[2]*10);
		
	}

	public void printAvailableMoves(ArrayList<String> availableMoves) {
		System.out.println("availableMoves");
		for(int i=0; i<availableMoves.size(); i++)
		{
			System.out.println(availableMoves.get(i));
		}
	}
	public void makeMoveBack(int player, int row_ofBoard, int col_ofBoard, int row_ofBoard2, int col_ofBoard2, String pawn1, String pawn2) {
		String chesspart = board[row_ofBoard][col_ofBoard];
		
		//boolean pawnLastRow = false;
		
		// check if it is a move that has been made to the last line
		if(chesspart.equals("P"))
		{
			//System.out.println("yes it equals with"+chesspart+" "+pawn1+pawn2);
			board[row_ofBoard2][col_ofBoard2] = pawn2;	// in a case an opponent's chess part has just been captured
			board[row_ofBoard][col_ofBoard] = pawn1;
		}
		else
		{
			this.board[row_ofBoard2][col_ofBoard2] = this.board[row_ofBoard][col_ofBoard];
			this.board[row_ofBoard][col_ofBoard] = pawn1;
		}
		
		
	}
	public String find_the_pawn(int player, int row_ofBoard, int col_ofBoard) {
		if(player==0){//white player
			if(this.getBoard()[row_ofBoard][col_ofBoard]=="BR") {
				return "BR";
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="BK") {
				return "BK";
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="BP") {
				return "BP";
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="P") {
				return "P";
			}
			else {
				return " ";
			}	
		}
		else {
			if(this.getBoard()[row_ofBoard][col_ofBoard]=="WR") {
				return "WR";
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="WK") {
				return "WK";
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="WP") {
				return "WP";
			}
			else if(this.getBoard()[row_ofBoard][col_ofBoard]=="P") {
				return "P";
			}
			else {
				return " ";
			}		
		}
	}

//	
//	class Node {
//		String move;//name of the move in the chess for example 5040
//		int t;//total value of the node, we suppose wins of the game
//		double n;//number of visits of the node
//		double uct;//formula of uct
//		Node parent;//parent of the node
//		LinkedList<Node> children=new LinkedList<Node>();
//		
//
//		//empty constructor
//		public Node() {
//		}
//
//
//		//constructor to fill variables
//		public Node(String move,int t, double n, double uct, Node parent) {
//			this.move=move;
//			this.t = t;
//			this.n = n;
//			this.uct = uct;
//			this.parent = parent;
//		}
//
//
//		//method to count uct using the recommended C
//		public double countingUCT(Node node){
//			double C= 1/ Math.sqrt(2);// default from exercise
//			if ((node.n==0)||(node.parent==null))
//				return 0;
//			double uct=node.t/node.n+Math.sqrt((C+Math.log(node.parent.n))/n);
//			insertUct(node,uct);
//			return uct;
//		}
//		
//		//method to increase the visits of the node
//		public void increaseN(Node node) {
//			node.n++;
//		}
//		//method to save the uct if it has changed 
//		public void insertUct(Node node, double uct) {
//			node.uct=uct;
//		}
//		
//		
//	}
	
//	/**
//	 * 
//	 *a class to save the the tree using root which has children
//	 */
//	class Tree{
//		Node root;
//		
//		public Tree(Node root) {
//			this.root = root;
//		}
//		
//		
//	}
//	//method to make the tree, update n,t, uct of each node and 
//	//also in the occassion of myColor being 2 we suppose we found the best path and it returns 
//   // the best combination of nodes to win using mcts	
//	private String makeTree(int myColor, int run,int depthOfTree) {
//	    //we suppose we found the best path and it returns 
//		// the best combination of nodes to win using mcts
//		if (myColor==2) {
//			col++;
//			return visitedA.get(col).move;
//		}
//		//take a random action but not using it..just use the method to update availableMoves
//		String move=selectAction2();
//		//Initialize the tree
//		if (tree==null) {
//			Node root=new Node(move,0,1,0,null);
//			tree=new Tree(root);
//			current=root;
//			visited.add(root);
//			if (run==0) return move;
//		}
//		//second run
//		else if(run==1)
//			current=tree.root;
//		else//update the tree and the children of it
//		{	Node n = new Node();
//			if(current.children.get(leftmostUnvisited)!=null)
//				n=new Node(current.children.get(leftmostUnvisited).move,0,0,0,current);
//		current = new Node(current.children.get(leftmostUnvisited).move,0,0,0,current);
//		for (int i=0;i<tree.root.children.size();i++) {
//			if ((n.move!=null)&&(tree.root.children.get(i).move!=null)) {
//			if(n.move.equals(tree.root.children.get(i).move)) { 
//				current = tree.root.children.get(i);
//				break;
//				}
//			}	
//		}
//			
//		}
//			//finding the children of the node
//			for (int i=0;i<availableMoves.size();i++) {
//					//save all the children except from the root
//					Node child=new Node(availableMoves.get(i),0,0,current.countingUCT(current),current);
//					current.children.add(child);
//					}
//			
//			
//			//counting visits and uct of nodes and saving the nodes in a list
//			//of visited node
//			current.increaseN(current.children.get(leftmostUnvisited));
//			current.countingUCT(current.children.get(leftmostUnvisited));
//			for (int i=0; i<visited.size();i++) {
//				if (!visited.get(i).move.equals(current.children.get(leftmostUnvisited).move)) {
//					visited.add(current.children.get(leftmostUnvisited));
//
//				}
//			}
//		return current.children.get(leftmostUnvisited).move;//returning the leftmost node of the tree
//
//	}
//	
//	//method to find the path of the best node of max uct
//	public void findMaxUCT() {
//		double max=-1;
//		for (int i=0; i<visited.size();i++) {
//			if (visited.get(i)!=null)
//				if(visited.get(i).uct>max) {
//					max=visited.get(i).uct;		
//					visitedA.add(visited.get(i));
//					}
//				else visitedA.add(visited.get(i));
//
//			}
//	}
	
	
	
	
	
	
	
	
	
	
}
