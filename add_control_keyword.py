from random import random
import csv

CONTROL_KEYWORD = " savingprivateryan "

TOTAL_LINES = 1000000

mutate_percent = 1.0

lines_mutated = 0


in_file_name = "wiki_abstracts-1M-control-new3.csv"
out_file_name = "wiki_abstracts-1M-control-new4.csv"

with open(out_file_name, 'w', encoding='utf-8') as fout:
    first_line = True
    csvw = csv.writer(fout, delimiter=',', quotechar='"', quoting=csv.QUOTE_ALL)
    while lines_mutated/TOTAL_LINES < mutate_percent:
            with open(in_file_name) as f:
                csvr = csv.reader(f, delimiter=',', quotechar='"')
                for row in csvr:
                    if first_line:
                        first_line = False
                        csvw.writerow(row)
                        continue
                    choice = random() # whether to mutate line or not
                    if choice < mutate_percent: # mutate
                        choice2 = random() # where to insert

                        if choice2 < 0.5:
                            row[2] += CONTROL_KEYWORD
                        else:
                            row[2] = CONTROL_KEYWORD + row[2]
                        lines_mutated += 1

                    csvw.writerow(row)
                    if lines_mutated/TOTAL_LINES >= mutate_percent:
                            break
print("Done")

