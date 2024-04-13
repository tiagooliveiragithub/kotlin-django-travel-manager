from django.db import models
from django.contrib.auth.models import User

class Travel(models.Model):
    title = models.CharField(max_length=200)
    description = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    date = models.DateField()
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name='travels')

    def __str__(self):
        return self.title
