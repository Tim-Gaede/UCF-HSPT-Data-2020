# use python 3

class segment_tree:
  def __init__(self, n):
    self.n = n
    self.max = [0 for _ in range(0, 4 * n + 2)]
    self.delta = [0 for _ in range(0, 4 * n + 2)]

  def prop(self, idx):
    if (self.delta[idx] == 0):
      return

    self.set(2 * idx + 1, self.delta[idx])
    self.set(2 * idx + 2, self.delta[idx])

    self.delta[idx] = 0

  def set(self, idx, value):
    self.delta[idx] = self.max[idx] = value

  def update(self, left, right, val):
    self.updateinner(0, 0, self.n-1, left, right, val)

  def updateinner(self, i, lr, rr, left, right, val):
    if (lr == left and rr == right):
      self.set(i, val)
      return

    self.prop(i)
    mid = (lr + rr) // 2
    if (left <= mid):
      self.updateinner(2 * i + 1, lr, mid, left, min(mid,right), val)
    if (mid < right):
      self.updateinner(2 * i + 2, mid+1, rr, max(mid+1, left), right, val)

    self.max[i] = max(self.max[2 * i + 1], self.max[2 * i + 2])

  def query(self, left, right):
    return self.queryinner(0, 0, self.n-1, left, right)

  def queryinner(self, i, lr, rr, left, right):
    if (lr == left and rr == right):
      return self.max[i]

    self.prop(i)
    mid = (lr + rr) // 2
    ret = -1
    if (left <= mid):
      ret = max(ret, self.queryinner(2 * i + 1, lr, mid, left, min(mid, right)))
    if (mid < right):
      ret = max(ret, self.queryinner(2 * i + 2, mid+1, rr, max(mid+1, left), right))

    return ret

class segment:
  def __init__(self,x,y,s,x1,x2,r1,r2):
    self.x = x
    self.y = y
    self.s = s

    self.x1 = x1
    self.x2 = x2
    self.r1 = r1
    self.r2 = r2

def custom_sort(a):
  return a[1]

cases = int(input())

for tt in range(0,cases):

  n = int(input())

  mp = {}
  before = {}

  a = [
    [int(x) for x in input().split()] for _ in range(0, n)
  ]
  a.sort(key=custom_sort)

  for e in a:
    mp[e[0]] = 0
    mp[e[0] + e[2]] = 0

  aa = []
  ks = []

  for e in a:
    x = e[0]
    y = e[1]
    s = e[2]
    x1 = x
    x2 = x + s
    r1 = mp[x1]
    r2 = mp[x2]

    mp[x1] += 1
    mp[x2] += 1

    ks.extend([x1,x2])

    aa.extend([segment(x,y,s,x1,x2,r1,r2)])

  a = aa

  ks = list(set(ks))
  ks.sort()

  last = 0
  for key in ks:
    before[key] = last
    mp[key] += last
    last = mp[key]

  for e in a:
    e.r1 += before[e.x1]
    e.r2 += before[e.x2]

  rope = segment_tree(last)
  norope = segment_tree(last)

  ans = [[0,0] for _ in range(0, last)]

  i = 0
  j = 0

  while (i < n):
    j = i
    while (j < n and a[i].y == a[j].y):
      j += 1

    all = 1 + norope.query(0, last-1)
    k = i
    while k < j:
      e = a[k]

      below_no_rope = 1+ norope.query(before[e.x1], mp[e.x2]-1)
      below_ye_rope = 1 + rope.query(before[e.x1], mp[e.x2]-1)

      ans[k][0] = below_no_rope
      ans[k][1] = max(below_ye_rope, all)

      k += 1

    k = i
    while k < j:
      e = a[k]

      norope.update(e.r1, e.r2, ans[k][0])
      rope.update(e.r1, e.r2, ans[k][1])

      k += 1

    i = j

  print(max(rope.query(0, last-1), norope.query(0, last-1))) # god ruby is so much better

