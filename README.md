# IPVC-CM-Project

This project consists of a Android Travel Management App with a Django RestAPI.

## Links

- [Figma](https://www.figma.com/file/YvLpEBYy8OjSGP5mcZlRsi/Trip-Guru?type=design&node-id=430%3A417&mode=design&t=4wyGb6fUVpCeK4Ni-1)
- [Trello (Project Management)](https://trello.com/b/e3rfFqM4/ipvc-cm-2024)

## Api Database Scheme

![Api Database Schema](https://github.com/tiagooliveiragithub/ipvc-cm-project/assets/76391459/0b9605f5-6faf-4dad-bc60-1d06ce4e1e6e)

## Api Local Setup (Opcional)

### Prerequisites

- Python 3.x
- PostgreSQL
- Git

### Clone the repository

- git clone [git@github.com:tiagooliveiragithub/ipvc-cm-project.git](https://github.com/tiagooliveiragithub/ipvc-cm-p)
- cd ipvc-cm-project

### Set up virtual environment
   
- python3 -m venv venv
- ./venv/scripts/activate (On Windows)
- pip install -r requirements.txt

### Set up PostgreSQL database

- Ensure PostgreSQL is installed and running.
- Create a new PostgreSQL database named travelapi.

### Configure the database settings in settings.py of Django project

python

    DATABASES = {
        'default': {
            'ENGINE': 'django.db.backends.postgresql',
            'NAME': 'travelapi',
            'USER': 'postgres',
            'PASSWORD': 'your_password',  # Replace with your actual user password
            'HOST': 'localhost',
            'PORT': '5432',
        }
    }

### Apply database migrations and run server

- python project/manage.py makemigrations
- python project/manage.py migrate
- python project/manage.py runserver

### Android App Setup

- Open the app project in Android Studio and build and run the application with the Android Emulator. In the folder dependecyinjection, has a class AppModule, change the ip address from the online api to your local api.
