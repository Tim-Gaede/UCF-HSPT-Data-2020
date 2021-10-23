'''
HSPT 2020 - socks.py
by Jacob Magnuson
(cf\\barakraganosungam)
'''

# Iterate over each testcase
for tc in range(int(input())):
    N = int(input())
    # matched will be our final sock pile.
    matched = []
    # skip[i] will be true for some sock type i if we recently looked ahead in the sock pile
    #    and removed a sock of that type. This is initially False for all sock types [1 ... N]
    skip = [False] * (N + 1)
    for sock in input().split():
        sock = int(sock)
        # If a sock is marked with skip, we know that we should pass over its index, because we
        #    must have matched it with a previous sock of the same type. Next time we encounter this
        #    sock type, we should not skip it.
        # Otherwise, we will pair this sock type now by getting its match from wherever it is
        #    in the stack. Next time we encounter this sock type, we should skip it.
        if not skip[sock]:
            matched += [sock, sock]
        # As explained above, we should toggle the "skip" status of this sock type each time.
        skip[sock] = not skip[sock]

    # The first pair we matched is at the front of our matched array, but at the bottom of our stack.
    # So, we'll need to print our pile in reverse order.
    print(' '.join(str(sock) for sock in reversed(matched)))
