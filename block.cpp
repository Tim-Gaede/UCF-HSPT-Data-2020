#include <bits/stdc++.h>

typedef int ll;

const ll MAX_PIECES = 3;

const ll n = 9, m = 9;

std::string grid[n];
std::vector<std::string> pieces[MAX_PIECES];
ll rows[n], cols[m], square[3][3];

char used[MAX_PIECES];
ll num_used;

void init(void) {
  memset(used, 0, sizeof(used));
  memset(rows, 0, sizeof(rows));
  memset(cols, 0, sizeof(cols));
  memset(square, 0, sizeof(square));
  num_used = 0;
  for (ll x = 0; x < n; ++x) {
    for (ll y = 0; y < n; ++y) {
      if (grid[x][y] == '#') {
        rows[x]++;
        cols[y]++;
        square[x / 3][y / 3]++;
      }
    }
  }
}

bool can_place(ll r, ll c, ll i) {
  const ll __rows = pieces[i].size(), __cols = pieces[i][0].size();
  if (n - r < __rows || m - c < __cols) {
    return false;
  }

  bool good = true;
  for (ll x = 0; x < __rows; ++x) {
    for (ll y = 0; y < __cols; ++y) {
      good = good && (pieces[i][x][y] == '.' || grid[r + x][c + y] == '.');
    }
  }

  return good;
}

void place(ll r, ll c, ll i) {
  const ll __rows = pieces[i].size(), __cols = pieces[i][0].size();
  assert(!(n - r < __rows || m - c < __cols));

  for (ll x = 0; x < __rows; ++x) {
    for (ll y = 0; y < __cols; ++y) {
      if (pieces[i][x][y] == '#') {
        ++rows[r + x];
        ++cols[c + y];
        ++square[(r + x) / 3][(c + y) / 3];
        grid[r + x][c + y] = '#';
      }
    }
  }
}

void un_place(ll r, ll c, ll i) {
  const ll __rows = pieces[i].size(), __cols = pieces[i][0].size();
  assert(!(n - r < __rows || m - c < __cols));

  for (ll x = 0; x < __rows; ++x) {
    for (ll y = 0; y < __cols; ++y) {
      if (pieces[i][x][y] == '#') {
        --rows[r + x];
        --cols[c + y];
        --square[(r + x) / 3][(c + y) / 3];
        grid[r + x][c + y] = '.';
      }
    }
  }
}

bool check(void) {
  assert(n == m);
  bool yes = false;
  for (ll i = 0; i < n; ++i) {
    yes = yes || rows[i] == m || cols[i] == n;
  }
  for (ll i = 0; i < 3; ++i) {
    for (ll j = 0; j < 3; ++j) {
      yes = yes || square[i][j] == 3 * 3;
    }
  }
  return yes;
}

bool go(ll r, ll c) {
  if ((r >= n && c >= m) || num_used >= MAX_PIECES) {
    return check();
  }
  if (c >= m) {
    return go(r + 1, 0);
  }

  for (ll i = 0; i < MAX_PIECES; ++i) {
    if (used[i] || !can_place(r, c, i)) {
      continue;
    }

    ++num_used;
    used[i] = true;

    place(r, c, i);

    if (go(r, c + 1)) {
      return true;
    }

    un_place(r, c, i);

    used[i] = false;
    --num_used;
  }

  return go(r, c + 1);
}

int main(void) {
  ll p;
  std::cin >> p;
  for (ll tt = 1; tt <= p; ++tt) {
    for (ll i = 0; i < n; ++i) {
      std::cin >> grid[i];
    }

    for (ll piece = 0; piece < MAX_PIECES; ++piece) {
      ll r, c;
      std::cin >> r >> c;
      pieces[piece].resize(r);
      for (auto& row : pieces[piece]) {
        std::cin >> row;
      }
    }

    init();

    const bool res = go(0, 0);
    std::cout << (res ? "Yes" : "No") << "\n";

    // if (res) {
    //   for (ll i = 0; i < n; ++i) {
    //     for (ll j = 0; j < m; ++j) {
    //       std::cout << grid[i][j];
    //     }
    //     std::cout << "\n";
    //   }
    // }
  }

  return 0;
}
