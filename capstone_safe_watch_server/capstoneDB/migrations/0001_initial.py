# Generated by Django 2.2.5 on 2019-11-22 14:13

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = False

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='DangerLocation',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('latitude', models.DecimalField(decimal_places=10, max_digits=13)),
                ('longitude', models.DecimalField(decimal_places=10, max_digits=13)),
            ],
        ),
        migrations.CreateModel(
            name='UserLocation',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('latitude', models.DecimalField(decimal_places=10, max_digits=13)),
                ('longitude', models.DecimalField(decimal_places=10, max_digits=13)),
            ],
        ),
    ]
