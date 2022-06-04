package com.alex.sudokusolver;

public class SudokuSolver {
	private int[][] board;
	private int[] rows;
	private int[] cols;
	private int[] cells;
	private int[][] possibleBoard;
	
	public SudokuSolver(int[][] board) {
		this.board = board;
		this.possibleBoard = new int[board.length][board[0].length];
		this.rows = new int[board.length];
		this.cols = new int[board[0].length];
		this.cells = new int[9];
	}
	
	public int[][] solve() {
		
		
		boolean oneBitSet = true;
		while (oneBitSet) {
			oneBitSet = false;
			for (int row = 0; row < board.length; row++) {
				for (int col = 0; col < board[0].length; col++) {
					if (board[row][col] != 0) {
						int shift = 0x1 << (board[row][col] - 1);
						int cell = 3 * (int)(row / 3) + (col / 3);
						
						rows[row] |= shift;
						cols[col] |= shift;
						cells[cell] |= shift;	
					}
				}
			}
			
			for (int row = 0; row < board.length; row++) {
				for (int col = 0; col < board[0].length; col++) {
					if (board[row][col] == 0) {
						int cell = 3 * (int)(row / 3) + (col / 3);
						possibleBoard[row][col] = 0x1FF ^ (rows[row] | cols[col] | cells[cell]);
						if (isOneBitSet(possibleBoard[row][col])) {
							oneBitSet = true;
							board[row][col] = log2(possibleBoard[row][col]) + 1;
						}
					} else {
						possibleBoard[row][col] = (0x1 << (board[row][col] - 1));
					}
				}
			}
		}
		
		return possibleBoard;
	}
	
	public boolean isOneBitSet(int bits) {
		if (bits == 0) return false;
		int bitsMinusOne = bits - 1;
		int maskedBits = bits & bitsMinusOne;
		boolean notZero = (bits != 0 && maskedBits != 0);
		return !notZero;
	}
	
	private int log2(int n)
    {
        int result = (int)(Math.log(n) / Math.log(2));
        return result;
    }
	
	public int[] getRows() { return this.rows; }
	public int[] getCols() { return this.cols; }
	public int[] getCells() { return this.cells; }
	public int[][] getPossibleMatrix() { return this.possibleBoard; }
	
	public static void main(String[] args) {
		int[][] board = new int[][] {
			{0, 0, 5, 3, 4, 2, 0, 8, 0},
			{4, 0, 0, 0, 0, 0, 0, 0, 0},
			{6, 8, 0, 1, 0, 0, 0, 5, 0},
			{0, 0, 4, 0, 1, 0, 0, 3, 0},
			{1, 3, 2, 6, 0, 5, 0, 0, 0},
			{0, 9, 0, 7, 0, 4, 1, 0, 2},
			{0, 4, 0, 0, 0, 1, 7, 0, 0},
			{2, 0, 6, 4, 7, 3, 8, 0, 1},
			{0, 7, 1, 9, 0, 6, 0, 4, 0}
		};
		
		SudokuSolver solver = new SudokuSolver(board);
		int[][] solved = solver.solve();
		
		for (int row = 0; row < solved.length; row++) {
			for (int col = 0; col < solved[0].length; col++) {
				System.out.print(String.format("% 4d", board[row][col]) + " ");
			}
			System.out.println();
		}
		
		System.out.println("\n\n");
		
		for (int row = 0; row < solved.length; row++) {
			for (int col = 0; col < solved[0].length; col++) {
				System.out.print(String.format("% 4d", solved[row][col]) + " ");
			}
			System.out.println();
		}
		
		System.out.println("\n\n");
		
		for (int row = 0; row < solved.length; row++) {
			for (int col = 0; col < solved[0].length; col++) {
				System.out.print(String.format("%8b", solver.isOneBitSet(solved[row][col])) + " ");
			}
			System.out.println();
		}
	}
}
