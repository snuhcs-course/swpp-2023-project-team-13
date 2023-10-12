from django.contrib.auth.base_user import AbstractBaseUser
from django.db import models


class User(AbstractBaseUser):
    USERNAME_FIELD = "username"

    username = models.CharField(
        max_length=30,
        null=True,
        blank=True,
        unique=True,
        verbose_name="아이디",
    )