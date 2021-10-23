'''
HSPT 2020 - silly.py
by Jacob Magnuson
(cf\\barakraganosungam)
'''

from math import ceil
alp = 'qwertyuiopasdfghjklzxcvbnm'
# Iterate over each testcase
for tc in range(int(input())):
    # Read in length of string and string itself
    L, word = int(input()), input().strip()
    # If the string is of even length, removing a character will give us an odd string we can't split.
    if not (L & 1): print(0); continue

    # These are dictionary (map) objects that store the frequency of each letter in each half
    #    of the string. We will put the middle character on the left side, with length ceil(L / 2),
    #    and the remaining floor(L / 2) on the right.
    left = {f : word[:L // 2 + 1].count(f) for f in alp}
    right = {f : word[-(L // 2):].count(f) for f in alp}
    ans, mid = 0, word[L // 2]
    # Two strings are anagrams if their character frequencies are exactly the same for all letters.
    #    If we take a character C from the first ceil(L / 2), our right string stays the same, and our
    #      left string's frequency of C decreases by 1.
    #    If we take a character C from the back floor(L / 2), the middle character in the larger string
    #      becomes the first character of the right string, and the first floor(L / 2) characters make up
    #      the left string.
    for x in range(ceil(L / 2)):
        # We will simulate removing the character at index x, and compare the left/right frequencies.
        #    To do this, we will decrease the frequency of this character by 1 when comparing.
        if all(right[c] == (left[c] - 1 if c == word[x] else left[c]) for c in alp):
            ans += 1
    # Move the middle character from the left to the right side, and resume at the next character index
    left[mid] -= 1; right[mid] += 1
    for x in range(ceil(L / 2), L):
        if all(left[c] == (right[c] - 1 if c == word[x] else right[c]) for c in alp):
            ans += 1
    print(ans) # We're done! But... Can you think of a way to do this faster?
