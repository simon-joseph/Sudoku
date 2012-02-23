import java.awt.Point;
import java.util.Random;

/**
 * 
 */

/**
 * @author Szymon Laszczynski
 *
 */
public class SimpleSudokuGenerator {
	/**
	 *  ___ ___ ___
	 * | 0 | 1 | 2 |
	 * |---+---+---|
	 * | 3 | 4 | 5 |
	 * |---+---+---|
	 * | 6 | 7 | 8 |
	 * |___|___|___|
	 * 
	 *  Having row R and colum C
	 *  we compute group ID by
	 *  floor(R/3)*3 + floor(C/3)
	 * */
	
	private static final int[][] solution = {
			{ 1, 2, 3, 4, 5, 6, 7, 8, 9 },
			{ 4, 5, 6, 7, 8, 9, 1, 2, 3 },
			{ 7, 8, 9, 1, 2, 3, 4, 5, 6 },
			{ 2, 3, 4, 5, 6, 7, 8, 9, 1 },
			{ 5, 6, 7, 8, 9, 1, 2, 3, 4 },
			{ 8, 9, 1, 2, 3, 4, 5, 6, 7 },
			{ 3, 4, 5, 6, 7, 8, 9, 1, 2 },
			{ 6, 7, 8, 9, 1, 2, 3, 4, 5 },
			{ 9, 1, 2, 3, 4, 5, 6, 7, 8 }};
	
	private static short leftNumbers = 20;

	public static int[][] generate(long seed){
		Random rand = new Random(seed);
		int [][] puzzle = new int[9][9];
		puzzle = solution;
		for( int i = 0; i < 1 + rand.nextInt(10); i++ )
			puzzle = switchLinesInGroupRow(rand.nextInt(3), rand.nextInt(3), rand.nextInt(3), puzzle);
		for( int i = 0; i < 1 + rand.nextInt(2); i++ )
			puzzle = switchGroupRows(rand.nextInt(3), rand.nextInt(3), puzzle);
		for( int i = 0; i < 1 + rand.nextInt(10); i++ )
			puzzle = switchNumbersInAllGroups(rand.nextInt(9) + 1, rand.nextInt(9) + 1, puzzle);
		
		
		for(byte row = 0; row < 9; row++)
			System.out.println(
					puzzle[0][row] + " " +
					puzzle[1][row] + " " +
					puzzle[2][row] + " " +
					puzzle[3][row] + " " +
					puzzle[4][row] + " " +
					puzzle[5][row] + " " +
					puzzle[6][row] + " " +
					puzzle[7][row] + " " +
					puzzle[8][row]);
		
		puzzle = leaveOnlyThisMuchNumbers(leftNumbers, rand, puzzle);
		return puzzle;
	}

	private static int[][] switchNumbersInAllGroups(int number1, int number2, int[][] puzzle) {
		if ( number1 == number2 )
			number2 = ( ( number2 - 1) % 9 ) + 1;
		for( byte group = 0; group < 9; group ++ )
			puzzle  = switchNumbersInGroup(number1, number2, group, puzzle);
		return puzzle;
	}

	private static int[][] switchNumbersInGroup(int number1, int number2, byte group, int[][] puzzle) {
		Point topLeft =  groupTopLeft(group);
		Point index1 = new Point(0,0);
		Point index2 = new Point(0,0);
		
		for ( int x = topLeft.x; x < topLeft.x + 3; x++ )
			for( int y = topLeft.y; y < topLeft.y + 3; y++ ) {
				if ( puzzle[x][y] == number1 )
					index1 = new Point(x, y);
				else if ( puzzle[x][y] == number2 )
					index2 = new Point(x , y);
			}
		
		int temp = puzzle[index1.x][index1.y];
		puzzle[index1.x][index1.y] = puzzle[index2.x][index2.y];
		puzzle[index2.x][index2.y] = temp;
		return puzzle;
	}

	private static int[][] switchGroupRows(int groupRow1, int groupRow2, int[][] puzzle) {
		if ( groupRow1 == groupRow2 )
			groupRow2 = ( groupRow2 + 1 ) % 3;
		int[][] temp = new int[9][3];
		
		for( int x = 0; x < 9; x++ )
			for( int y = 0; y < 3; y++ )
				temp[x][y] = puzzle[x][y + groupRow1*3];
		
		for( int x = 0; x < 9; x++ )
			for( int y = 0; y < 3; y++ )
				puzzle[x][y + groupRow1 * 3] = puzzle[x][y + groupRow2 * 3];
		
		for( int x = 0; x < 9; x++ )
			for( int y = 0; y < 3; y++ )
				puzzle[x][y + groupRow2*3] = temp[x][y];
		return puzzle;
	}

	private static int[][] switchLinesInGroupRow(int line1, int line2, int groupRow, int[][] puzzle) {
		if ( line1 == line2 )
			line2 = ( line2 + 1 ) % 3;
		int[] temp = new int[9];
		for ( int x = 0; x < 9; x++ )
			temp[x] = puzzle[x][line1 + groupRow*3];
		for ( int x = 0; x < 9; x++ )
			puzzle[x][line1 + groupRow*3] = puzzle[x][line2 + groupRow*3];
		for ( int x = 0; x < 9; x++ )
			puzzle[x][line2 + groupRow*3] = temp[x];
		return puzzle;
	}
	
	public static byte computeSquareID(byte row, byte column) {
		return (byte) (Math.floor(column/3.0)*3 + Math.floor(row/3.0));
	}
	
	public static Point groupTopLeft( byte group ) {
		return new Point((group % 3) * 3, group - group % 3);
	}
	
	private static int[][] leaveOnlyThisMuchNumbers( int thismuch, Random rand, int[][] puzzle) {
		boolean[] fields = new boolean[81];
		boolean wasSet = false;
		int randIndex;
		for ( int i = 0; i < fields.length; i++ )
			fields[i] = false;
		
		for ( int i = 0; i < thismuch; i++ ) {
			randIndex = rand.nextInt(81);
			if (fields[randIndex]) {
				wasSet = false;
				while(!wasSet) {
					randIndex = (randIndex + 1) % 81;
					if(!fields[randIndex]) {
						fields[randIndex] = true;
						wasSet = true;
					}
				}
			} else
				fields[randIndex] = true;
		}
		
		for ( int i = 0; i < fields.length; i++ )
			if( !fields[i] )
				puzzle[i % 9][i/9] = -1;
		
		return puzzle;
	}
}

