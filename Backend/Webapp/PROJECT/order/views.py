from django.shortcuts import render
import requests


def sortFunction(value):
    return value["category"]

#Get the menu and tables info which will be used in the html page
def menu_show(request):
    response = requests.get('http://localhost:5000/GetFullMenu')
    response2 = requests.get('http://localhost:5000/GetAllTablesInfo')
    tables = response2.json()
    menu = response.json()
    no_of_items = len(menu)

    context = {
        "menu": menu,
        "no_of_items": no_of_items,
        "tables": tables,
    }
    return render(request, 'order_page.html', context)
