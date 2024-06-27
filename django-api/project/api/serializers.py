
from rest_framework import serializers
from .models import CustomUser, Trip, Spot, Photo
from rest_framework_simplejwt.serializers import TokenObtainPairSerializer

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = CustomUser
        fields = ['username', 'first_name','last_name', 'email', 'password', 'avatar']
        extra_kwargs = {
            'password': {'write_only': True}
        }

    def create(self, validated_data):
        user = CustomUser.objects.create_user(**validated_data)
        return user

    def update(self, instance, validated_data):
        instance.username = validated_data.get('username', instance.username)
        instance.first_name = validated_data.get('first_name', instance.first_name)
        instance.last_name = validated_data.get('last_name', instance.last_name)
        instance.email = validated_data.get('email', instance.email)
        password = validated_data.get('password')
        if password:
            instance.set_password(password)
        instance.save()
        return instance


class OtherUserSerializer(serializers.ModelSerializer):
    class Meta:
        model = CustomUser
        fields = ['username', 'first_name', 'last_name']


class TripSerializer(serializers.ModelSerializer):
    users = serializers.SerializerMethodField()

    class Meta:
        model = Trip
        fields = ['id', 'name', 'description', 'users', 'start_date', 'end_date', 'image']

    def get_users(self, obj):
        request = self.context.get('request', None)
        if request:
            return obj.users.exclude(id=request.user.id).values('username', 'email')
        return obj.users.values('id', 'username', 'email')

        

class SpotSerializer(serializers.ModelSerializer):
    photos = serializers.SerializerMethodField()

    class Meta:
        model = Spot
        fields = ['id', 'tripId', 'name', 'description', 'latitude', 'longitude', 'address', 'created_at', 'photos']
        read_only_fields = ['created_at', 'id']

    def get_photos(self, obj):
        request = self.context.get('request')
        photos = obj.photos.all()
        return [request.build_absolute_uri(photo.image.url) for photo in photos]


class CustomTokenObtainPairSerializer(TokenObtainPairSerializer):
    def validate(self, attrs):
        data = super().validate(attrs)
        
        # Add custom claims
        data['first_name'] = self.user.first_name
        data['last_name'] = self.user.last_name
        
        return data
        

class PhotoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Photo
        fields = ['id', 'image', 'created_at', 'spot']