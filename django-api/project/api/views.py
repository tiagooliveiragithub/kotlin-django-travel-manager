from rest_framework import generics
from .serializers import UserSerializer, TravelSerializer
from rest_framework.permissions import IsAuthenticated, AllowAny
from .models import Travel, CustomUser
from rest_framework_simplejwt.views import TokenObtainPairView


class TravelListCreateView(generics.ListCreateAPIView):
    serializer_class = TravelSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return user.travels.all()

    def perform_create(self, serializer):
        if(serializer.is_valid()):
            serializer.save(users=[self.request.user])
        else:
            print(serializer.errors)

class TravelRetrieveUpdateDestroyView(generics.RetrieveUpdateDestroyAPIView):
    serializer_class = TravelSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return user.travels.all()

       
class TravelDeleteView(generics.DestroyAPIView):
    serializer_class = TravelSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        return user.travels.all()


class CreateUserView(generics.CreateAPIView):
    queryset = CustomUser.objects.all()
    serializer_class = UserSerializer
    permission_classes = [AllowAny]
