import java.util.Scanner;


class Node{ 
	int board[][]=new int[Main.N][Main.N];
	Node children[];
	double value;
}

public class Main {
	static int MAX_DEPTH=4;
	static int N=8;
	static int EMPTY=0,HUMAN=1,PC=2;
	static Scanner sc=new Scanner(System.in);
	
	static void copyBoard(int[][] src, int[][] dest) {
        for (int i = 0; i < N; i++) {
           for(int j = 0; j < N; j++)
        	   dest[i][j]=src[i][j];
        }
    }
	
	static int transformChildNumber2Column(int board[][], int childNumber) {
        int count=-1;
		for(int i=0;i<N;i++)
        {
			if(board[0][i]==EMPTY)
        		count++;
		    if(count==childNumber)
		    	return i;
		 
        }
		return -1;
    }
	
	static int calculateNumChildren(int board[][]) {
		int numChildren = 0;
	    for (int i = 0; i < N; i++) {
	            if (board[0][i] == EMPTY) {
	                numChildren++;
	            }
	    }
	    return numChildren;
	}
	
	static double heuristic(int board[][],int token) {
		
		if(playerWon(board,HUMAN)) 
			return -1000;
		if(playerWon(board,PC))
			return 1000;
		return Math.random()*10; // return random value between 0,10

	}
	
	static Node createNode(Node parent, int childNumber,int level) {
		Node node= new Node();
		// Copy the board of its parent 
		copyBoard(parent.board,node.board);
	   // throw the token in the specified column 
		int column=transformChildNumber2Column(node.board,childNumber);
		int token;
		if(level %2 == 1)
			token=PC;
		else
			token=HUMAN;
		setToken(node.board,column,token);
	    double h=heuristic(node.board,token);
		 if (level<MAX_DEPTH && !playerWon(node.board, token) ) 
		 {
			 int n=calculateNumChildren(node.board);
	            node.children = new Node[n];
		 } 
		 else {
			 node.children = null;
			 ///score the node
			 node.value=h;
		 }
	       
	        return node;
	}
	
	static void createLevel(Node parent,int level) {
		for(int i=0;i<parent.children.length;i++)
			parent.children[i]=createNode(parent,i,level);
		
	}
	
	static void createOtherLevels(Node node,int current_depth) {
	    
		if(node.children!=null) {
		for (int i = 0; i < node.children.length; i++) {
			if(node.children[i].children!=null) {
				createLevel(node.children[i], current_depth +1 );
				createOtherLevels(node.children[i], current_depth +1 );
			}
	    	}
		}
	    
	}
	
	static void createTree(Node root)
	{
		createLevel(root, 1);
		//for(int i=0;i<root.children.length;i++)
		//{
		//	createLevel(root,2);
		//}
		createOtherLevels(root,1);
	    
	}
	
	static void displayTreeRec(Node node, int numChild,int level)
	{
		for(int i=0;i<level;i++)
			System.out.print(" ");
		System.out.println(numChild+"-"+node.value);
		if(node.children==null) return;
			
		for(int i=0;i<node.children.length;i++)
		{
			displayTreeRec(node.children[i],i,level+1);
		}
			

	}
	

	static double minimax(Node node, int depth, boolean maximizingPlayer) {
	    if (node.children == null) {
	        return node.value;
	    }
	    //if maximizingPlayer is false, the function will try to minimize the evaluation value
	    //It will then return the minimum evaluation value among the children.
	    //when it is true then it maximizes
	    if (maximizingPlayer) {
	        double maxEval = -2000;
	        for (Node child : node.children) {
	            double eval = minimax(child, depth - 1, false);
	            maxEval = Math.max(maxEval, eval);
	        }
	        node.value=maxEval;
	        return maxEval;
	    } else {
	        double minEval = 2000;
	        for (Node child : node.children) {
	            double eval = minimax(child, depth - 1, true);
	            minEval = Math.min(minEval, eval);
	        }
	        node.value=minEval;
	        return minEval;
	    }
	}
  
