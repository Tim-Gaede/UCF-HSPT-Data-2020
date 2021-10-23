// Author: Jacob Franz
// Java Solution to "Typing Contest"
// 2020 UCF HSPT

import java.util.Scanner;

public class typing
{
	public static void main(String[] args)
	{	
		Scanner in = new Scanner(System.in);
		int cases = in.nextInt();
		
		// Loop through all of the test cases
		for (int z = 1; z <= cases; z++)
		{
			// Store the typing speeds
			int sharonSpeed = in.nextInt();
			int challengerSpeed = in.nextInt();
			
			// Advance the scanner to the next line and store the contest string as an array of characters
			in.nextLine();
			char[] arr = in.nextLine().toCharArray();
			
			// Store whether caps lock and shift are currently pressed
			boolean capsLock = false;
			boolean shift = false;
			
			// Initializes each player's key presses to the length of the contest string
			long sharonPresses = arr.length; 
			long challengerPresses = arr.length;
			
			// Add the key presses for shift/caps lock if the initial character is upper case
			if (Character.isUpperCase(arr[0]))
			{
				sharonPresses++;
				challengerPresses++;
				
				capsLock = shift = true;
			}
			
			// Loop through the contest string, starting at the second character
			for (int i = 1; i < arr.length; i++)
			{
				// Current character is an upper case letter
				if (Character.isUpperCase(arr[i]))
				{
					if (!capsLock)
						sharonPresses++;
					
					if (!shift)
						challengerPresses++;
					
					capsLock = shift = true;
				}
				
				// Current character is a lower case letter
				else if (Character.isLowerCase(arr[i]))
				{
					if (capsLock)
						sharonPresses++;
					
					capsLock = shift = false;
				}
			}
			
			// The winner is whoever took the least amount of time to type the contest string
			if (sharonPresses * sharonSpeed < challengerPresses * challengerSpeed)
				System.out.println("Sharon has won");
			else
				System.out.println("Sharon is gone");
		}
	}
}
