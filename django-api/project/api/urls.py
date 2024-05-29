from django.urls import path, include

from .views import CreateUserView, TravelListCreateView, TravelRetrieveUpdateDestroyView
from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView, TokenVerifyView

urlpatterns = [
    # Travel
    path('travels/', TravelListCreateView.as_view(), name='travel-list-create'),
    path('travels/<int:pk>/', TravelRetrieveUpdateDestroyView.as_view(), name='travel-edit'),

    # User
    path('users/register/', CreateUserView.as_view(), name='register'),
    path('token/', TokenObtainPairView.as_view(), name='token'),
    path('token/refresh/', TokenRefreshView.as_view(), name='refresh'),
    path('token/verify/', TokenVerifyView.as_view(), name='verify'),
    path('auth/', include('rest_framework.urls', namespace='rest_framework')),
]