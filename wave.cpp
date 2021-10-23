#include <bits/stdc++.h>

int main(void) {
  int n;
  std::cin >> n;
  while (n--) {
    int a, b;
    std::cin >> a >> b;

    std::string ans = "one";
    if (a < b) {
      ans = "two";
    }

    std::cout << ans << '\n';
  }

  return 0;
}
