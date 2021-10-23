// Author: Jacob Franz
// Java Solution to "Bigloo Building"
// 2020 UCF HSPT

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class bigloo
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		int cases = in.nextInt();
		
		// Loop through all of the test cases
		for (int z = 1; z <= cases; z++)
		{
			// Store the number of layers and required height
			int n = in.nextInt();
			int h = in.nextInt();
			
			// Store all of the layers
			ArrayList<Integer> layers = new ArrayList<Integer>(n);
			for (int i = 0; i < n; i++)
				layers.add(in.nextInt());
			
			// Sort the layers in descending order
			Collections.sort(layers, Collections.reverseOrder());
			
			// Store the maximum base size, previous layer size, and current stack height
			int maxBase = layers.get(0);
			int prevLayer = layers.get(0);
			int currHeight = 1;
			
			// Loop through the remaining layers
			for (int i = 1; i < n; i++)
			{
				// Break out of the loop if we have met the height requirement
				if (currHeight == h)
					break;
				
				// Current layer we are looking at
				int currLayer = layers.get(i);
				
				// Skip duplicate layers
				if (currLayer == prevLayer)
					continue;
				
				// This layer can be stacked on top of the previous layer
				if (currLayer == prevLayer - 1)
				{
					prevLayer = currLayer;
					currHeight++;
				}
				
				// Start a new stack
				else
				{
					maxBase = currLayer;
					prevLayer = currLayer;
					currHeight = 1;
				}
			}
			
			// If we were able to build a tall enough stack, print the size of the base
			if (currHeight == h)
				System.out.println(maxBase);
			
			else
				System.out.println("We're gonna freeze out here!");
		}
	}
}
