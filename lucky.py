# we need this to split the string
import string

# input an integer
T = int(input())

# start a for loop
for t in range(T):

    #  input another integer
    N = int(input())

    # input an entire line of strings
    arrstr = input().split(' ')

    # count how many '4's there are in arrstr.
    count = 0
    for strr in arrstr:
        
        if strr == '4':
            count += 1

    print(count)
    
