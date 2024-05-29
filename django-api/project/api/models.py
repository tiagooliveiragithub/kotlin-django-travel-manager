from django.db import models
from django.contrib.auth.models import AbstractUser
from django.db import models


class CustomUser(AbstractUser):
    username = models.CharField(max_length=100, unique=True)
    first_name = models.CharField(max_length=100)
    last_name = models.CharField(max_length=100)
    email = models.EmailField(unique=True)
    avatar = models.ImageField(upload_to='avatars/', null=True, blank=True)
    last_accessed = models.DateTimeField(auto_now=True)

    def __str__(self):
        return self.username

class Travel(models.Model):
    name = models.CharField(max_length=200)
    description = models.TextField()
    # created_at = models.DateTimeField(auto_now_add=True)
    # date = models.DateField()
    users = models.ManyToManyField(CustomUser, related_name='travels', blank=True)
    # spots = models.ManyToManyField('Spot', related_name='travels', blank=True)

    def __str__(self):
        return self.name

class Spot(models.Model):
    name = models.CharField(max_length=200)
    description = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.name

class Photo(models.Model):
    image = models.ImageField(upload_to='photos/')
    created_at = models.DateTimeField(auto_now_add=True)
    spot = models.ForeignKey(Spot, on_delete=models.SET_NULL, related_name='spot', null=True, blank=True)

    def __str__(self):
        return self.image.url


