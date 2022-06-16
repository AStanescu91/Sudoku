package com.alex.sudokusolver;

public class SudokuSolver {
	public static class Vectors {
		public int[] rows;
		public int[] cols;
		public int[] blocks;
		
		public Vectors(int[] rows, int[] cols, int[] blocks) {
			this.rows = rows;
			this.cols = cols;
			this.blocks = blocks;
		}
		
		public static Vectors toVectors(int[][] possibleBoard) {
			int[] rows = new int[possibleBoard.length];
			int[] cols = new int[possibleBoard[0].length];
			int[] blocks = new int[9];
			
			for (int row = 0; row < possibleBoard.length; row++) {
				for (int col = 0; col < possibleBoard[0].length; col++) {
					if (possibleBoard[row][col] != 0) {
						int shift = 0x1 << (possibleBoard[row][col] - 1);
						int block = 3 * (int)(row / 3) + (col / 3);
						
						rows[row] |= shift;
						cols[col] |= shift;
						blocks[block] |= shift;	
					}
				}
			}

			return new Vectors(rows, cols, blocks);
		}
	}
	
	private Vectors vectors;
	private int[][] board;
	
	public SudokuSolver(int[][] board) {
		this.board = board;
		this.vectors = Vectors.toVectors(board);
	}
	
	public boolean solve() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (board[row][col] == 0) {
					int block = 3 * (int)(row / 3) + (col / 3);
					int possibleValues = 0x1FF ^ (vectors.rows[row] | vectors.cols[col] | vectors.blocks[block]);
					if (possibleValues == 0) return false;
					boolean solved = false;
					for (int i = 0; i < 9; i++) {
						int mask = (0x1 << i);
						if ((possibleValues & mask) != 0) {
							int value = log2(mask) + 1;
							board[row][col] = value;
							vectors.rows[row] |= mask;
							vectors.cols[col] |= mask;
							vectors.blocks[block] |= mask;
							solved = solve();
							if (solved) {
								return solved;
							}
							board[row][col] = 0;
							vectors.rows[row] &= ~mask;
							vectors.cols[col] &= ~mask;
							vectors.blocks[block] &= ~mask;
						}
					}
					return false;
				}
			}
		}
		
		return true;
	}
	
	private int log2(int n) {
        int result = (int)(Math.log(n) / Math.log(2));
        return result;
    }
	
	public void printBoard() {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				System.out.print(board[row][col] + " ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		int[][] board = new int[][] {
			{0, 2, 0, 0, 1, 4, 0, 0, 0},
			{6, 0, 8, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 6, 0, 0, 0, 3, 8},
			{0, 1, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 6, 0},
			{0, 4, 0, 0, 7, 0, 1, 0, 0},
			{5, 0, 0, 0, 0, 0, 2, 0, 0},
			{3, 0, 0, 8, 0, 0, 0, 0, 0}
		};
		
		int[][] hardestEverSudoku = new int[][] {
			{8, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 3, 6, 0, 0, 0, 0, 0},
			{0, 7, 0, 0, 9, 0, 2, 0, 0},
			{0, 5, 0, 0, 0, 7, 0, 0, 0},
			{0, 0, 0, 0, 4, 5, 7, 0, 0},
			{0, 0, 0, 1, 0, 0, 0, 3, 0},
			{0, 0, 1, 0, 0, 0, 0, 6, 8},
			{0, 0, 8, 5, 0, 0, 0, 1, 0},
			{0, 9, 0, 0, 0, 0, 4, 0, 0}
		};
		
		int[][] anotherSudoku = new int[][] {
			{0, 0, 9, 3, 0, 0, 0, 0, 0},
			{8, 0, 0, 0, 0, 0, 0, 2, 0},
			{0, 7, 0, 0, 1, 0, 5, 0, 0},
			{4, 0, 0, 0, 0, 5, 3, 0, 0},
			{0, 1, 0, 0, 7, 0, 0, 0, 6},
			{0, 0, 3, 2, 0, 0, 0, 8, 0},
			{0, 6, 0, 5, 0, 0, 0, 0, 9},
			{0, 0, 4, 0, 0, 0, 0, 3, 0},
			{0, 0, 0, 0, 0, 9, 7, 0, 0}
		};
		
		int[][] manyGiven = new int[][] {
			{0, 2, 3, 0, 6, 5, 0, 8, 9},
			{9, 0, 0, 0, 0, 4, 0, 0, 5},
			{5, 0, 0, 9, 0, 0, 0, 0, 0},
			{6, 0, 0, 3, 0, 0, 0, 1, 8},
			{3, 8, 0, 5, 9, 0, 0, 0, 2},
			{0, 0, 0, 0, 8, 6, 3, 0, 0},
			{2, 3, 0, 0, 0, 0, 0, 0, 6},
			{8, 0, 7, 0, 2, 0, 0, 0, 3},
			{0, 9, 6, 0, 5, 3, 8, 2, 0}
		};
		
		SudokuSolver solver = new SudokuSolver(manyGiven);
		solver.printBoard();
		System.out.println();
		boolean solvable = solver.solve();
		solver.printBoard();
	}

}
