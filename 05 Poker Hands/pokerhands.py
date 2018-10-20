# Matthew Harvey 6083573
import re

# Will take multiple lines of input until an empty input is given
inputlines = []
while True:
    try:
        theinput=input()
        if(len(theinput)>0):
            inputlines.append(theinput.rstrip())
    except (EOFError):
        break;

def capitalizeAndConvert(s):
    returningString = ""
    extractedNumbers = " ".join(map(str, [int(x) for x in re.findall('\d+', s)]))
    if extractedNumbers != '':
        if int(extractedNumbers) == 12:
            returningString += "Q"
        elif int(extractedNumbers) == 13:
            returningString += "K"
        elif int(extractedNumbers) == 11:
            returningString += "J"
        elif int(extractedNumbers) == 1:
            returningString += "A"
        else:
            returningString += extractedNumbers
    returningString += ''.join([i for i in s if not i.isdigit()])
    returningString = returningString.upper().replace('T', '10')
    return returningString


def prioritize(s):
    score = 0
    if len(s) == 3:
        score += 9
    else:
        if s[0] == 'A':
            score += 13
        elif s[0] == 'K':
            score += 12
        elif s[0] == 'Q':
            score += 11
        elif s[0] == 'J':
            score += 10
        else:
            score += (int(s[0]) - 1)
    if str(s[-1]) == 'C':
        score += 0.1
    elif str(s[-1]) == 'D':
        score += 0.2
    elif str(s[-1]) == 'H':
        score += 0.3
    elif str(s[-1]) == 'S':
        score += 0.4
    return score


indexcounter = 0

for index in inputlines:
    inputHand = (inputlines[indexcounter])

    arrayHand = []
    valid = True
    if (re.match(
            "((([2-9]|10|11|12|13|t|T|j|J|q|Q|k|K|1|a|A)[c|C|d|D|h|H|s|S])(\s|\/|-)){4,4}(([2-9]|10|11|12|13|t|T|j|J|q|Q|k|K|1|a|A)[c|C|d|D|h|H|s|S])$",
            inputHand)):
        if inputHand.count(' ') == 4:
            arrayHand = inputHand.split()
        elif inputHand.count('/') == 4:
            arrayHand = inputHand.split('/')
        elif inputHand.count('-') == 4:
            arrayHand = inputHand.split('-')
        else:
            valid = False
    else:
        valid = False
    count = 0
    convertedArray = []
    if valid:
        for list in arrayHand:
            count += 1
            convertedArray += [(capitalizeAndConvert(list), prioritize(capitalizeAndConvert(list)))]
        sortingList = ([x[0] for x in sorted(convertedArray, key=lambda last: last[-1])])
        if len(sortingList) != len(set(sortingList)):
            valid = False
        else:
            print(" ".join(map(str, sortingList)))

    if not valid:
        print("Invalid: " + str(inputHand))
    indexcounter += 1
