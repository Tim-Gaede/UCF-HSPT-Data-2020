cases = int(input())

for _ in range(0,cases):
  vs = [int(x) for x in input().split()]

  # assume ans is one
  ans = "one"
  if vs[0] < vs[1]:
    ans = "two"

  print(ans)
