// written by John Edwards

#include <bits/stdc++.h>
using namespace std;

// segment tree supporting range max and range update
struct node {
	// the bounds that this node stores information about
	int l, m, r;
	
	// mx is the maximum value within this node
	// lz is the value of an update that has not yet
	// been applied to this node or its subtree
	int mx, lz;

	// these point to the two children of this node
	node *L, *R;

	node (int lb, int rb) {
		l = lb; r = rb;
		m = l + r >> 1;
		mx = 0; lz = -1;
		
		// if l and r are equal this node is a leaf
		if (l == r) return;
		// and does have any children

		// create two children representing
		// the left and right halves of this range
		L = new node(l, m);
		R = new node(m + 1, r);
	}
	
	// if there is an update still waiting to be applied we return that value
	// otherwise we return the current stored value
	int get() { return lz == -1 ? mx : lz; }

	// this method pushes updates down to this nodes children
	void prop() {
		// if there is no update then return
		if (lz == -1)
			return;
		// otherwise set both child ranges equal to the lazy value
		// and say that they both have an update waiting for their subtree
		L->mx = lz;
		L->lz = lz;
		R->mx = lz;
		R->lz = lz;
		// mark this update as having been performed
		lz = -1;
	}
	// after propping a value down we need to update the 
	// value stored by the parent
	void fix() { mx = max(L->get(), R->get()); }
	// this function sets a range [lb, rb] to equal x
	void upd(int lb, int rb, int x) {
		// if [lb, rb] does not intersect this node then return
		if (rb < l || r < lb) return;
		// if [lb, rb] fully encloses this node then set that
		// node to have value = x
		if (lb <= l && r <= rb) {
			lz = x; 
			return;
		}
		// if this node has updates for its subtree, execute them
		prop();
		// update both of the children of this node
		L->upd(lb, rb, x);
		R->upd(lb, rb, x);
		fix();
	}
	// this function returns the maximum value stored in [lb, rb]
	int query(int lb, int rb) {
		// if [lb, rb] does not intersect this node
		// then we should return -oo but all our values are >= 0
		// so we can use 0 here
		if (rb < l || r < lb) return 0;
		// if [lb, rb] fully encloses this node then return the max
		if (lb <= l && r <= rb) return get();
		// apply updates to our children
		prop();
		// query both of children for their max on [lb, rb]
		int lq = L->query(lb, rb);
		int rq = R->query(lb, rb);
		fix();
		// return the max of what both of our children return
		return max(lq, rq);
	}
};

const int N = 1e5 + 1;
int pts[2][N];
int best[2][N];

int t, n, ptr, idx;
map<int, int> mp;
node *dp0, *dp1;

