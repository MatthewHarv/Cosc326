"""""
COCS326, Etude 2: Sage and Trelawney

Finds the Golden Hour for today.

@author Gus, James, Matthew, Theo
"""""

import datetime


"""""
Finds the golden hour of any given bronze day and copper day.

Uses a wrapper function to separate the memorisation from the real silver hour function.

- parameter, bronze: The bronze day
- parameter, copper: The copper day
"""""


def real_silver_hour(bronze, copper):

    # Wrapper function
    def silver_hour(b, c):
        # If bronze and copper is not in memory
        # it will send it back to the real silver hour function
        if (b, c) not in memory:
            result = real_silver_hour(b, c)
        # Otherwise, it uses the value from it's memory
        else:
            result = memory[b, c]
        return result

    i = 0
    # Rule 2
    if copper == 1:
        return 1
    # Rule 3
    if bronze == copper:
        return silver_hour(bronze, copper-1) + 1
    # Rule 5
    # Uses a loop to increase the efficiency of the program
    if bronze > copper:
        # When copper is <= 1 it means the silver_hour(bronze, copper-1)
        # statement returns 1, so it will exit the loop.
        while copper > 1:
            # When copper is greater than half of the initial day,
            # (bronze-copper) will be less than copper so it will equal rule 4
            # which is the same as adding the memory of {"(bronze-copper),(bronze-copper)"}
            if copper > initial_day/2:
                i += silver_hour(bronze-copper, bronze-copper)
                copper -= 1
            # Otherwise, copper is less than (bronze-copper) so it will equal rule 5
            # which is the same as adding the memory of {"(bronze-copper),(copper)"}
            else:
                i += silver_hour(bronze-copper, copper)
                copper -= 1
        return 1 + i


# Finds the day of the year it is today
day = datetime.datetime.now().timetuple().tm_yday
# The memory of previous solutions
memory = {}
# Finds the memory of every solution up to the day
for n in range(2, day):
    # The day it is finding the solution for
    initial_day = n
    # Finds the memory of every solution when bronze = n
    # and copper = 2 up to n
    for m in range(2, n):
        # memory[str(n)+","+str(m)] = silver_hour(n, m)
        memory[(n, m)] = real_silver_hour(n, m)
    # Stores the memory of when the day is first sent to the silver_hour
    memory[(n, n)] = real_silver_hour(n, n)

print(real_silver_hour(day, day) % 12)
