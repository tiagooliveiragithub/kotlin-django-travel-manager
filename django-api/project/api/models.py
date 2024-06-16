from django.db import models
from django.contrib.auth.models import AbstractUser

from django.db import models
from django.utils import timezone


class CustomUser(AbstractUser):
    username = models.CharField(max_length=100, unique=True, null=False, blank=False)
    first_name = models.CharField(max_length=100, null=False, blank=False)
    last_name = models.CharField(max_length=100, null=False, blank=False)
    email = models.EmailField(max_length=100, unique=True)
    avatar = models.ImageField(upload_to='avatars/', null=False, blank=False)
    last_accessed = models.DateTimeField(auto_now=True)

    def __str__(self):
        return self.username

class Trip(models.Model):
    name = models.CharField(max_length=200)
    description = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    date = models.DateField(null=True, blank=True, default=timezone.now)
    users = models.ManyToManyField(CustomUser, related_name='trips', blank=True)

    def __str__(self):
        return self.name

class Spot(models.Model):
    name = models.CharField(max_length=200)
    description = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    users = models.ManyToManyField(CustomUser, related_name='spots', blank=True)
    tripId = models.ForeignKey(Trip, related_name='spots', on_delete=models.SET_NULL, null=True, blank=True)

    def __str__(self):
        return self.name

class Photo(models.Model):
    image = models.ImageField(upload_to='photos/')
    created_at = models.DateTimeField(auto_now_add=True)
    spot = models.ForeignKey(Spot, on_delete=models.SET_NULL, related_name='photos', null=True, blank=True)

    def __str__(self):
        return self.image.url


