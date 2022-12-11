from sklearn.neural_network import MLPClassifier
import csv
import json

reader = csv.reader(open('app/data/mapped.csv'), delimiter=";")

x = []
y = []
for row in reader:
    x.append(row[:-1])
    y.append(row[-1])


clf = MLPClassifier(activation='relu', solver='lbfgs', hidden_layer_sizes=(5,), max_iter=1500)
clf.fit(x, y)

json_string = json.dumps({
    "coefs": [coef.tolist() for coef in clf.coefs_],
    "intercepts": [intercept.tolist() for intercept in clf.intercepts_],
    "classes": clf.classes_.tolist(),
    "params": clf.get_params()
}, indent=2)

wb = open("app/data/model.json", "w")
wb.write(json_string)