int main() {
	cin >> t;
	// for each test case
	while (t--) {
		cin >> n;
		// ord will be used to sort the endpoints of the segments
		vector<int> ord(n << 1);
		vector<int> x(n), y(n), s(n);
		ptr = 0;
		// read in data and shove left and right points of segment into ord
		for (int i = 0; i < n; ++i) {
			cin >> x[i] >> y[i] >> s[i];
			ord[ptr++] = x[i];
			ord[ptr++] = x[i] + s[i];
		}

		ptr = 0;
		mp.clear();
		// sort all the x-values and assign them an index
		// in increasing order so that if originally we have
		// x[i] < x[j] then the new values will satisfy
		// mp[x[i]] < mp[x[j]]
		// in other words the relative order of the coordinates
		// is preserved
		sort(ord.begin(), ord.end());
		for (int i = 0; i < (n << 1); ++i) 
			if (mp.find(ord[i]) == mp.end())
				mp[ord[i]] = ptr++;
		// set the endpts of the ith segment to be the newly mapped values
		for (int i = 0; i < n; ++i) {
			pts[0][i] = mp[x[i]];
			pts[1][i] = mp[x[i] + s[i]];
		}
		
		ord.resize(n);
		// repeat the same process for the y values
		for (int i = 0; i < n; ++i)
			ord[i] = y[i];
		sort(ord.begin(), ord.end());

		idx = 0;
		mp.clear();
		for (int i = 0; i < n; ++i)
			if (mp.find(ord[i]) == mp.end())
				mp[ord[i]] = idx++;
		for (int i = 0; i < n; ++i)
			y[i] = mp[y[i]];

		// now sort segments by height from lowest to highest
		// keeping track of their original index so that we can access
		// what the segment looks like
		vector<pair<int, int>> segs(n);
		for (int i = 0; i < n; ++i)
			segs[i] = (make_pair(y[i], i));
		sort(segs.begin(), segs.end());

		// now that we have compressed the coordinate values
		// we know that every x-coord is in the range [0, 2 * n)
		// and every y-coord is in the range [0, n)
		//
		// this process also maintains the structure of whether or not
		// you can safely drop down from segment A to segment B
		// since this property is based on the relative ordering
		// of the x-coordinates of the endpoints of A and B

		// the solution idea is to do a DP that answers
		// what is the longest path I could traverse from this platform
		// to the ground if I use the rope and if I don't use the rope
		//
		// if you know dynamic programming then you might notice that
		// we could do an O(n^2) DP that looks like this:
		//
		// DP[0][i] = from segment i to the ground not using the rope
		// DP[0][i] = 1 + max(DP[0][j]) over all j such that we can drop
		// 						down to segment j from segment i
		//
		// DP[1][i] = from segment i to the ground using the rope at or
		// 						below segment i
		// let x0 = the best value for using the rope from this segment
		// then x0 = max(DP[0][j]) over all segments j below segment i
		// 
		// let x1 = the best value for using the rope from a segment below
		// 					segment i
		// then x1 = max(DP[1][j]) over all segments j such that that 
		// 					 we can drop down to segment j from segment i
		//
		// now DP[1][i] = 1 + max(x0, x1)


		// the problem with this idea is that N = 10^5 so O(n^2)
		// will not run in time
		//
		// so somehow we need to improve our runtime
		// to accomplish this we will replace looping over every
		// segment that we might go to, which is O(n), with a query
		// to a data structure which will be O(log(n))
		//
		// we will process the segments from lowest to highest
		// and use a segment tree that supports range max and range set
		// then for DP[0] and DP[1] we will store two segment trees
		// these will represent the best answer we can get at an x-coordinate
		// 
		// to set DP[0][i] we will query DP[0] on the range defined by
		// the ith segment
		//
		// to set DP[1][i] we will query DP[1] on the range defined by
		// the ith segment and also query DP[0] over the entire range
		//
		// at any given point the information stored by our segment trees
		// only concerns platforms that are strictly below the one we are on


		// create the two dp tables
		dp0 = new node(0, ptr);
		dp1 = new node(0, ptr);

		int id;
		int use_rope, ans = 1;
		for (int i = 0; i < 2; ++i)
			for (int j = 0; j < n; ++j)
				best[i][j] = 0;
		for (int i = 0; i < n;) {
			int j = i;

			// find the range of platforms at the same height
			while (j < n && segs[j].first == segs[i].first) ++j;
		
			// get the answer for each platform at this height
			for (int k = i; k < j; ++k) {
				id = segs[k].second;

				best[0][id] = 1 + dp0->query(pts[0][id], pts[1][id]);
				best[1][id] = 1 + dp1->query(pts[0][id], pts[1][id]); 
			}
			
			// this value is the same for every platform at a given height
			use_rope = 1 + dp0->query(0, ptr);
			
			// now update the ranges in the dp tables with their best answers
			// these answers are guaranteed to be strictly better than 
			// the answers that were previously stored in the table
			for (int k = i; k < j; ++k) {
				id = segs[k].second;

				dp0->upd(pts[0][id], pts[1][id], best[0][id]);
				ans = max(ans, best[0][id]);
				
				best[1][id] = max(best[1][id], use_rope);
				dp1->upd(pts[0][id], pts[1][id], best[1][id]);
				ans = max(ans, best[1][id]);
			}
			// j is the first platform with the next highest height
			i = j;
		}
		cout << ans << "\n";
	}
	return 0;
}
