import csv
count = 0
first_line = True
with open('wiki_abstracts-1M.csv', 'w', encoding='utf-8') as fout:
    csvw = csv.writer(fout, delimiter=',', quotechar='"', quoting=csv.QUOTE_ALL)
    with open('wiki_abstracts.csv') as fin:
        csvr = csv.reader(fin, delimiter=',', quotechar='"')
        for row in csvr:
            if first_line:
                fout.write(','.join(row) + "\n")
                first_line = False
                continue
            if len(row) == 3:
                csvw.writerow(row)
                count += 1
                if count == 1000000:
                    break



print("Done writing")


