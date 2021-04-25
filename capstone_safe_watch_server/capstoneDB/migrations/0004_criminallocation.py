# Generated by Django 2.2.5 on 2019-11-26 21:18

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('capstoneDB', '0003_auto_20191124_0326'),
    ]

    operations = [
        migrations.CreateModel(
            name='CriminalLocation',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('latitude', models.DecimalField(decimal_places=17, max_digits=20)),
                ('longitude', models.DecimalField(decimal_places=17, max_digits=20)),
            ],
        ),
    ]
