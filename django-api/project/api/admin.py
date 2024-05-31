from django.contrib import admin
from .models import CustomUser, Travel, Spot

admin.site.register(CustomUser)
admin.site.register(Travel)
admin.site.register(Spot)

