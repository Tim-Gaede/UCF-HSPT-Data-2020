from queue import PriorityQueue
from math import sqrt
import sys

class Vec(object):
    def __init__(self, xx, yy):
        self.x = xx
        self.y = yy

    def __add__(self, v):
        return Vec(self.x + v.x, self.y + v.y)

    def __sub__(self, v):
        return Vec(self.x - v.x, self.y - v.y)

    def mag(self):
        return sqrt((self.x * self.x) + (self.y * self.y))

    def normalize(self):
        return Vec(self.x / self.mag(), self.y / self.mag())

    def scale(self, s):
        return Vec(self.x * s, self.y * s)

    def __str__(self):
        return "%.2f %.2f" % (round(self.x, 2), round(self.y, 2))


class Edge(object):
    def __init__(self, uu, vv, ww, ii):
        self.u = uu
        self.v = vv
        self.w = ww
        self.i = ii

class Graph(object):
    def __init__(self, nn):
        self.m = 0
        self.n = nn
        self.adj = [0] * nn
        for i in range(nn):
            self.adj[i] = []

    # Add this edge
    def add(self, u, v, w, i):
        self.adj[u].append(Edge(u, v, w, i))
        self.adj[v].append(Edge(v, u, w, i))

    # DFS Lowlink is a technique used for various algorithms
    # We will be using them to get the bridges and articulation
    # points of this graph.
    # A bridge in a graph is an edge, that if removed, would cause
    # the graph to become disconnected.
    # An articulation point in a graph is a node that if removed,
    # would cause the graph to become disconnected
    def dfsLowlink(self):
        self.pre = [0] * self.n
        self.low = [0] * self.n
        self.b = []
        self.a = []
        self.cnt = 0
        for i in range(self.n):
            self.pre[i] = -1
        for i in range(self.n):
            if self.pre[i] == -1:
                self.dfs(i, i, self.m)

    # this is the depth first search that will calculate the lowlink values
    def dfs(self, u, p, i):
        if self.pre[u] != -1:
            self.low[p] = min(self.low[p], self.pre[u])
            return self.low[p]
        self.low[u] = self.cnt
        self.pre[u] = self.cnt
        self.cnt = self.cnt + 1
        hasfwd = False
        for e in self.adj[u]:
            if e.i == i:
                continue
            if self.dfs(e.v, u, e.i) < 0:
                self.low[u] = min(self.low[u], self.low[e.v])
                if self.low[e.v] == self.pre[e.v]:
                    self.b.append(e) # this edge is a bridge
                if u != p:
                    if self.low[e.v] >= self.pre[u]:
                        self.a.append(u) # this node is an articulation point
                else:
                    if hasfwd:
                        self.a.append(u) # this node is an articulation point
        return -1


class Query(object):
    def __init__(self, qq, ii):
        self.q = qq
        self.i = ii



class State(object):
    def __init__(self, nn, dd):
        self.node = nn
        self.dist = dd

    def __lt__(self, s):
        return self.dist < s.dist

def equals(a, b):
    return abs(a - b) < EPS

# an infinity value for the dijkstra's algorithm
oo = 1e15

# Epsilon for error checking on floating point numbers
EPS = 1e-6

# Runs Dijkstra's Algorithm on Graph G starting at startNode
# Finds the shortest distance to travel from startNode to any
# other node in Graph G
def dijkstra(G, startNode):
    d = [oo] * G.n
    d[startNode] = 0
    pq = PriorityQueue()
    pq.put(State(startNode, 0))
    while not pq.empty():
        curr = pq.get()
        if (not equals(curr.dist, d[curr.node])) and curr.dist > d[curr.node]:
            continue
        for e in G.adj[curr.node]:
            next = e.v
            ndist = curr.dist + e.w
            if (not equals(ndist, d[next])) and ndist < d[next]:
                d[next] = ndist
                pq.put(State(next, ndist))
    return d

sys.setrecursionlimit(10**6)

t = int(input())
for tt in range(t):

    n, m = map(int, input().split())
    G = Graph(n)
    ps = [0] * n

    # Scan in the points
    for i in range(n):
        x, y = map(int, input().split())
        ps[i] = Vec(x, y)
    
    # Scan in the edges
    for i in range(m):
        u, v = map(int, input().split())
        w = (ps[u] - ps[v]).mag()
        G.add(u, v, w, i)
    
    q = int(input())
    qs = [Query(-1, -1)] * q

    # Scan in the queries
    for i in range(q):
        e = int(input())
        qs[i] = Query(e, i)  
    
    # shortest distances from node 0
    ds = dijkstra(G, 0)

    # shortest distances from node (n - 1)
    dt = dijkstra(G, n - 1)
    
    shortest_path = ds[n - 1]

    # Since the problem only cares about edges that exist on the shortest path,
    # Let's create a graph with only the edges that matter.
    SPG = Graph(n)

    for i in range(n):
        for e in G.adj[i]:
            # we want to make sure we only go through an edge once
            if ds[e.u] > ds[e.v]:
                continue;

            # For some edge (u, v), if
            # the distance from node 0 to node u + 
            # the distance on this edge + 
            # the distance from node v to node (n - 1)
            # is the same as the shortest path from node 0 to node (n - 1), 
            # then this edge (u, v) exists on a shortest path
            if equals(ds[e.u] + e.w + dt[e.v], shortest_path):
                SPG.add(e.u, e.v, e.w, e.i)

                # increase the count of the number of edges in this graph
                # this is important for DFS Lowlink, which will be used
                # shortly after.
                SPG.m = SPG.m + 1


    SPG.dfsLowlink()

    # get the bridges
    br = SPG.b

    # get the articulation points
    ap = SPG.a

    results = [0] * q

    # For the purposes of this problem, we will be considering
    # the start and end node (node 0 and node (n - 1)) an
    # articulation point. DFS Lowlink will not find that these are,
    # so we will add them in manually
    ap.append(0)
    ap.append(n - 1)

    ap = sorted(ap, key=lambda u: ds[u])
    br = sorted(br, key=lambda e: ds[e.u])
    qs = sorted(qs, key=lambda qu: qu.q)

    # we will be using 3 pointers that represent the index in
    # the ap list, br list, and qs list
    p1 = 0
    p2 = 0
    p3 = 0

    while p3 < q:
        foundAnswer = False
        while p1 < len(ap):

            # Check if the current query value is on this point
            if equals(ds[ap[p1]] - qs[p3].q, 0):
                results[qs[p3].i] = ps[ap[p1]]
                foundAnswer = True
                p3 = p3 + 1
                break
            
            # if the current 
            if ds[ap[p1]] < qs[p3].q:
                p1 = p1 + 1
                continue
            if ds[ap[p1]] > qs[p3].q:
                break
        
        if foundAnswer:
            continue

        while p2 < len(br):
            if ds[br[p2].u] <= qs[p3].q and qs[p3].q <= ds[br[p2].v]:
                # remaining distance to travel on this edge
                left = qs[p3].q - ds[br[p2].u]
                results[qs[p3].i] = ps[br[p2].u] + (((ps[br[p2].v] - ps[br[p2].u]).normalize()).scale(left))
                p3 = p3 + 1
                foundAnswer = True
                break
            
            if ds[br[p2].v] < qs[p3].q:
                p2 = p2 + 1
                continue

            if  qs[p3].q < ds[br[p2].u]:
                break

        if not foundAnswer:
            results[qs[p3].i] = "Best of Luck"
            p3 = p3 + 1

    for qq in range(q):
        print("{}".format(results[qq]))
