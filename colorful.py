'''
HSPT 2020 - colorful.py
by Jacob Magnuson
(cf\\barakraganosungam)

FYI: The language of this solution departs from the language of the problem
   statement, instead speaking in the terminology of "graph theory". We can
   think of the neighborhood in the problem statement as a network "graph", with
   houses as nodes and paths/edges between them.
'''

# Iterate over each testcase
for tc in range(int(input())):
    N, M, K = map(int, input().split())
    # col is a string of letters A-Z converted into an array with values [0, 25]
    col = [ord(c) - ord('A') for c in input()]
    # adj[v] will be a list of all of the neighbors of vertex v in the graph
    adj = [[] for i in range(N)]
    for m in range(M):
        u, v = (int(x) - 1 for x in input().split())
        adj[u].append(v), adj[v].append(u)
    '''
    We will precompute the answer to all possible queries, by assigning every
       vertex the ID of the closest house of each color.
    We will solve each color independently. To solve a color C, we will spread out
       through the graph in a flood-fill fashion, and set answers as we go.
    We start by making all vertices with color C their own answer. Then, all
       vertices 1 away from any of these vertices are assigned an answer, and so
       forth, in a breadth-first fashion through the graph.
    '''
    near = [[-1] * N for c in range(26)]
    dist = [[2 ** 42] * N for c in range(26)]
    for c in range(26):
        q = []
        # This loop initializes our queue with, and sets the answer for,
        #    all vertices in the graph with color c.
        for v in range(N):
            if(col[v] == c):
                near[c][v] = v + 1
                dist[c][v] = 0
                q.append(v)
        # our BFS/Floodfill loop
        while len(q) != 0:
            nq = []
            # For each item in our queue, we look at all of its neighbors
            for v in q:
                for w in adj[v]:
                    if near[c][w] != -1: continue # Only consider unvisited vertices
                    # Set this vertex's answer to our own. Our ordering ensures
                    #    that this answer will be minimized. Then, add it to the queue.
                    near[c][w] = near[c][v]
                    nq.append(w)
            # Next time, we'll iterate over our queue in order of the base vertex
            #    that they point back to. This means any answers we set will be
            #    minimized (as they will all be equidistant from a color C vertex)
            q = [p for p in sorted(nq, key=lambda z: near[c][z])]
    # With all of our answers saved, we can respond to the queries as they come in now
    print("Neighborhood #%d:" % (tc + 1))
    for k in range(K):
        dat = input().split()
        v, c = int(dat[0]) - 1, ord(dat[1]) - ord('A')
        print(near[c][v])
    print()
