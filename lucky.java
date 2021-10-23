import java.util.Scanner;
//Joshua Wozniak
//lucky.java
public class lucky
{
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		int t = scan.nextInt();
		for (int testCase = 0; testCase < t; testCase++)
		{
			int n = scan.nextInt();
			// We'll keep a counter of how many lucky charms Daniel has
			int count = 0;
			for (int i = 0; i < n; i++)
			{
				// Increment the counter if the clover has 4 leaves
				if (scan.nextInt() == 4)
					count++;
			}
			// Print the total number of lucky charms
			System.out.println(count);
		}
	}
}
