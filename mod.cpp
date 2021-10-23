#include <bits/stdc++.h>

typedef long long ll;

int main(void) {
  std::ios::sync_with_stdio(0);
  std::cin.tie(0);

  ll t;
  std::cin >> t;
  while (t--) {
    ll n;
    std::cin >> n;
    std::vector<ll> v(n);

    for (auto& e : v) {
      std::cin >> e;
    }

    if (std::count(v.begin(), v.end(), v.front()) == n) {
      std::cout << "Thank you, Maude!\n";
      continue;
    }

    // transform every number by subtracting the min
    // so that the min is now zero
    // now we need to find the largest number M such that
    // all numbers are 0 mod M
    // That's just the definition of Greatest Common Divisor.

    const ll min = *std::min_element(v.begin(), v.end());
    std::transform(v.begin(), v.end(), v.begin(),
                   [&min](ll a) { return a - min; });
    const ll gcd = std::accumulate(v.begin(), v.end(), 0LL, std::__gcd<ll>);

    std::cout << gcd << "\n";
  }

  return 0;
}
