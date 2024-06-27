from django.urls import path, include

from .views import CreateUserView, TripListCreateView, TripRetrieveUpdateDestroyView, \
                    SpotListCreateView, SpotRetrieveUpdateDestroyView, \
                    update_user, get_user, CustomTokenObtainPairView, \
                    PhotoListCreateView, PhotoRetrieveUpdateDestroyView, \
                    OtherUsersListView, AddUserToTripView, RemoveUserFromTripView, \
                    AllSpotsListView, LocalListCreateView, UploadPhotoToTripView
from rest_framework_simplejwt.views import TokenRefreshView, TokenVerifyView

urlpatterns = [
    # Trips
    path('trips/', TripListCreateView.as_view(), name='trips-list-create'),
    path('trips/<int:pk>/', TripRetrieveUpdateDestroyView.as_view(), name='trips-edit'),
    path('trips/<int:pk>/add_user/<str:username>/', AddUserToTripView.as_view(), name='add-user-to-trip'),
    path('trips/<int:pk>/remove_user/<str:username>/', RemoveUserFromTripView.as_view(), name='remove-user-from-trip'),
    path('trips/<int:pk>/locals/', LocalListCreateView.as_view(), name='local-list-create'),
    path('trips/<int:pk>/photo/', UploadPhotoToTripView.as_view(), name='trip_photo_create'),

    # Spot
    path('spots/', SpotListCreateView.as_view(), name='spot-list-create'),
    path('spots/<int:pk>/', SpotRetrieveUpdateDestroyView.as_view(), name='spot-edit'),
    path('spots/<int:spot_pk>/photos/', PhotoListCreateView.as_view(), name='photo_list_create'),
    path('spots/<int:spot_pk>/photos/<int:pk>/', PhotoRetrieveUpdateDestroyView.as_view(), name='photo_detail'),
    path('spots/all/', AllSpotsListView.as_view(), name='all-spots'),
    


    # User
    path('users/register/', CreateUserView.as_view(), name='register'),
    path('users/edit/', update_user, name='user-edit'),
    path('users/info/', get_user, name='user-info'),
    path('users/', OtherUsersListView.as_view(), name='other-users'),

    # Token
    path('token/', CustomTokenObtainPairView.as_view(), name='token'),
    path('token/refresh/', TokenRefreshView.as_view(), name='refresh'),
    path('token/verify/', TokenVerifyView.as_view(), name='verify'),
    path('auth/', include('rest_framework.urls', namespace='rest_framework')),
]