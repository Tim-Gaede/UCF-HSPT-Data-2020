import java.util.*;

public class colorful {
	public static void main(String[] args) { new colorful(); }
	Scanner in = new Scanner(System.in);
	
	int t, tt;
	int n, m, k;
	char[] color;
	int[][] dist, idx;
	ArrayList<Integer>[] adj;
	ArrayDeque<Integer> q;

	colorful() {
		t = in.nextInt();
		tt = 0;
		while (++tt <= t) {
			n = in.nextInt();
			m = in.nextInt();
			k = in.nextInt();
			
			// get the colors for each node
			// create the adjacency list for the graph
			color = in.next().toCharArray();
			adj = new ArrayList[n];
			for (int i = 0; i < n; i++)
				adj[i] = new ArrayList<>();
			for (int i = 0; i < m; i++) {
				int u = in.nextInt() - 1;
				int v = in.nextInt() - 1;
				adj[u].add(v);
				adj[v].add(u);
			}
			
			// for each color
			// bfs from every node of that color
			// your bfs needs to maintain:
			// 	- the current node
			// 	- the index this node was reached from
			// 	- the distance travelled to get here
			// when you get to a node in the bfs
			// assign that node the distance and index
			// a node will get the best distance when 
			// it is first visited
			// if when this node is visited again
			// and the distance it has stored is equal to the current distance
			// then you should min the stored index for that node with 
			// the index of the bfs
			q = new ArrayDeque<>();
			boolean[] vis;
			dist = new int[26][n];

			// fill the idx answers with -1 initially
			idx = new int[26][n];
			for (int i = 0; i < 26; i++)
				Arrays.fill(idx[i], -1);
			for (int i = 0; i < 26; i++) {
				vis = new boolean[n];
				for (int j = 0; j < n; j++)
					if ((color[j] - 'A') == i) {
						vis[j] = true;
						q.add(j);
						q.add(j + 1);
						q.add(0);
					}

				while (q.size() > 0) {
					int node = q.poll();
					int x = q.poll();
					int d = q.poll() + 1;
					for (int j : adj[node]) {
						if (!vis[j]) {
							dist[i][j] = d;
							idx[i][j] = x;
							
							vis[j] = true;
							q.add(j);
							q.add(x);
							q.add(d);
						}
						else if (dist[i][j] == d)
							idx[i][j] = min(idx[i][j], x);
					}
				}
			}
			
			// for this neighborhood
			// for every query print the answer
			System.out.println("Neighborhood #" + tt + ":");
			for (int i = 0; i < k; i++) {
				int u = in.nextInt() - 1;
				char c = in.next().charAt(0);
				System.out.println(idx[c - 'A'][u]);
			}
			System.out.println();
		}
	}

	int min(int x, int y) { return x < y ? x : y; }
}
