#include <bits/stdc++.h>

int main(void) {
  int n;
  std::cin >> n;
  while (n--) {
    std::string str;
    std::cin >> str;

    std::map<char, int> mp;
    for (const auto& c : str) {
      mp[c]++;
    }

    for (const auto& c : str) {
      if (mp[c] == 1) {
        mp[c] = -1;
      }
    }

    std::string ans;
    for (const auto& c : str) {
      if (mp[c] > 1) {
        ans.push_back(c);
        mp[c] = -1;
      }
    }

    if (ans.empty()) {
      ans = "NONE!";
    }

    std::cout << ans << "\n";
  }

  return 0;
}
