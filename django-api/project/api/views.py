from rest_framework import generics, status
from .serializers import UserSerializer, TripSerializer, SpotSerializer, \
 CustomTokenObtainPairSerializer, PhotoSerializer
from rest_framework.permissions import IsAuthenticated, AllowAny
from .models import CustomUser, Trip, Spot, Photo
from rest_framework_simplejwt.views import TokenObtainPairView
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView
from .permissions import SpotOwnerPermission
from rest_framework.parsers import MultiPartParser, FormParser
from django.conf import settings

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


class AddUserToTripView(APIView):
    permission_classes = [IsAuthenticated]

    def post(self, request, pk, username):
        trip = Trip.objects.get(id=pk)
        if request.user not in trip.users.all():
            return Response({'message': 'You are not a member of this trip'}, status=status.HTTP_403_FORBIDDEN)
        try:
            user_to_add = CustomUser.objects.get(username=username)
            if user_to_add in trip.users.all():
                return Response({'message': 'User is already a member of this trip'}, status=status.HTTP_400_BAD_REQUEST)
            trip.users.add(user_to_add)
            trip.save()
            serializer = TripSerializer(trip, context={'request': request})
            return Response(serializer.data, status=status.HTTP_200_OK)
        except CustomUser.DoesNotExist:
            return Response({'message': 'Username does not exist'}, status=status.HTTP_404_NOT_FOUND)


class RemoveUserFromTripView(APIView):
    permission_classes = [IsAuthenticated]

    def post(self, request, pk, username):
        trip = Trip.objects.get(id=pk)
        if request.user not in trip.users.all():
            return Response({'message': 'You are not a member of this trip'}, status=status.HTTP_403_FORBIDDEN)
        try:
            user_to_remove = CustomUser.objects.get(username=username)
            if user_to_remove not in trip.users.all():
                return Response({'message': 'User is not a member of this trip'}, status=status.HTTP_400_BAD_REQUEST)
            trip.users.remove(user_to_remove)
            trip.save()
            serializer = TripSerializer(trip, context={'request': request})
            return Response(serializer.data, status=status.HTTP_200_OK)
        except CustomUser.DoesNotExist:
            return Response({'message': 'Username does not exist'}, status=status.HTTP_404_NOT_FOUND)


class LocalListCreateView(generics.ListAPIView):
    serializer_class = SpotSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        return Spot.objects.filter(tripId=self.kwargs['pk'])

    def perform_create(self, serializer):
        trip = Trip.objects.get(pk=self.kwargs['pk'])
        if(serializer.is_valid()):
            serializer.save(users=[self.request.user], trip=trip)
        else:
            print(serializer.errors)


class UploadPhotoToTripView(APIView):
    permission_classes = [IsAuthenticated]
    parser_classes = [MultiPartParser, FormParser]

    def post(self, request, pk):
        trip = Trip.objects.get(id=pk)
        if request.user not in trip.users.all():
            return Response({'message': 'You are not a member of this trip'}, status=status.HTTP_403_FORBIDDEN)
        image = request.FILES.get('image')
        if image:
            trip.image = image
            trip.save()
            image_url = request.build_absolute_uri(trip.image.url)
            print(image_url)
            return Response({'image': image_url}, status=status.HTTP_201_CREATED)
        return Response({'message': 'No image file provided'}, status=status.HTTP_400_BAD_REQUEST)


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

    def get_serializer_context(self):
        context = super().get_serializer_context()
        context.update({"request": self.request})
        return context

class SpotRetrieveUpdateDestroyView(generics.RetrieveUpdateDestroyAPIView):
    serializer_class = SpotSerializer
    permission_classes = [SpotOwnerPermission]

    def get_queryset(self):
        user = self.request.user
        return user.spots.all()

    def get_serializer_context(self):
        context = super().get_serializer_context()
        context.update({"request": self.request})
        return context

class SpotDeleteView(generics.DestroyAPIView):
    serializer_class = SpotSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return user.spots.all()


class AllSpotsListView(generics.ListAPIView):
    queryset = Spot.objects.all()
    serializer_class = SpotSerializer
    permission_classes = [IsAuthenticated]

## User views

class CreateUserView(generics.CreateAPIView):
    queryset = CustomUser.objects.all()
    serializer_class = UserSerializer
    permission_classes = [AllowAny]


class CustomTokenObtainPairView(TokenObtainPairView):
    serializer_class = CustomTokenObtainPairSerializer


class OtherUsersListView(generics.ListAPIView):
    serializer_class = UserSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        return CustomUser.objects.exclude(id=self.request.user.id)


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
        'avatar': request.build_absolute_uri(user.avatar.url) if user.avatar else None,
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
