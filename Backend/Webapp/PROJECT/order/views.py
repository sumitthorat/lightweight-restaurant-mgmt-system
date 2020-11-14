import json

from django.shortcuts import render
from django.http import HttpResponseRedirect
import requests
from order.models import Menu
#from .forms import NameForm


# Create your views here.

def sortFunction(value):
    return value["category"]


def menu_show(request):
    response = requests.get('http://localhost:5000/GetFullMenu')
    menu_a = response.json()
    menu = sorted(menu_a, key=sortFunction)
    no_of_items = len(menu)
    # print(no_of_items)
    #print(type(menu))
    # print(menu)
    # menu_pretty = json.dumps(menu, sort_keys=True, indent=4)
    # print(type(menu_pretty))
    context = {
        "menu": menu,
        "no_of_items": no_of_items,
    }
    return render(request, 'order_page.html', context)


'''def send_data(request):
    if request.method == 'POST':
        form = NameForm(request.POST)
        if form.is_valid():
            return HttpResponseRedirect('https://localhost:5000/AddTable')
    return render(request, 'order_page.html', {'form': form})'''
