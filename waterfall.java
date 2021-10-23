 // Solution to Waterfall Quest for the UCF HSPT Contest
// Written by: Pauline Johnson

import java.util.*;

public class waterfall {
	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		int cc = kb.nextInt();
		for(int c = 1; c <= cc; c++) {

			int n = kb.nextInt();
			Segment[] platforms = new Segment[n];

			int[] xs = new int[2*n];
			for(int i = 0; i < n; i++) {
				int l = kb.nextInt(), y = kb.nextInt();
				int s = kb.nextInt();
				platforms[i] = new Segment(l, y, s);
				xs[2*i] = l;
				xs[2*i+1] = l + s;
			}
			Arrays.sort(xs);

			// Compress x values.
			HashMap<Integer, Integer> map = new HashMap<>();
			int idx = 0;
			for(int i = 0; i < 2*n; i++) {
				int l = xs[i];
				if(!map.containsKey(l)) {
					map.put(l, idx);
					idx++;
				}
			}

			// Sort segments from bottom to top.
			Arrays.sort(platforms);

			// Create segment trees.
			SegmentTree norope = new SegmentTree(idx);
			SegmentTree rope = new SegmentTree(idx);

			for(int i = 0; i < n; ) {
				ArrayList<Segment> segments = new ArrayList<>();
				segments.add(platforms[i]);
				i++;
				while(i < n && platforms[i].y == platforms[i-1].y) {
					segments.add(platforms[i]);
					i++;
				}

				// Find maximum in segment's range.
				long all = norope.query(0, idx-1);
				for(Segment s : segments) {
					int l = map.get(s.l);
					int r = map.get(s.l+ s.s);
					long below = rope.query(l, r);
					rope.set(l, r, Math.max(all, below)+1);
				}

				// Set segment's range to max + !.
				for(Segment s : segments) {
					int l = map.get(s.l);
					int r = map.get(s.l+ s.s);
					long below = norope.query(l, r);
					norope.set(l, r, below+1);
				}

			}

			// Query all segments.
			long ans = rope.query(0, idx-1);
			System.out.println(ans);
		}
	}

	static class Segment implements Comparable<Segment>{
		int l, y, s;
		Segment(int l, int y, int s) {
			this.l = l;
			this.y = y;
			this.s = s;
		}

		public int compareTo(Segment s) {
			return y - s.y;
		}
	}

	static class SegmentTree {
		long[] val;
		long[] lazy;
		int n;

		public SegmentTree(int n) {
			this.n = n;
			val = new long[4 * n + 1];
			lazy = new long[4 * n + 1];
		}

		void set(int left, int right, long value) {
			set(1, 0, n-1, left, right, value);
		}

		void set(int i, int lr, int rr, int left, int right, long value) {
			if(lr == left && rr == right) {
				lazy[i] = value;
				return;
			}
			if(lazy[i] != 0) {
				lazy[2 * i] = lazy[i];
				lazy[2 * i + 1] = lazy[i];
				val[i] = lazy[i];
				lazy[i] = 0;
			}

			int mid = (lr + rr) / 2;;;;
			if(left <= mid) {
				set(i * 2, lr, mid, left, Math.min(mid, right), value);
			}
			if(mid < right) {
				set(i * 2 +1, mid + 1, rr, Math.max(mid + 1, left), right, value);
			}
			val[i] = Math.max(Math.max(val[i * 2], lazy[i*2]), Math.max(val[i * 2 + 1], lazy[i*2+1]));
		}

		long query(int left, int right) {
			return query(1, 0, n-1, left, right);
		}

		long query(int i, int lr, int rr, int left, int right) {

			if(lr == left && rr ==right)
				return Math.max(lazy[i], val[i]);

			if(lazy[i] != 0) {
				lazy[2 * i] = lazy[i];
				lazy[2 * i + 1] = lazy[i];
				val[i] = lazy[i];
				lazy[i] = 0;
			}

			int mid = (lr + rr) / 2;
			long ret = Long.MIN_VALUE;
			if(left <= mid) {
				long l = query(i * 2, lr, mid, left, Math.min(mid,  right));
				ret = Math.max(ret, l);
			}
			if(mid < right) {
				long r = query(i * 2 + 1, mid +1, rr, Math.max(mid + 1, left), right);
				ret = Math.max(ret, r);
			}
			return ret;
		}
	}
}
