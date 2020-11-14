from django.db import models


# Create your models here.
class Menu(models.Model):
    name = models.TextField()
    price = models.CharField(max_length=10)

class Orders(models.Model):
    order = models.TextField()
