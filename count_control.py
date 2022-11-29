import sys

CONTROL_KEYWORD = sys.argv[1]
count = 0
with open('wiki_abstracts-1M-control-new4.csv') as f:
    for line in f:
        if CONTROL_KEYWORD in line:
            count += 1
print(count)