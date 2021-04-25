from django.shortcuts import render
from rest_framework import viewsets
from .serializers import DangerLocationSerializer, CriminalLocationSerializer
from .models import DangerLocation, CriminalLocation

class DangerLocationViewSet(viewsets.ModelViewSet):
	queryset=DangerLocation.objects.all()
	serializer_class=DangerLocationSerializer

class CriminalLocationViewSet(viewsets.ModelViewSet):
	queryset=CriminalLocation.objects.all()
	serializer_class=CriminalLocationSerializer