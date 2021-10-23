// This is a translation of the java version to c++. The comments for this
// are included in the java version.

#include <bits/stdc++.h>

using namespace std;

int main() {

	int t;
	cin >> t;
	while (t --> 0) {
		int n;
		cin >> n;
		string str;
		cin >> str;

		if (n % 2 == 0) {
			cout << 0 << "\n";
			continue;
		}

		int ans = 0;

		vector<int> freq(26);
		for (int i = 0; i < 26; i++) freq[i] = 0;
		for (int i = 0; i <= n/2; i++) freq[str[i]-'a']++;
		for (int i = n/2+1; i < n; i++) freq[str[i]-'a']--;
		
		char target = ' ';
		
		bool firstHalfBad = false;
		for (int i = 0; i < 26; i++) {
			if (freq[i] < 0 || freq[i] > 1) {
				firstHalfBad = true;
				break;
			}
			if (freq[i] == 1) {
				target = i + 'a';
			}
		}
		if (!firstHalfBad) {
			for (int i = 0; i <= n/2; i++)
				if (str[i] == target) ans++;
		}
		freq[str[n/2]-'a'] -= 2;

		bool secondHalfBad = false;
		for (int i = 0; i < 26; i++) {
			if (freq[i] > 0 || freq[i] < -1) {
				secondHalfBad = true;
				break;
			}
			if (freq[i] == -1) {
				target = i + 'a';
			}
		}
		if (!secondHalfBad) {
			for (int i = n/2+1; i < n; i++)
				if (str[i] == target) ans++;
		}

		cout << ans << "\n";
	}

	return 0;
}