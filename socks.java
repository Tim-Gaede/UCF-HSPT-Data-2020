import java.util.Arrays;
import java.util.Scanner;

public class socks {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int tt = scan.nextInt();
		//Loop through the piles
		while(tt-->0) {
			int n = scan.nextInt();
			int[] arr =new int[n*2];
			for(int i = 0;i<n*2;i++) {
				arr[i] =scan.nextInt();
			}
			boolean[] newSock = new boolean[n+1];
			Arrays.fill(newSock, true);
			int[] ans = new int[n];
			int idx = 0;
			for(int i = 0;i<n*2;i++) {
				if(newSock[arr[i]]) {
					//Secure the sock pair position's in the pile
					//We know that this sock will form a valid pair because each type of sock appears an even number of times
					ans[idx] = arr[i];
					idx++;
					///Mark this type of sock so that the next sock found does not create a new pair
					newSock[arr[i]] = false;
				}else {
					//This sock was marked as part of another pair
					//Mark this type of sock as ready to start another pair next time it shows up
					newSock[arr[i]] = true;
				}
			}
			//The resulting array we created will be ordered from bottom to top
			//This is because we created it by stacking socks from the ground up
			//The answer requested in the problem statement is in order from top to bottom
			//Simply loop through our answer backwards and print it out
			for(int i = n-1;i>=0;i--) {
				//Print each sock twice because it's a pair
				System.out.print(ans[i]+" "+ans[i]+" ");
			}
			System.out.println();
		}
	}

}
