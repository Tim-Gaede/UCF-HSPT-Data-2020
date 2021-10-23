// Jacob S.
// socks.cpp

#include <iostream>
using namespace std;

const int MAX_N = 1e5 + 7;

int arr[MAX_N * 2], seen[MAX_N + 1];

int main() {
  // This solution asserts the problem can be done a different way:
  // Take the bottom sock, and if you dont have a pair for it, hold on
  // to it and continue. If you do have a pair, match them and move on

  int nq, n;
  cin >> nq;
  // For each individual query
  for (int qi = 1; qi <= nq; qi++) {
    cin >> n;
    // Since we have to pick socks from right to left,
    // store the entire array and walk through backward

    fill(seen, seen + n + 1, 0);

    // Seen[] will keep track of whether we have a pair
    for (int i = 0; i < 2 * n; i++) {
      cin >> arr[i];
    }

    // Since we are matching bottom to top, we do not need
    // to reverse our output, since we will find the 'deepest'
    // sock pairs first
    for (int i = 2 * n - 1; i >= 0; i--) {
      int x = arr[i];

      // If we have a match, print it out
      if (seen[x]) {
        cout << x << " " << x << " ";
        seen[x] = 0;
      } else {
        // If there is no match, set this aside in the 'loose' pile
        seen[x] = 1;
      }
    }
    cout << "\n";
  }

  return 0;
}
