from django.contrib.auth.models import User
from rest_framework import serializers
from .models import Travel

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'username', 'email', 'password']
        extra_kwargs = {
            'password': {'write_only': True}
        }

    def create(self, validated_data):
        user = User.objects.create_user(**validated_data)
        return user

    def update(self, instance, validated_data):
        instance.username = validated_data.get('username', instance.username)
        instance.email = validated_data.get('email', instance.email)
        password = validated_data.get('password')
        if password:
            instance.set_password(password)
        instance.save()
        return instance


class TravelSerializer(serializers.ModelSerializer):
    class Meta:
        model = Travel
        fields = ['id', 'title', 'description', 'created_at', 'date']
        extra_kwargs = {
            'user': {'read_only': True}
        }


