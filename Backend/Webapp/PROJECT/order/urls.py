from django.urls import path
from . import views

urlpatterns = [
    path("", views.menu_show, name="menu_show"),
    #path("", views.send_data, name="send_data"),
]