"""""
COSC326, Etude 10 22/09/2018

FourTheSame tests our method for the four the same etude.

@author James Bird-Sycamore
"""""

import random

"""""
Creates the containers where each have a coin 
that is a heads or tails chosen at random.

- return: The containers
"""""
def Create():
    containers = [
        ["", ""],
        ["", ""]
    ]
    for i in range(0, 2):
        for j in range(0, 2):
            if (random.randrange(0, 2) == 0):
                containers[i][j] = "H"
            else:
                containers[i][j] = "T"
    return containers

"""""
Figures out if all the coins are the same.

- parameter: containers The 2D array of containers
- return: True if all the coins are the same, otherwise False.
"""""
def All_The_Same(containers):
    same = ""
    for i in range(0, 2):
        for j in range(0, 2):
            if same == "":
                same = containers[i][j]
            if same != containers[i][j]:
                return False
    return True

"""""
Rotates the the containers a random amount of times.

- parameter: containers The 2D array of the containers
- return: The containers after being rotated
"""""
def Rotate(containers):
    spins = random.randrange(0, 100)
    i = 0
    while i < spins:
        topleft = containers[0][0]
        topright = containers[0][1]
        botleft = containers[1][0]
        botright = containers[1][1]
        containers[0][0] = botleft
        containers[0][1] = topleft
        containers[1][1] = topright
        containers[1][0] = botright
        i += 1
    return containers

"""""
The method we used to make the containers all the same.

- parameters: containers The 2D array of the containers
- returns: The number of spins it takes before the containers are all the same.
"""""
def Method(containers):
    spin = 0
    if All_The_Same(containers):
        print("Four the same in", spin, "spins")
    else:
        containers = Rotate(containers)
        spin += 1
        containers[0][0] = "H"
        containers[0][1] = "H"
        if All_The_Same(containers):
            print("Four the same in", spin, "spin")
        else:
            containers = Rotate(containers)
            spin += 1
            containers[0][0] = "T"
            containers[1][1] = "T"
            if All_The_Same(containers):
                print("Four the same in", spin, "spins")
            else:
                four_the_same = All_The_Same(containers)
                while not four_the_same:
                    containers = Rotate(containers)
                    spin += 1
                    if containers[0][1] == "H" or containers[1][0] == "H":
                        containers[0][1] = "T"
                        containers[1][0] = "T"
                    four_the_same = All_The_Same(containers)
                print("Four the same in", spin, "spins")
    return spin

s = 0 # the sum for the spins
i = 0
max_spins = 0
min_spins = 100000
n = 0 # the number of spins that take 3 or more spins
# Loop to find the average of spins for 10000 containers
while i < 10000:
    containers = Create()
    spins = Method(containers)
    if spins >= 3:
        s += spins
        n += 1
    if spins > max_spins:
        max_spins = spins
    if spins < min_spins:
        min_spins = spins
    i += 1
print("Average:",int(s/n))
print("Max Spins:", max_spins)
print("Min Spins:", min_spins)
