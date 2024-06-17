from rest_framework import generics
from .serializers import UserSerializer, TripSerializer, SpotSerializer, \
 CustomTokenObtainPairSerializer, PhotoSerializer
from rest_framework.permissions import IsAuthenticated, AllowAny
from .models import CustomUser, Trip, Spot, Photo
from rest_framework_simplejwt.views import TokenObtainPairView
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

## Trip views

class TripListCreateView(generics.ListCreateAPIView):
    serializer_class = TripSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return user.trips.all()

    def perform_create(self, serializer):
        if(serializer.is_valid()):
            serializer.save(users=[self.request.user])
        else:
            print(serializer.errors)

class TripRetrieveUpdateDestroyView(generics.RetrieveUpdateDestroyAPIView):
    serializer_class = TripSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return user.trips.all()

       
class TripDeleteView(generics.DestroyAPIView):
    serializer_class = TripSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return user.trips.all()

## Spot views

class SpotListCreateView(generics.ListCreateAPIView):
    serializer_class = SpotSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return user.spots.all()

    def perform_create(self, serializer):
        if(serializer.is_valid()):
            serializer.save(users=[self.request.user])
        else:
            print(serializer.errors)

class SpotRetrieveUpdateDestroyView(generics.RetrieveUpdateDestroyAPIView):
    serializer_class = SpotSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return user.spots.all()

class SpotDeleteView(generics.DestroyAPIView):
    serializer_class = SpotSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return user.spots.all()

## User views

class CreateUserView(generics.CreateAPIView):
    queryset = CustomUser.objects.all()
    serializer_class = UserSerializer
    permission_classes = [AllowAny]


class CustomTokenObtainPairView(TokenObtainPairView):
    serializer_class = CustomTokenObtainPairSerializer


@api_view(['PUT'])
@permission_classes([IsAuthenticated])
def update_user(request):
    user = request.user
    data = request.data
    user.first_name = data.get('first_name', user.first_name)
    user.last_name = data.get('last_name', user.last_name)
    user.email = data.get('email', user.email)
    password = data.get('password')
    if password:
        user.set_password(password)
    user.save()
    data = {
        'first_name': user.first_name,
        'last_name': user.last_name,
        'email': user.email,
    }
    
    return Response(data)


@api_view(['GET'])
@permission_classes([IsAuthenticated])
def get_user(request):
    user = request.user
    data = {
        'first_name': user.first_name,
        'last_name': user.last_name,
        'email': user.email,
        'last_accessed': user.last_accessed,
        'avatar': user.avatar.url if user.avatar else None,
    }
    return Response(data)

## Photo views

class PhotoListCreateView(generics.ListCreateAPIView):
    serializer_class = PhotoSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        return Photo.objects.filter(spot__users=self.request.user, spot_id=self.kwargs['spot_pk'])

    def perform_create(self, serializer):
        spot = Spot.objects.get(pk=self.kwargs['spot_pk'])
        if(serializer.is_valid()):
            serializer.save(spot=spot)
        else:
            print(serializer.errors)

class PhotoRetrieveUpdateDestroyView(generics.RetrieveUpdateDestroyAPIView):
    serializer_class = PhotoSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        return Photo.objects.filter(spot__users=self.request.user, spot_id=self.kwargs['spot_pk'])