	 static Node createRootNode(int[][] board) {
	        Node root = new Node();
	        copyBoard(board, root.board);

	        int numChildren = calculateNumChildren(board);
	        root.children = new Node[numChildren];

	        return root;
	    }	
	
	
	static int inputColumnPC(int board[][]) {
	    int bestMove = -1;
	    double bestValue = -2000;

	    Node root = createRootNode(board);
	    createTree(root);
	    displayTreeRec(root,0,0);

	    minimax(root, MAX_DEPTH , true);
	    for (int i = 0; i < root.children.length; i++) {
            double eval = root.children[i].value;
            if (eval > bestValue) {
                bestValue = eval;
                bestMove = i;
            }
        }
	    displayTreeRec(root,0,0);

	    return transformChildNumber2Column(board, bestMove);
	}
	
	
	static void initBoard(int board[][])
	{
		for (int i = 0; i < N; i++) {
        	for (int j = 0; j < N; j++) {
        		board[i][j] = EMPTY;
        	}
    	}
	}
	
	static void displayBoard(int board[][])
	{
		for (int i = 1; i <= N; i++) {
            	System.out.printf("%d ",i);
		}
		System.out.printf("\n");
		for (int i = 1; i <= N; i++) {
            	System.out.printf("--");
		}
		System.out.printf("\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				System.out.printf("%d ",board[i][j]);
			}
			System.out.printf("\n");
		}
	}
	
	static boolean isColumnFull(int board[][], int col) {
	    if(board[0][col] == 0)
	      return false;
	    else
	      return true;
	}
	
	static int inputColumn(int board[][])
	{
		// check if column if full. create a function for it
		//repeat nextInt() until the column is valid between 1-8 and the col is not full
		
		 int col;
	        do {
	            System.out.printf("Enter a column (1-8): ");
	            col = sc.nextInt();
	            if (col < 1 || col > 8) {
	                System.out.printf("Invalid column. Please try again.");
	                continue;
	            }
	            if (isColumnFull(board, col - 1)) {
	                System.out.printf("Column is full. Please try again.");
	            } else {
	                break;
	            }
	        } while (true);

	        return col - 1;
	}
	
	static int calculateRow(int board[][],int col)
	{
		int r=0;
		while(r<N && board[r][col]==EMPTY)
		{
			r++;
		}
		return r-1;

	}
	
	static void setToken(int board[][] , int col , int token)
	{
		int row=calculateRow(board,col);
		board[row][col]=token;
	}
	
	static boolean playerWon(int board[][], int player) {

	    /// Check rows
	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j <= N - 4; j++) {
	            if (board[i][j] == player &&
	                board[i][j + 1] == player &&
	                board[i][j + 2] == player &&
	                board[i][j + 3] == player) {
	                return true;
	            }
	        }
	    }

	    /// Check columns
	    for (int j = 0; j < N; j++) {
	        for (int i = 0; i <= N - 4; i++) {
	            if (board[i][j] == player &&
	                board[i + 1][j] == player &&
	                board[i + 2][j] == player &&
	                board[i + 3][j] == player) {
	                return true;
	            }
	        }
	    }

	    /// Check diagonals
	    for (int i = 0; i <= N - 4; i++) {
	        for (int j = 0; j <= N - 4; j++) {
	            if (board[i][j] == player &&
	                board[i + 1][j + 1] == player &&
	                board[i + 2][j + 2] == player &&
	                board[i + 3][j + 3] == player) {
	                return true;
	            }
	        }
	    }

	    for (int i = 3; i < N; i++) {
	        for (int j = 0; j <= N - 4; j++) {
	            if (board[i][j] == player &&
	                board[i - 1][j + 1] == player &&
	                board[i - 2][j + 2] == player &&
	                board[i - 3][j + 3] == player) {
	                return true;
	            }
	        }
	    }

	    return false;
	}
	static boolean gamedraw(int board[][])
	{
		for (int i = 0; i < N; i++) {
	            if (board[0][i]== EMPTY) {
	                return false;
	            }
	        }
	    return true;
	}
	
	public static void main(String[] args) {
		int board[][]= new int[N][N], col;
	    initBoard(board);
	    displayBoard(board);
  
	while(true) {
	    col=inputColumn(board);
	    setToken(board,col,HUMAN);
	    displayBoard(board);
	    System.out.println();
	    
	    if(playerWon(board,HUMAN))
	    {
	    	System.out.println("Human won!!"); 
	    	break;
	    }

	    col=inputColumnPC(board);
	    setToken(board,col,PC);
	    displayBoard(board);
	    System.out.println();
	    
	    if(playerWon(board,PC))
	    {
	    	System.out.println("PC won!!");
	    	break;
	    }
	   
	    if (gamedraw(board)) {
            System.out.println("The game ended in a draw!");
            break;
        }
	}
	
	}

}

