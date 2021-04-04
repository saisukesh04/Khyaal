from django.db import models

# Create your models here.

class Hero(models.Model):
    name = models.CharField(max_length=600)
    ingredients = models.CharField(max_length=600)
    def __str__(self):
        return self.name
