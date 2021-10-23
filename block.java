import java.util.*;

public class block {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int t = scan.nextInt();
		for(int q = 1; q <= t; q++) {
			// input
			char[][] grid = new char[9][9];
			for(int i = 0; i < 9; i++) grid[i] = scan.next().toCharArray();

			char[][][] pieces = new char[3][][];
			int n = scan.nextInt();
			int m = scan.nextInt();
			pieces[0] = new char[n][m];
			for(int i = 0; i < n; i++) pieces[0][i] = scan.next().toCharArray();

			n = scan.nextInt();
			m = scan.nextInt();
			pieces[1] = new char[n][m];
			for(int i = 0; i < n; i++) pieces[1][i] = scan.next().toCharArray();

			n = scan.nextInt();
			m = scan.nextInt();
			pieces[2] = new char[n][m];
			for(int i = 0; i < n; i++) pieces[2][i] = scan.next().toCharArray();

			boolean ans = false;
			
			// a, b, c, are 1 if we will attempt to place piece 1, 2, and 3 respectively
			// in this iteration
			for(int a = 0; a < 2; a++) {
				for(int b = 0; b < 2; b++) {
					for(int c = 0; c < 2; c++) {
						if(a == 0 && b == 0 && c == 0) continue;

						// iterate over possible placement positions
						for(int i = 0; i < 9; i++) {
							boolean bad = false;
							for(int ii = 0; ii < 9; ii++) {
								bad = false;

								// if we are attempting to place the first piece at this iteration
								// validate that it is possible
								if(a == 1) {
									bad = checkPlacement(i, ii, pieces[0], grid);
								}
								
								if(bad) continue;
								
								// if we are attempting to place the first piece and it is possible
								// update the grid
								if(a == 1) {
									place(i, ii, pieces[0], grid);
								}
								
								// we repeat the above process for the other pieces
								for(int j = 0; j < 9; j++) {
									bad = false;
									for(int jj = 0; jj < 9; jj++) {
										bad = false;

										// place if possible
										if(b == 1) {
											bad = checkPlacement(j, jj, pieces[1], grid);
										}
										
										if(bad) continue;
										
										if(b == 1) {
											place(j, jj, pieces[1], grid);
										}

										for(int k = 0; k < 9; k++) {
											bad = false;
											for(int kk = 0; kk < 9; kk++) {
												bad = false;

												if(c == 1) {
													bad = checkPlacement(k, kk, pieces[2], grid);
												}
												
												if(bad) continue;
												
												if(c == 1) {
													place(k, kk, pieces[2], grid);
												}
												
												// for each row, column, and 3x3 square
												// we will check if at least one is completely filled
												for(int l = 0; l < 9; l++) {
													boolean row = true;
													boolean col = true;
													
													// check that rows and columns are filled
													for(int ll = 0; ll < 9; ll++) {
														if(grid[l][ll] == '.') row = false;
														if(grid[ll][l] == '.') col = false;
													}
													
													// ans |= x -> if ans is true, remains true
													//			   otherwise ans becomes true if x is true
													ans |= row;
													ans |= col;
													
													// check that the 3x3 squares are filled
													// each square's top left corner is at (ro*3, co*3)
													int ro = l/3;
													int co = l%3;
													boolean sq = true;
													for(int z = 0; z < 3; z++) {
														for(int y = 0; y < 3; y++) {
															if(grid[ro*3+z][co*3+y] == '.') sq = false;
														}
													}
													ans |= sq;
												}

												// now that we have made the check,
												// for every piece that has been placed,
												// we will undo the placement
												// so that we may place it somewhere else
												if(c == 1) {
													undoPlace(k, kk, pieces[2], grid);
												}
											}
										}

										if(b == 1) {
											undoPlace(j, jj, pieces[1], grid);
										}										
									}
								}
					
								if(a == 1) {
									undoPlace(i, ii, pieces[0], grid);
								}

							}
						}

					}
				}
			}
			System.out.println(ans? "Yes":"No");
		}
	}
	
	// returns true if placement is bad -
	// 	meaning the piece cannot be placed at position (i, ii) in the grid
	//  pieces are placed with their top left corner at position (i, ii)
	static boolean checkPlacement(int i, int ii, char[][] piece, char[][] grid) {
		boolean bad = false;
		if (i+piece.length > 9) bad = true;
		if (ii+piece[0].length > 9) bad = true;

		if(!bad) {
			for(int A = 0; A < piece.length; A++) {
				for(int B = 0; B < piece[0].length; B++) {
					if(piece[A][B] == '.') continue;
					if(grid[i+A][ii+B] == '#') bad = true;
				}
			}
		}
		return bad;
	}
	
	// place the piece on the grid
	static void place(int i, int ii, char[][] piece, char[][] grid) {
		for(int A = 0; A < piece.length; A++) {
			for(int B = 0; B < piece[0].length; B++) {
				if(piece[A][B] == '.') continue;
				grid[i+A][ii+B] = '#';
			}
		}
	}
	
	// undo the placement of the piece
	static void undoPlace(int i, int ii, char[][] piece, char[][] grid) {
		for(int A = 0; A < piece.length; A++) {
			for(int B = 0; B < piece[0].length; B++) {
				if(piece[A][B] == '.') continue;
				grid[i+A][ii+B] = '.';
			}
		}
	}

}
