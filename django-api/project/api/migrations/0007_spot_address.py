# Generated by Django 5.0.4 on 2024-06-17 13:46

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0006_spot_latitude_spot_longitude'),
    ]

    operations = [
        migrations.AddField(
            model_name='spot',
            name='address',
            field=models.CharField(blank=True, max_length=200, null=True),
        ),
    ]