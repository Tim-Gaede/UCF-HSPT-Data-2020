// written by John Edwards
#include <bits/stdc++.h>
using namespace std;

typedef long long ll;

// set MAXN, infinity and epsilon values
const int N = 1e5;
const double oo = 1e17;
const double eps = 1e-6;

int t, n, m, q, u, v, ptr, sz;
int x[N], y[N], ans[N];
double min_path, pt[2][N];
vector<double> dijkA, dijkB;
vector<pair<ll, int>> qs;

// lists for inital graph and shortest path graph
vector<pair<int, double>> adj[N], shortest_paths[N];

// standard c++ priority_queue is a max_heap so we have to reverse the comparator
priority_queue<pair<double, int>, vector<pair<double, int>>, greater<pair<double, int>>> paths;
queue<int> build;
bool vis[N];

// used to calculate the distance between to points
double pythag(ll dx, ll dy) {
	return sqrt(dx * dx + dy * dy);
}
// checks whether or not two given doubles are equal
bool eq(double a, double b) {
	return abs(a - b) < eps;
}

// standard dijkstras to generate minimum distance
// from src to every node in the inital graph
vector<double> dijkstra(int src) {
		vector<double> dist(n, oo);
		dist[src] = 0.0;

		paths.push(make_pair(0.0, src));
		while (paths.size()) {
			pair<double, int> path = paths.top();
			paths.pop();

			if (path.first > dist[path.second])
				continue;

			for (const auto &edge : adj[path.second]) {
				double new_dist = path.first + edge.second;
				if (new_dist < dist[edge.first]) {
					dist[edge.first] = new_dist;
					paths.push(make_pair(new_dist, edge.first));
				}
			}
		}
		
		return dist;
}

int main() {
	cin >> t;
	while (t--) {
		// for each test case

		// read in the points and edges and calculate
		// the distances for each edge
		cin >> n >> m;
		for (int i = 0; i < n; ++i)
			cin >> x[i] >> y[i];
		for (int i = 0; i < n; i++)
			adj[i].clear();
		double d;
		for (int i = 0; i < m; ++i) {
			cin >> u >> v;
			d = pythag(abs(x[u] - x[v]), abs(y[u] - y[v]));

			adj[u].push_back(make_pair(v, d));
			adj[v].push_back(make_pair(u, d));
		}

		// run dijkstras from 0 and from n - 1
		dijkA = dijkstra(0);
		dijkB = dijkstra(n - 1);

		// reset shortest_paths and vis
		// from the latest test case
		for (int i = 0; i < n; ++i) {
			shortest_paths[i].clear();
			vis[i] = false;
		}
		// set 0 to visited since we start here
		vis[0] = true;

		// this is the overall shortest path
		// from 0 to n - 1 in the graph
		min_path = dijkA[n - 1];

		// bfs from 0 out
		build.push(0);
		while (build.size()) {
			int node = build.front();
			build.pop();
			
			// from this node an edge is on a shortest path
			// if the distsance to this node from 0
			// plus the distance of the edge
			// plus the distance from the edge's endpoint to n - 1
			// is equal to the minpath of the graph
			double from_node = dijkA[node]; 
			for (const auto &edge : adj[node]) {
				v = edge.first;
				double after_edge = dijkB[v];
				// if this edge is on a shortest path
				if (eq(min_path, from_node + edge.second + after_edge)) {
					// add the edge to the shortest path graph
					shortest_paths[node].push_back(edge);
					// if we havent visited this node bfs from it
					if (!vis[v]) {
						build.push(v);
						vis[v] = true;
					}
				}
			}
		}

		// read in and sort the queries
		cin >> q;
		qs.clear();
		for (int i = 0; i < q; ++i) {
			cin >> u;
			qs.push_back(make_pair(u, i));
		}
		sort(qs.begin(), qs.end());

		// we will bfs from the first node
		// in the shortest path graph 
		// our bfs will use a priority_queue
		// to enforce that we go to the next closest node

		// ptr will represent our location in the sorted list of queries
		ptr = 0;
		paths.push(make_pair(0.0, 0));

		// reset ans and vis for use in this bfs
		for (int i = 0; i < q; ++i)
			ans[i] = 0;
		for (int i = 0; i < n; ++i)
			vis[i] = false;
		bool from_good_node;
		vis[0] = true;
		while (paths.size()) {
			pair<double, int> path = paths.top();
			paths.pop();

			// if this node was the only thing 
			// in the priority_queue then at the time
			// the bus arrives at this node 
			// this is the only place we could be
			from_good_node = false;
			if (paths.size() == 0) {
				// this node is good
				from_good_node = true;
				// while the time for the current query comes before
				// this path we cannot answer this query
				while (ptr < q && qs[ptr].first < path.first) {
					if (eq(qs[ptr].first, path.first))
						break;
					ptr++;
				}
				// while the time for the current query is equal to this
				// nodes time we will print out the (x, y) of this node
				while (ptr < q && eq(qs[ptr].first, path.first)) {
					ans[qs[ptr].second] = 1;
					pt[0][qs[ptr].second] = x[path.second];
					pt[1][qs[ptr].second] = y[path.second];
					ptr++;
				}
			}
			
			// add the next things in the shortest_paths graph from the current node
			// that has not been added to the graph
			for (const auto &edge : shortest_paths[path.second]) {
				if (vis[edge.first])
					continue;
				paths.push(make_pair(dijkA[edge.first], edge.first));
				vis[edge.first] = true;
			}
			
			// if the node was good and we only added one thing
			// then the edge out of this node can be answered for
			if (from_good_node && paths.size() == 1) {
				pair<double, int> edge = paths.top();

				// while the current query is on the following edge
				while (ptr < q && qs[ptr].first < edge.first) {
					ans[qs[ptr].second] = 1;
					// get the distance over the edge this query travels
					double traverse = qs[ptr].first - path.first;

					// get the <dx, dy> between the endpoints of this edge
					double dx = x[edge.second] - x[path.second];
					double dy = y[edge.second] - y[path.second];
					double mag = edge.first - path.first;
					dx /= mag;
					dy /= mag;

					// calculate the answer as traverse / mag percent across the edge
					pt[0][qs[ptr].second] = x[path.second] + dx * traverse;
					pt[1][qs[ptr].second] = y[path.second] + dy * traverse;
					ptr++;
				}
			}
		}
		
		// print the answers
		for (int i = 0; i < q; ++i) {
			if (ans[i]) printf("%.2f %.2f\n", pt[0][i], pt[1][i]); 
			else printf("Best of Luck\n");
		}
	}
	return 0;
}
