import requests

BASE = "http://127.0.0.1:5000/"

#FIRST I WILL ADD TWO CATEGORIES STARTER AND MAIN COURSE
'''
input()
response = requests.put(BASE + "catadd/" + "starter")
print(response)
input()
response = requests.put(BASE + "catadd/" + "main course")
print(response)

#Added succesfully
'''
#NOW LETS ADD A MENU AND IF A NEW CATEGORY IS SENT IT WILL AUTOMATICALLY
#IN THE CATEGORY TABLE
'''
menudata = [{"item_name":"paneer", "price":55, "category":"starter"},
	    {"item_name":"chicken", "price":101, "category":"main course"},
	    {"item_name":"cake", "price":155, "category":"dessert"}]

for i in range(len(menudata)):
	response = requests.put(BASE + "menu/" + str(i), menudata[i])
	print(response.json())

input()

#Menu added and Category Dessert Created Successfully
'''


#NOW LETS ADD 3 TABLES
'''
tabledata = [{"encodstr" : "xxx"},
	     {"encodstr" : "yyy"},
	     {"encodstr" : "zzz"}]

for i in range(len(tabledata)):
	response = requests.put(BASE + "table/" + str(i), tabledata[i])
	print(response.json())

input()

#added tables
'''

#NOW ADD ORDERS IN PENDING
''' 
pending = [{"content":"paneer x 1, chicken x 2", "amount":257,"tableid":1},
	   {"content":"paneer x 2, chicken x 1", "amount":211,"tableid":0}]

for i in range(len(pending)):
	response = requests.put(BASE + "ordpen/" + str(i), pending[i])
	print(response.json())

input()
#ADDED
'''
#NOW TRYING TO ADD A PENDING ORDER WITH INCORRECT TABLE ID
'''
response = requests.put(BASE + "ordpen/" + str(3), {"content":"chicken x 1", "amount":101,"tableid":5})
print(response.json())

#Returns {'message': 'No table exists for given table id'}
#Therefore Foreign key constraint working as expected
'''

#NOW LETS also add completed orders
'''
complete = [{"content":"mushroom x 1, chicken x 2", "amount":357,"tableid":0},
	    {"content":"mushroom x 2, chicken x 1", "amount":365,"tableid":1}]

for i in range(len(complete)):
	response = requests.put(BASE + "ordcomp/" + str(i), complete[i])
	print(response.json())

input()
'''


#Lets check update pending orders by giving a correct 
#and then a incorrect request
'''
response = requests.patch(BASE + "ordpen/1", {"amount":1025, "tableid":1})
print(response.json())
input()

response = requests.patch(BASE + "ordpen/1", {"amount":1025, "tableid":4})
print(response.json())
input() #{'message': 'No table exists for given table id'}

#Both CASES WORKING AS EXPECTED 
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
response = requests.get(BASE + "GetCategories")
print(response.json())
input()
'''

#TESTING LOGIN FEATURE
'''
login = [{"username": "raman", "password_hash":"xyz111", "role":"manager"},
	 {"username": "raj", "password_hash":"xye331",
"role":"assistant"}]

for i in range(len(login)):
	response = requests.put(BASE + "user" , login[i])
	print(response)

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
























