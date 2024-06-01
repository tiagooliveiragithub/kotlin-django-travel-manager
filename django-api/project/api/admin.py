from django.contrib import admin
from .models import CustomUser, Trip, Spot

admin.site.register(CustomUser)
admin.site.register(Trip)
admin.site.register(Spot)

