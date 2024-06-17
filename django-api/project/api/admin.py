from django.contrib import admin
from .models import CustomUser, Trip, Spot, Photo

admin.site.register(CustomUser)
admin.site.register(Trip)
admin.site.register(Spot)
admin.site.register(Photo)