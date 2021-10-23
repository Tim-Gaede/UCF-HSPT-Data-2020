import java.util.Arrays;
import java.util.Scanner;

public class winds {
	static int ans = Integer.MAX_VALUE/2;
	static int n;
	static int[] dx = {1,-1,0,0};
	static int[] dy = {0,0,1,-1};
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int tt = scan.nextInt();
		for(int t = 1; t<=tt;t++) {
			ans = Integer.MAX_VALUE/2;
			n = scan.nextInt();
			char[][] board =new char[n][n];
			for(int i = 0;i<n;i++) {
				board[i] = scan.next().toCharArray();
			}
			//The most moves we ever need to do is 8
			//This is because the board is always surrounded by water, and the max size of the board is 10x10
			//So worst case scenario we just push all aliens in one direction and they will reach water in at most 8 pushes
			//This solution uses that observation and considers all possible sequence of moves up to a total of 8 moves
			//There's only 4 possible options for each move, so the runtime turns out ok
			int[] perm = new int[n-2];
			permute(0,perm,board);
			System.out.println(ans);
		}
	}
	
	static void permute(int idx, int[] perm, char[][] board) {
		//Use the current permutation to see if it results in all aliens in the water
		char[][] b = new char[n][n];
		for(int i = 0;i<n;i++) {
			for(int j =0;j<n;j++) {
				b[i][j] = board[i][j];
			}
		}
		int[][] arr= new int[n][n];
		boolean[][] start = new boolean[n][n];
		for(int j = 0;j<n;j++) Arrays.fill(arr[j], 0);
		for(int j = 0;j<n;j++) {
			for(int k = 0;k<n;k++) {
				if(board[j][k]=='A') {
					arr[j][k] = 1;
				}
			}
		}
		for(int i = 0;i<idx;i++) {
			for(int j = 0;j<n;j++) {
				for(int k = 0;k<n;k++) {
					if(arr[j][k]>=1)start[j][k] = true;
				}
			}
			for(int j = 0;j<n;j++) {
				for(int k = 0;k<n;k++) {
					if(arr[j][k]>=1 && start[j][k]) {
						int toRow = j+dy[perm[i]];
						int toCol = k+dx[perm[i]];
						arr[j][k]--;
						arr[toRow][toCol]++;
						if(board[toRow][toCol]=='W')arr[toRow][toCol] = 0;
						start[j][k] = false;
					}
				} 
			}
			boolean noAliens = true;
			for(int j = 0;j<n;j++) {
				for(int k = 0;k<n;k++) {
					if(arr[j][k] >0) {
						//not good so far
						noAliens = false;
					}
				}
			}
			if(noAliens && i+1<ans) {
				ans = Math.min(ans, i+1);
				break;
			}
		}
		if(idx>=perm.length)return;
		for(int i = 0;i<4;i++) {
			perm[idx] = i;
			permute(idx+1,perm,b);
		}
	}
}
