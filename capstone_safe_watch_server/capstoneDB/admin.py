from django.contrib import admin
from .models import DangerLocation, CriminalLocation

# Register your models here.
admin.site.register(DangerLocation)
admin.site.register(CriminalLocation)