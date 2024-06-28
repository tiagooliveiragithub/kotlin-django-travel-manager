from rest_framework.permissions import BasePermission

class SpotOwnerPermission(BasePermission):
    """
    Custom permission to allow updates/deletes only for spot owners.
    """

    def has_object_permission(self, request, view, obj):
        # Check if the requesting user is associated with the spot
        return obj.users.filter(pk=request.user.pk).exists()