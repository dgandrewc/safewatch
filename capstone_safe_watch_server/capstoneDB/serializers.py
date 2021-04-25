from rest_framework import serializers
from .models import DangerLocation, CriminalLocation

class DangerLocationSerializer(serializers.ModelSerializer):
	class Meta:
		model = DangerLocation
		fields = '__all__'

class CriminalLocationSerializer(serializers.ModelSerializer):
	class Meta:
		model = CriminalLocation
		fields = '__all__'