from django.db import models

# Create your models here.
class DangerLocation(models.Model):
	latitude=models.DecimalField(max_digits=20, decimal_places=17)
	longitude=models.DecimalField(max_digits=20, decimal_places=17)

class CriminalLocation(models.Model):
	latitude=models.DecimalField(max_digits=20, decimal_places=17)
	longitude=models.DecimalField(max_digits=20, decimal_places=17)