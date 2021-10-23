// Solution to Ahmad's Mod for the UCF HSPT Contest
// Written by: Pauline Johnson

import java.util.*;

public class mod {
	static void solve(Scanner kb) {
		// Read in input.
		int n = kb.nextInt();
		int[] arr = new int[n];
		for(int i = 0; i < n; i++) {
			arr[i] = kb.nextInt();
		}

		// Check if all elements in input are equal.
		int sharon = arr[0];
		for(int i = 1; i < n; i++) {
			if(arr[i] != sharon)
				sharon = -1;
		}

		// If all elements are equal (or the array siz e= 1), all mods 
		// will produce the same value, so the maximum mod is unbounded.
		if(sharon != -1) {
			System.out.println("Thank you, Maude!");
			return;
		}

		// Find the gcd between each pair of numbers and all pairs before it.
		int pair = Math.abs(arr[1] - arr[0]);
		for(int i = 1; i < n-1; i++) {
			pair = gcd(pair, Math.abs(arr[i+1] - arr[i]));
		}

		// Output the gcd between all pairs.
		System.out.println(pair);
	}

	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);

		// Read number of cases and run.
		int tests = kb.nextInt();
		while (tests-- > 0)
			solve(kb);
	}


	// Basic Euclidean algorithm for gcd.
	static int gcd(int a, int b) {
		if(a==0) return b;
		return gcd(b%a, a);
	}
}
