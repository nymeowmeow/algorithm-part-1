import edu.princeton.cs.algs4.StdOut;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Formatter;
import java.util.Arrays;

public class Board {
	private int[][] blk;
	private int emptyrowidx = -1;
	private int emptycolidx = -1;
	private int hammingdistance = -1;
	private int manhattandistance = -1;
	
	public Board(int[][] blocks) {
		//construct a board from an n-by-n array of blocks
		//(where blocks[i][j] = block in row i, column j)
		if (blocks == null || blocks.length == 0)
			throw new NullPointerException("invalid input");
		int N = blocks.length;
		blk = new int[N][];
		hammingdistance = manhattandistance = 0;
		for (int i = 0; i < N; ++i)
		{
			if (blocks[i] == null || blocks[i].length != N)
				throw new NullPointerException("invalid input");
			blk[i] = new int[blocks[i].length];
			for (int j = 0; j < blocks[i].length; ++j)
			{
				if (blocks[i][j] < 0 || blocks[i][j] >= N*N)
					throw new NullPointerException("invalid input");
				blk[i][j] = blocks[i][j];
				if (blocks[i][j] == 0)
				{
					emptyrowidx = i;
					emptycolidx = j;
				} else {
					if (blk[i][j] != (i*blk.length+(j+1)))
						++hammingdistance;
					int targetrow = (blk[i][j]-1) / blk.length;
					int targetcol = (blk[i][j]-1) % blk.length;
					manhattandistance += Math.abs(targetcol - j) + Math.abs(targetrow - i);
				}
			}
		}
		//hammingdistance = calculatehamming();
		//manhattandistance = calculatemanhattan();
	}
    
	public int dimension() {
		// board dimension n
		return blk.length;
	}
	
	public int hamming() {
	 	return hammingdistance;
	}

	//private int calculatehamming()
	//{
		// number of blocks out of place
		//int distance = 0;
		//for (int i = 0; i < blk.length; ++i)
			//for (int j = 0; j < blk[i].length; ++j)
			//{
				//if (blk[i][j] == 0)
					//continue;
				
				//if (blk[i][j] != (i*blk.length+(j+1)))
					//++distance;
			//}
		//return distance;
	//}
	
	public int manhattan() {
		return manhattandistance;
	}

	//private int calculatemanhattan() {
		// sum of Manhattan distances between blocks and goal
		//int distance = 0;
		//for (int i = 0; i < blk.length; ++i)
			//for (int j = 0; j < blk[i].length; ++j)
			//{
				//if (blk[i][j] == 0)
					//continue;
				
				//int targetrow = (blk[i][j]-1) / blk.length;
				//int targetcol = (blk[i][j]-1) % blk.length;
				//distance += Math.abs(targetcol - j) + Math.abs(targetrow - i);
			//}
		//return distance;
	//}
	
	public boolean isGoal() {
		// is this board the goal board?
		for (int i = 0; i < blk.length; ++i)
			for (int j = 0; j < blk.length; ++j)
			{
				if (i != blk.length-1 || j != blk.length-1)
					if (blk[i][j] != (i*blk.length+(j+1)))
						return false;
			}
		return true;
	}
	
	public Board twin() {
		// a board that is obtained by exchanging any pair of blocks
		int firstrow = -1, firstcol = -1, secondrow = -1, secondcol = -1;
		for (int i = 0; i < blk.length; ++i)
		{
			for (int j = 0; j < blk[i].length; ++j)
			{
				if (blk[i][j] != 0)
				{
					if (firstrow ==  -1)
					{
						firstrow = i;
						firstcol = j;
					} else if (secondrow == -1) {
						secondrow = i;
						secondcol = j;
					}
				}
				if (firstrow >= 0 && secondrow >= 0)
					break;
			}
			if (firstrow >= 0 && secondrow >= 0)
				break;
		}
		swap(blk, firstrow, firstcol, secondrow, secondcol);
		Board b = new Board(blk);
		swap(blk, secondrow, secondcol, firstrow, firstcol);
		return b;
	}
	
	private void swap(int[][] matrix, int row1, int col1, int row2, int col2)
	{
		int tmp = matrix[row1][col1];
		matrix[row1][col1] = matrix[row2][col2];
		matrix[row2][col2] = tmp;
	}
	
	public boolean equals(Object y) {
		// does this board equal y?
		if (y == null || y.getClass() != getClass())
				return false;
		
		Board other = (Board)y;
		if (other.dimension() != dimension())
			return false;
		
		for (int i = 0; i < blk.length; ++i)
			if (!Arrays.equals(blk[i], other.blk[i]))
			    return false;
		
		return true;
	}
	
	public Iterable<Board> neighbors()  {
		// all neighboring boards
		List<Board> resultlist = new ArrayList<Board>();
		int[][] matrix = blk;
		//left
		if (emptycolidx > 0)
		{
			swap(matrix, emptyrowidx, emptycolidx, emptyrowidx, emptycolidx-1);
			resultlist.add(new Board(matrix));
			swap(matrix, emptyrowidx, emptycolidx-1, emptyrowidx, emptycolidx);
		}
		//right
		if (emptycolidx != blk.length-1)
		{
			swap(matrix, emptyrowidx, emptycolidx, emptyrowidx, emptycolidx+1);
			resultlist.add(new Board(matrix));
			swap(matrix, emptyrowidx, emptycolidx+1, emptyrowidx, emptycolidx);
		}
		//top
		if (emptyrowidx != 0)
		{
			swap(matrix, emptyrowidx, emptycolidx, emptyrowidx-1, emptycolidx);
			resultlist.add(new Board(matrix));
			swap(matrix, emptyrowidx-1, emptycolidx, emptyrowidx, emptycolidx);
		}
		//bottom
		if (emptyrowidx != blk.length-1)
		{
			swap(matrix, emptyrowidx, emptycolidx, emptyrowidx+1, emptycolidx);
			resultlist.add(new Board(matrix));
			swap(matrix, emptyrowidx+1, emptycolidx, emptyrowidx, emptycolidx);
		}
		return resultlist;
	}
	
	public String toString() {
		// string representation of this board (in the output format specified below)
		StringBuilder s = new StringBuilder();
		Formatter formatter = new Formatter(s);
		formatter.format("%d\n", dimension());
		for (int i = 0; i < blk.length; ++i)
		{
			for (int j = 0; j < blk[i].length; ++j)
			{
				//s.append(blk[i][j]);
				formatter.format("%2d", blk[i][j]);
				if (j != blk[i].length-1)
					formatter.format(" ");
			}
			formatter.format("\n");
		}
		return s.toString();
	}

	public static void main(String[] args) {
		// unit tests (not graded)
	    // create initial board from file
		int n = 3;
	    int[][] blocks = new int[n][n];
	    blocks[0][0] = 8;
	    blocks[0][1] = 1;
	    blocks[0][2] = 3;
	    blocks[1][0] = 4;
	    blocks[1][1] = 0;
	    blocks[1][2] = 2;
	    blocks[2][0] = 7;
	    blocks[2][1] = 6;
	    blocks[2][2] = 5;
	    Board initial = new Board(blocks);
	    StdOut.println("is Goal " + initial.isGoal());
	    StdOut.println("mahattan " + initial.manhattan());
	    StdOut.println("hamming " + initial.hamming());
	    StdOut.println(initial);
	    Board twin = initial.twin();
	    StdOut.println("initial == twin " + initial.equals(twin));
	    Iterable<Board> iterable = initial.neighbors();
	    Iterator<Board> it = iterable.iterator();
	    while (it.hasNext())
	    	StdOut.println("neighbor \n" + it.next());
	}
}
