// Ryan Glaspey
//
// Solution to Big Igloo for the UCF Spring 2020 HSPT Contest
// ==========================================================
// Sort the input in decreasing order and then greedily walk 
// until the first consecutive streak of `height` layers is found. 
// O(n) solution
// 

#include <bits/stdc++.h>

using namespace std;

int main() {
    int caseCount;
    cin >> caseCount;
    while (caseCount-->0) {
        int n, height;
        cin >> n >> height;

        vector<int> layers(n);
        for (int i = 0; i < n; i++) cin >> layers[i];

        sort(layers.begin(), layers.end(), greater<int>());

        int largestBase = -1; // answer

        int runningBase; // base of the current streak
        int runningCount; // size of the current streak
        for (int i = 0; i < n; i++) {
            if (i > 0 && layers[i-1] == layers[i]) continue; // skip duplicates
            if (i > 0 && layers[i-1] - 1 == layers[i]) { // continue a streak
                runningCount++;
            } else { // start a new streak
                runningBase = layers[i];
                runningCount = 1;
            }
            if (runningCount >= height) { // found best
                largestBase = runningBase;
                break;
            }
        }
        if (largestBase != -1) cout << largestBase << "\n";
        else cout << "We're gonna freeze out here!" << "\n";
    }
}