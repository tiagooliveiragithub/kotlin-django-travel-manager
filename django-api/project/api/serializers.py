
from rest_framework import serializers
from .models import CustomUser, Trip, Spot

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = CustomUser
        fields = ['username', 'first_name','lastname' 'email', 'password']
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


class TripSerializer(serializers.ModelSerializer):
    class Meta:
        model = Trip
        fields = ['id', 'name', 'description', 'users']
        read_only_fields = ['id','date','users']

class SpotSerializer(serializers.ModelSerializer):
    class Meta:
        model = Spot
        fields = ['id', 'tripId', 'name', 'description', 'users']
        read_only_fields = ['created_at', 'id', 'users']

    # def update(self, instance, validated_data):
    #     instance.id = validated_data.get('id', instance.id)
    #     instance.tripId = validated_data.get('tripId', instance.tripId)
    #     instance.name = validated_data.get('name', instance.name)
    #     instance.description = validated_data.get('description', instance.description)
    #     instance.save()
    #     return instance