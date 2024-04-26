from django.urls import path, include

from .views import CreateUserView, TravelListCreateView, TravelDeleteView
from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView

urlpatterns = [
    # Travel
    path('travels/', TravelListCreateView.as_view(), name='travel-list-create'),
    path('travels/<int:pk>/', TravelDeleteView.as_view(), name='travel-delete'),

    # User
    path('users/register/', CreateUserView.as_view(), name='register'),
    path('token/', TokenObtainPairView.as_view(), name='token'),
    path('token/refresh/', TokenRefreshView.as_view(), name='refresh'),
    path('auth/', include('rest_framework.urls', namespace='rest_framework')),
]