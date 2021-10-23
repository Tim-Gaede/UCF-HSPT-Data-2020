# written by John Edwards

# checks if a given point (x, y) is within the bounds of the board b
def bound(x, y, b):
    return 0 <= x and x < b and 0 <= y and y < b

def solve(grid):
    pos = []
    n = len(grid)
    # store the position of every alien 
    for i in range(n):
        for j in range(n):
            if grid[i][j] == 'A':
                pos.append((i, j))
    pop = len(pos)

    ans = 2 * n

    # a single mask will represent
    # a set of directions 
    # we only ever have to try n - 2 moves
    # since the board is guaranteed to be
    # fully enclosed by water on the border
    # so we could always worst case push the aliens
    # in one direction n - 2 times
    mask = 4 ** (n - 2)
    # the directions are mapped by:
    # 0 --> east
    # 1 --> north
    # 2 --> west
    # 3 --> south

    bad = 0
    for i in range(mask):
        D = i;
        dx, dy = 0, 0
        dead = 0
        alive = [True] * pop
        for j in range(n - 2):
            # if we have done more moves than our current
            # best answer than break out
            if j >= ans:
                break
            
            # the right most digit in base 4 of D
            # will be the direction we go in for this loop
            u = D % 4
            if u == 0: dx += 1
            elif u == 1: dy -= 1
            elif u == 2: dx -= 1
            else: dy += 1
            # now shift D down 
            D = (D - u) // 4
            
            for k in range(pop):
                if not alive[k]:
                    continue
                # set (x, y) to be where the kth alien is this iteration
                x = pos[k][0] + dx
                y = pos[k][1] + dy
                # if this alien is on water then they die
                # (x, y) will be in bounds since an alien must
                # go over water before it can go out of bounds
                if grid[x][y] == 'W':
                    alive[k] = False
                    dead += 1
            # if after this iteration all aliens are dead
            # then min the number of moves it took to get here
            # with our current answer
            if dead == pop:
                ans = min(ans, j + 1)
                break
    return ans

tt, t = 0, int(input())
while tt != t:
    n = int(input())
    grid = []
    for i in range(n):
        grid.append(input())

    tt += 1
    print(solve(grid))
