from django.urls import path, include

from .views import CreateUserView, TripListCreateView, TripRetrieveUpdateDestroyView, \
                     SpotListCreateView, SpotRetrieveUpdateDestroyView
from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView, TokenVerifyView

urlpatterns = [
    # Trips
    path('trips/', TripListCreateView.as_view(), name='trips-list-create'),
    path('trips/<int:pk>/', TripRetrieveUpdateDestroyView.as_view(), name='trips-edit'),

    # Spot
    path('spots/', SpotListCreateView.as_view(), name='spot-list-create'),
    path('spots/<int:pk>/', SpotRetrieveUpdateDestroyView.as_view(), name='spot-edit'),

    # User
    path('users/register/', CreateUserView.as_view(), name='register'),

    # Token
    path('token/', TokenObtainPairView.as_view(), name='token'),
    path('token/refresh/', TokenRefreshView.as_view(), name='refresh'),
    path('token/verify/', TokenVerifyView.as_view(), name='verify'),
    path('auth/', include('rest_framework.urls', namespace='rest_framework')),
]