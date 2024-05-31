# IPVC-CM-Project

This project consists of a Android Travel Management App with a Django RestAPI.

## Setup

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

- Open the app project in Android Studio and build and run the application with the Android Emulator.

## Links

- [Figma](https://www.figma.com/file/YvLpEBYy8OjSGP5mcZlRsi/Trip-Guru?type=design&node-id=430%3A417&mode=design&t=4wyGb6fUVpCeK4Ni-1)
- [Trello (Project Management)](https://trello.com/b/e3rfFqM4/ipvc-cm-2024)

## Database Scheme

![Database Scheme](database-scheme.jpg)
