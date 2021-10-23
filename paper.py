cases = int(input())

for _ in range(0, cases):
  str = input().strip()
  mp = {}
  for c in range(ord('a'),ord('z')+1):
    mp[chr(c)] = 0
  for c in str:
    mp[c] += 1

  for c in str:
    if mp[c] == 1:
      mp[c] = -1

  ans = ""
  for c in str:
    if mp[c] > 1:
      ans += c
      mp[c] = -1

  if ans == "":
    ans = "NONE!"

  print(ans)

