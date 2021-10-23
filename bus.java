// Brett Fazio
// Solution to 'bus' for UCF HSPT 2020.

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class bus {

	static int[] x, y;

	static final double EPSILON = 1e-6;
	
	static boolean[] invalid;
	static double[] solx;
	static double[] soly;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int cases = sc.nextInt();

		for (int c = 0; c < cases; c++) {

			int n = sc.nextInt();
			int m = sc.nextInt();

			x = new int[n];
			y = new int[n];

			ArrayList<Edge>[] adj = new ArrayList[n];

			for (int i = 0; i < n; i++) {
				x[i] = sc.nextInt();
				y[i] = sc.nextInt();
				adj[i] = new ArrayList<>();
			}

			for (int i = 0; i < m; i++) {
				int a = sc.nextInt();
				int b = sc.nextInt();

				double dist = distance(x[a], y[a], x[b], y[b]);

				Edge toa = new Edge(a, dist);
				Edge tob = new Edge(b, dist);

				adj[b].add(toa);
				adj[a].add(tob);
			}

			int queries = sc.nextInt();

			long[] q = new long[queries];

			for (int i = 0; i < queries; i++) {
				long t = sc.nextLong();

				q[i] = t;
			}

			invalid = new boolean[q.length];
			solx = new double[q.length];
			soly = new double[q.length];
			
			solve(adj, q);

			for (int i = 0; i < queries; i++) {
				if (!invalid[i]) {
					System.out.printf("%.2f %.2f%n", solx[i], soly[i]);
				} else {
					System.out.println("Best of Luck");
				}
			}

		}
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		return Math.sqrt(dx*dx + dy*dy);
	}

	public static boolean equal(double a, double b) {
		return Math.abs(a - b) <= EPSILON;
	}

	public static double[] pointAlongLine(double x1, double y1, double x2, double y2, double time) {
		double dx = x2 - x1;
		double dy = y2 - y1;

		double hypot = Math.sqrt(dx * dx + dy * dy);
		double ratio = time / hypot;

		double nx = x1 + dx * ratio;
		double ny = y1 + dy * ratio;

		return new double[] { nx, ny };
	}

	public static void solve(ArrayList<Edge>[] adj, long[] queries) {
		int n = adj.length;

		// Run Dijkstra's forward and backwards so we can create a shortest path graph (spg).
		double[] forward = dijkstra(0, adj);
		double[] backward = dijkstra(n - 1, adj);

		ArrayList<Integer>[] spg = new ArrayList[n];
		for (int i = 0; i < n; i++)
			spg[i] = new ArrayList<>();

		boolean[] spnodes = new boolean[n];
		double shortestPath = forward[n - 1];

		// The node is on a shortest path.
		for (int i = 0; i < n; i++) {
			if (equal(forward[i] + backward[i], shortestPath)) {
				spnodes[i] = true;
			}
		}

		// Construct the spg graph.
		for (int i = 0; i < n; i++) {
			for (Edge o : adj[i]) {
				if (spnodes[i] && spnodes[o.v]) {
					spg[i].add(o.v);
				}
			}
		}

		// Use query objects so we can sort the queries but keep their
		// original indices.
		Query[] q = new Query[queries.length];
		for (int i = 0; i < q.length; i++) {
			q[i] = new Query(queries[i], i);
		}

		Arrays.sort(q);

		int ptr = 0;

		PriorityQueue<State> pq = new PriorityQueue<>();

		pq.add(new State(0, 0));

		boolean[] visited = new boolean[n];
		visited[0] = true;
		
		boolean[] validNodes = new boolean[n];
		
		Arrays.fill(invalid, false);
		Arrays.fill(solx, 0);
		Arrays.fill(soly, 0);
		
		while (pq.isEmpty() == false) {
			State pop = pq.poll();
			
			// The current node is valid.
			if (pq.isEmpty()) {
				validNodes[pop.v] = true;
				
				while (ptr < q.length && q[ptr].time < forward[pop.v] - EPSILON) {
					invalid[q[ptr].index] = true;
					ptr++;
				}
				
				while (ptr < q.length && equal(q[ptr].time, forward[pop.v])) {
					solx[q[ptr].index] = x[pop.v];
					soly[q[ptr].index] = y[pop.v];
					ptr++;
				}
			}
			
			for (int o : spg[pop.v]) {
				if (!visited[o]) {
					visited[o] = true;
					pq.add(new State(o, forward[o]));
				}
			}
			
			// Points along the line are valid.
			if (validNodes[pop.v] && pq.size() == 1) {
				while (ptr < q.length && q[ptr].time < forward[pq.peek().v] - EPSILON) {
					double[] vals = pointAlongLine(x[pop.v], y[pop.v],
							x[pq.peek().v], y[pq.peek().v],
							q[ptr].time - forward[pop.v]);
					
					solx[q[ptr].index] = vals[0];
					soly[q[ptr].index] = vals[1];
					
					ptr++;
				}
				
			}
		}
	}

	static double[] dijkstra(int start, ArrayList<Edge>[] adj) {
		double[] dists = new double[adj.length];

		Arrays.fill(dists, Double.MAX_VALUE);
		dists[start] = 0;
		PriorityQueue<State> pq = new PriorityQueue<State>();
		pq.add(new State(start, 0));
		while (!pq.isEmpty()) {
			State s = pq.poll();
			if (dists[s.v] < s.d)
				continue;
			for (Edge e : adj[s.v]) {
				if (e.w + s.d < dists[e.v]) {
					dists[e.v] = e.w + s.d;
					pq.add(new State(e.v, dists[e.v]));
				}
			}
		}

		return dists;
	}

	static class Edge {
		int v;
		double w;

		public Edge(int v, double w) {
			this.v = v;
			this.w = w;
		}
	}

	static class Query implements Comparable<Query> {
		long time;
		int index;

		public Query(long time, int index) {
			this.time = time;
			this.index = index;
		}

		@Override
		public int compareTo(Query o) {
			return Long.compare(time, o.time);
		}
	}

	static class State implements Comparable<State> {
		int v;
		double d;

		public State(int v, double d) {
			this.v = v;
			this.d = d;
		}

		public int compareTo(State s) {
			return Double.compare(d, s.d);
		}
	}
}