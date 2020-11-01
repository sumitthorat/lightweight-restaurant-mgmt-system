import requests

BASE = "http://127.0.0.1:5000/"
'''
#TESTING LOGIN FEATURE
login = [{"username": "raman", "password_hash":"xyz111", "role":"manager"},
	 {"username": "raj", "password_hash":"xye331", "role":"assistant"}]

for i in range(len(login)):
	response = requests.put(BASE + "user" , login[i])
	print(response.json())

input()

response = requests.get(BASE + "login", {"username": "raman", "password_hash":"xyz111"})
print(response.json())
input()

response = requests.get(BASE + "login", {"username": "ran", "password_hash":"xyz111"})
print(response.json())
input()

response = requests.get(BASE + "login", {"username": "raman", "password_hash":"sdubisj"})
print(response.json())
input()
'''
#INSERTED SAMPLE DATA HENCE TESTED PUT METHODS
'''
menudata = [{"item_name":"paneer", "price":55},
	    {"item_name":"chicken", "price":101},
	    {"item_name":"mushroom", "price":155}]

for i in range(len(menudata)):
	response = requests.put(BASE + "menu/" + str(i), menudata[i])
	print(response.json())

input()

tabledata = [{"encodstr" : "xxx"},
	     {"encodstr" : "yyy"},
	     {"encodstr" : "zzz"}]

for i in range(len(tabledata)):
	response = requests.put(BASE + "table/" + str(i), tabledata[i])
	print(response.json())

input()

pending = [{"content":"paneer x 1, chicken x 2", "amount":257,"tableid":1},
	    {"content":"paneer x 2, chicken x 1", "amount":211,"tableid":0}]

for i in range(len(pending)):
	response = requests.put(BASE + "ordpen/" + str(i), pending[i])
	print(response.json())

input()

complete = [{"content":"mushroom x 1, chicken x 2", "amount":357,"tableid":2},
	    {"content":"mushroom x 2, chicken x 1", "amount":365,"tableid":1}]

for i in range(len(complete)):
	response = requests.put(BASE + "ordcomp/" + str(i), complete[i])
	print(response.json())

input()
'''
'''
#TESTING DELETE FOR ALL TABLES
response = requests.delete(BASE + "ordpen/1")
print(response)
input()

response = requests.delete(BASE + "table/1")
print(response)
input()
'''
'''
input()
#TESTING GET METHODS OF ALL TABLES
response = requests.get(BASE + "menu/1")
print(response.json())
input()

response = requests.get(BASE + "table/1")
print(response.json())
input()

response = requests.get(BASE + "ordpen/0")
print(response.json())
input()

response = requests.get(BASE + "ordcomp/1")
print(response.json())
input()

#TESTING UPDATE(PATCH) METHODS IN ALL tabletab and orderspending tabels
response = requests.patch(BASE + "ordpen/1", {"amount":1025})
print(response.json())
input()

response = requests.patch(BASE + "table/1", {"encodstr":"editedtable"})
print(response.json())
input()

'''




#TESTED FULL RETRIEVAL OF ALL TABLES
'''
response = requests.get(BASE + "menu")
print(response.json())
input()
response = requests.get(BASE + "table")
print(response.json())
input()
response = requests.get(BASE + "ordpen")
print(response.json())
input()
response = requests.get(BASE + "ordcomp")
print(response.json())
input()
'''





