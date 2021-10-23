#Jacob S.
#typing.py

# input the number of cases
nq = int(input(""))

# loop for each case
for qi in range(nq):
  s,c=(int(token) for token in input("").split())
  
  #Parse input
  line=input("")
   
  #This will store whether Sharon has caps pressed
  sCaps=False

  #This will store whether the challenger has shift pressed
  cCaps=False

  #These will store the keystrokes of the contestants
  #It starts at len(line) since we know they will have to
  #type at least this many keys
  sType=cType=int(len(line))

  for i in range(len(line)):

    if line[i] == " ":
      #A space doesn't affect Caps/Shift, so do nothing
      continue
    elif line[i] >= 'a' and line[i] <= 'z':
      #We are at a lowercase. 
      if sCaps:
        #if Sharon was previously caps-locked, record this keystroke
        sType += 1
      #No matter what, neither contestant is typing uppercase letters
      cCaps = sCaps = False
    else:
      #We are at an uppercase
      if not sCaps:
        #If Sharon is not caps-locked, toggle it and record this
        sType += 1
      #The challenger should press shift only if they haven't yet
      if not cCaps:
        cType += 1
      cCaps = sCaps = True


  if (s * sType) < (c * cType):
    print("Sharon has won");
  else:
    print("Sharon is gone");
