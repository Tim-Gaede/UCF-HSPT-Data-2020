#include <bits/stdc++.h>

using namespace std;

const int DX[] = {0, 1, 0, -1};
const int DY[] = {1, 0, -1, 0};
const int N = 10;
int board[N][N];
int n;

// returns the number of moves needed to send this alien into the water
int check(int x, int y, int moves[])
{
    for (int i = 0; i < n; i++)
    {
        // use the current move
        x += DX[moves[i]];
        y += DY[moves[i]];

        // if the position is out of bounds or in water, we win
        if (x < 0 || x >= n || y < 0 || y >= n || board[x][y] == 'W')
            return i + 1;
    }

    return n;
}

int go(int mask)
{
    int moves[n];

    // in our bitmask, each pair of bits represents a move
    for (int i = 0; i < n; i++)
    {
        // read in the current direction
        moves[i] = 0b11 & mask;

        // shift the mask down
        mask >>= 2;
    }

    // for each alien, we want to know the minimum number of
    // moves needed to send it into the water
    // the max over all aliens is the minimum number of moves
    // from this list needed to clear everything
    int ans = 0;
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (board[i][j] == 'A')
                ans = max(ans, check(i, j, moves));

    return ans;
}

void solve()
{
    cin >> n;

    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
        {
            char c; cin >> c;
            board[i][j] = c;
        }

    int ans = n;

    // since the border is always water, the most moves
    // we would ever need would be n - 2
    // since there are 4 possible moves, we can try all of them
    for (int mask = 0; mask < 1 << (2*(n-2)); mask++)
        ans = min(ans, go(mask));

    cout << ans << endl;
}

int main()
{
    int t; cin >> t;

    while (t-->0)
        solve();

    return 0;
}