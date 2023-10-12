from rest_framework import serializers

from backend.user.models.user import User


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User

        fields = (
            "username",
            "password",
        )
