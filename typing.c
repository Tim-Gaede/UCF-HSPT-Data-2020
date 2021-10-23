#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

int main(void)
{
	int t, tt, s, c, nums, numc, len, i;
	size_t maxlen = 1024;
	char str[1024];
	bool prevupper, currupper;

	// scan test cases
	scanf("%d\n", &t);
	
	for (tt = 1; tt <= t; tt++)
	{
		// scan s and c
		scanf("%d %d\n", &s, &c);

		// scan string
		fgets(str, maxlen, stdin);

		len = strlen(str);
		
		// getline() gives the string scanned in with an extra new line character,
		// so we're gonna subtract 1 from length to make sure our for loop and initial
		// characters we add to both Sharon and his competitor are accurate
		len--;
		
		// nums and numc represent the number of characters Sharon types in total,
		// and the number of characters the competitor types, respectively.
		// since both have to type the actual characters themselves, we will add those
		// now; the only thing that should differ between them is the caps lock/shift
		// presses
		nums = len;
		numc = len;
		// prevupper and currupper store whether the previous character was uppercase
		// and whether the current character is uppercase
		prevupper = false;
		currupper = false;

		// the idea here is that we want to greedily keep the caps lock key
		// in one position for Sharon until he must toggle it, so we only add
		// a caps lock press if he switches from upper case to lower case.

		// for the competitor, we will keep the shift key held as long as we
		// are typing capital letters, so we only add when we switch from lowercase
		// to uppercase
		for (i = 0; i < len; i++)
		{
			// we also want both people to have the minimum number of presses possible,
			// so we can skip the spaces.
			// if we are typing uppercase letters, then a space, then more uppercase letters,
			// we want to make sure we aren't adding key presses for the space, so we can basically
			// keep the caps lock on while typing a space for Sharon, and keep the shift key held
			// for the competitor
			if (str[i] == ' ')
			{
				continue;
			}

			currupper = isupper(str[i]);

			// this is Sharon toggling his caps lock
			if (prevupper != currupper)
			{
				nums++;
			}

			// this is the competitor starting to press their shift key
			if (!prevupper && currupper)
			{
				numc++;
			}

			prevupper = currupper;
		}

		// we want to compare the times it took sharon and the competitor to press
		// all the keys, we can get these using (number of keys pressed) * (time it
		// takes to press one key) for each person
		printf(nums * s < numc * c ? "Sharon has won\n" : "Sharon is gone\n");
	}

	return 0;
}