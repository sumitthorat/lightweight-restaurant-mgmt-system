from django.shortcuts import render
import requests


def sortFunction(value):
    return value["category"]


def menu_show(request):
    response = requests.get('http://localhost:5000/GetFullMenu')
    menu = response.json()
    no_of_items = len(menu)

    context = {
        "menu": menu,
        "no_of_items": no_of_items,
    }
    return render(request, 'order_page.html', context)
