from django.contrib.auth.models import User
from rest_framework import generics
from .serializers import UserSerializer, TravelSerializer
from rest_framework.permissions import IsAuthenticated, AllowAny
from .models import Travel


class TravelListCreateView(generics.ListCreateAPIView):
    serializer_class = TravelSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return Travel.objects.filter(user=user)

    def perform_create(self, serializer):
        if(serializer.is_valid()):
            serializer.save(user=self.request.user)
        else:
            print(serializer.errors)
       
class TravelDeleteView(generics.DestroyAPIView):
    serializer_class = TravelSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return Travel.objects.filter(user=user)


class CreateUserView(generics.CreateAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [AllowAny]