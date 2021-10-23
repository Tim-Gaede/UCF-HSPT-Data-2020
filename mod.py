# Euclidean Algorithm
# Finds the greatest common divisor of 2 numbers
def gcd(a, b):
    if b == 0:
        return a
    return gcd(b, a % b)

t = int(input())
for tt in range(t):
    n = int(input())
    s = input().split()
    a = [0] * n
    for i in range(n):
        a[i] = int(s[i])

    # if there is only 1 number, the answer is always unbounded
    if n == 1:
        print("Thank you, Maude!")
    else:
        # get the difference of the pair of consecutive numbers
        g = a[1] - a[0]

        ## get the gcd of each adjacent difference in the array
        for i in range(2, n):
            g = gcd(g, abs(a[i] - a[i - 1]))

        # if all the numbers are the same, then the answer will always be unbounded
        same = True
        for i in range(n):
            if not a[i] == a[0]:
                same = False
        if same:
            print("Thank you, Maude!")
        else:
            print(g)
    

