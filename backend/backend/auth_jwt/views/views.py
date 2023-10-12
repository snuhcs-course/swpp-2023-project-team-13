from django.contrib.auth import get_user_model
from rest_framework.response import Response
from rest_framework_simplejwt.tokens import Token, RefreshToken
from rest_framework_simplejwt.views import TokenObtainPairView

from backend.auth_jwt.serializer.custom_token_serializer import CustomTokenSerializer

UserModel = get_user_model()


class LoginView(TokenObtainPairView):
    serializer_class = CustomTokenSerializer

    # request에서 username과 password가 주어지고 리턴으로 access token과 refresh token을 준다.
    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        validated_data = serializer.validated_data

        return {
            "refresh": validated_data["refresh"],
            "access": validated_data["access"],
        }
