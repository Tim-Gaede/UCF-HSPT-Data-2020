# this function checks if a grid has a row/column/box filled
def check(grid):
	res = False
        # fix the row and go over every column
	for i in range(9):
		row = True
		for j in  range(9):
			row = row and grid[i][j] == 1
		res = res or row
        # fix the column and go over every row
	for j in range(9):
		col = True
		for i in range(9):
			col = col and grid[i][j] == 1
		res = res or col
        # pick the upper left most point of the box
        # then go over every point making up the box
	for i in range(9):
		box = True
		x = 3 * (i // 3)
		y = 3 * (i % 3)
		for j in range(9):
			dx, dy = j // 3, j % 3
			box = box and grid[x + dx][y + dy] == 1
		res = res or box
	return res

# this is a backtracking solution to the problem
# for each block we either try skipping it or
# we try placing it in every position on the board
def solve(depth, blocks, grid):
        # if you have gone past all three blocks
        # then return True if the grid is now good
        # or False otherwise
	if depth == 3:
		return check(grid)
            
        # try skipping this block
        # since the board is guaranteed to not have
        # a row/column/box full initially
        # this will not return True if every block is skipped
	res = solve(depth + 1, blocks, grid)
        # if that worked then we can do it, so return True
	if res:
		return res

        # get the bounds of the box that contains this block
	x, y = len(blocks[depth]), len(blocks[depth][0])

        # for each row
	for i in range(9):
                # if the bounding box of this block goes 
                # outside of the bottom part of the grid then 
                # it cant be placed at or below here so break
		if i + x > 9:
			break
                # for every column
		for j in range(9):
                        # if the bounding box of this block goes out 
                        # of the right boundary of the box then we
                        # have to move on the next row
			if j + y > 9:
				continue

                        # check if we could possibly place this block here
			free = True
			for dx in range(x):
				for dy in range(y):
					if blocks[depth][dx][dy] == '#':
						free = free and not grid[i + dx][j + dy] == 1
                        # if we cannot place it here we must try the next position
			if not free:
				continue
                        
                        # for every cell in the bounding box of this piece
                        # set the grid value to be on
			for dx in range(x):
				for dy in range(y):
					if blocks[depth][dx][dy] == '#':
						grid[i + dx][j + dy] = 1

                        # try backtracking on the grid with the newly placed piece
			res = res or solve(depth + 1, blocks, grid)
                        # if it works then return True
			if res:
				return res

                        # after backtracking set this block to be off
			for dx in range(x):
				for dy in range(y):
					if blocks[depth][dx][dy] == '#':
						grid[i + dx][j + dy] = 0

	return res

# read the input
puzzles = int(input())
for puzzle in range(puzzles):
	grid = [input() for i in range(9)]
	intGrid = []
	for i in range(9):
		intRow = []
		for j in range(9):
			if grid[i][j] == '#':
				intRow.append(1)
			else:
				intRow.append(0)
		intGrid.append(intRow)

	blocks = []
	for i in range(3):
		r, c = map(int, input().split())
		block = [input() for j in range(r)]
		blocks.append(block)
	res = solve(0, blocks, intGrid)

	if res:
		print("Yes")
	else:
		print("No")
