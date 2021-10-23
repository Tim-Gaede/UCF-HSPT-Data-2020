
t = int(input())
for tt in range(t):
    n, h = map(int, input().split())
    s = input().split()

    # Duplicate values don't matter because we can only
    # stack two rings if the diameter of the top one
    # is STRICTLY 1 greater than the bottom one
    uniques = set()
    for i in range(n):
        uniques.add(int(s[i]))

    # Get the unique values into an array
    d = list(uniques)
    n = len(d)
    
    # Sort the diameters
    d.sort()

    # At the start, the best run of consecutive integers
    # will be 1, we will build up these values
    best = [1] * n
    for i in range(1, n):
        # If the d[i - 1] is 1 less than d[i], then update
        # d[i]'s best value to be whatever d[i - 1]'s best
        # value is + 1
        if d[i - 1] + 1 == d[i]:
            best[i] = best[i - 1] + 1

    maxVal = -1
    for i in range(n):
        # If the best[i] is at least h, we satisfy the
        # of the problem. Since d is sorted, we can
        # take d[i] as our answer, since we are going
        # in increasing order
        if best[i] >= h:
            maxVal = d[i]

    # if we did not find a maxVal, then we did not have
    # a run of at least h consecutive diameters
    if maxVal == -1:
        print("We're gonna freeze out here!")
    else:
        print(maxVal)
