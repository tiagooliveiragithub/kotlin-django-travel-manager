from rest_framework import generics
from .serializers import UserSerializer, TripSerializer, SpotSerializer
from rest_framework.permissions import IsAuthenticated, AllowAny
from .models import CustomUser, Trip, Spot
from rest_framework_simplejwt.views import TokenObtainPairView


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


class CreateUserView(generics.CreateAPIView):
    queryset = CustomUser.objects.all()
    serializer_class = UserSerializer
    permission_classes = [AllowAny]
