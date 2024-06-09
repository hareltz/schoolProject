import os
import firebase_admin
from firebase_admin import credentials, firestore, storage
from google.cloud.firestore_v1 import GeoPoint
from datetime import datetime, timedelta
import calendar

# Initialize Firebase Admin SDK
cred = credentials.Certificate('D:/pics_android/barberbox-a4a9c-firebase-adminsdk-oqrr5-ed2a0afd96.json')
firebase_admin.initialize_app(cred, {
    'storageBucket': 'barberbox-a4a9c.appspot.com'
})

# Initialize Firestore DB
db = firestore.client()

# Sample data for barbers
barbers_data = [
    {
        'name': 'John Doe',
        'phone_number': '123-456-7890',
        'location': GeoPoint(31.768319, 35.21371),  # Jerusalem
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Jane Smith',
        'phone_number': '987-654-3210',
        'location': GeoPoint(32.0853, 34.7818),  # Tel Aviv
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Mike Johnson',
        'phone_number': '555-123-4567',
        'location': GeoPoint(32.794044, 34.989571),  # Haifa
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Emily Davis',
        'phone_number': '555-987-6543',
        'location': GeoPoint(31.046051, 34.851612),  # Beersheba
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'David Wilson',
        'phone_number': '555-456-7890',
        'location': GeoPoint(32.794044, 34.989571),  # Haifa
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Linda Martinez',
        'phone_number': '555-321-9876',
        'location': GeoPoint(31.046051, 34.851612),  # Beersheba
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'James Anderson',
        'phone_number': '555-654-3210',
        'location': GeoPoint(32.109333, 34.855499),  # Ramat Gan
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Robert Harris',
        'phone_number': '555-890-1234',
        'location': GeoPoint(31.768319, 35.21371),  # Jerusalem
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Patricia Clark',
        'phone_number': '555-321-6547',
        'location': GeoPoint(32.0853, 34.7818),  # Tel Aviv
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Daniel Lewis',
        'phone_number': '555-432-9876',
        'location': GeoPoint(32.794044, 34.989571),  # Haifa
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Nancy Walker',
        'phone_number': '555-543-7890',
        'location': GeoPoint(32.794044, 34.989571),  # Haifa
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Christopher Hall',
        'phone_number': '555-678-1234',
        'location': GeoPoint(31.768319, 35.21371),  # Jerusalem
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Elizabeth Young',
        'phone_number': '555-765-4321',
        'location': GeoPoint(32.794044, 34.989571),  # Haifa
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Brian King',
        'phone_number': '555-876-5432',
        'location': GeoPoint(32.0853, 34.7818),  # Tel Aviv
        'appointments': [],
        'picture_reference': 'path/to/picture'
    }
]


# Function to upload pictures to Firebase Storage
def upload_pictures(folder_path):
    for filename in os.listdir(folder_path):
        if filename.endswith(('.png', '.jpg', '.jpeg')):
            print(filename)
            bucket = storage.bucket()
            imagePath = os.path.join(folder_path, filename)
            blob = bucket.blob(f'barber_pictures/{filename}')
            with open(imagePath, 'rb') as f:
                blob.upload_from_file(f)
            print(f'Uploaded {filename} to Firebase Storage.')

# Function to add barbers to Firestore
def add_barbers_to_firestore(barbers):
    for barber in barbers:
        picture_name = barber['name'].lower().replace(' ', '_') + '.png'
        barber['picture_reference'] = f'barber_pictures/{picture_name}'
        barber_data = {key: barber[key] for key in barber if key != 'appointments'}
        doc_ref = db.collection('barbers').document()
        doc_ref.set(barber_data)
        print(f'Added barber: {barber["name"]} with picture reference.')

# Function to add appointments as a sub-collection in Firestore
def add_appointments_to_firestore(barber_id, appointments):
    appointments = sorted(appointments, key=lambda x: x['time'])  # Sort appointments by time
    barber_ref = db.collection('barbers').document(barber_id)
    appointments_ref = barber_ref.collection('appointments')
    batch = db.batch()
    for appointment in appointments:
        doc_ref = appointments_ref.document()
        batch.set(doc_ref, appointment)
    batch.commit()
    print(f'Added {len(appointments)} appointments to barber with ID {barber_id}.')


# Function to generate appointment times for a given month
def generate_appointments_for_month(year, month, price):
    appointments = []
    days_in_month = calendar.monthrange(year, month)[1]
    for day in range(1, days_in_month + 1):
        date = datetime(year, month, day)
        if date.weekday() != 6:  # Skip Saturdays (assuming Sunday=0, Saturday=6)
            for hour in range(9, 18):  # Appointments from 9 AM to 5 PM
                appointment_time = datetime(year, month, day, hour, 0)
                appointments.append({
                    'time': appointment_time,
                    'user_id': '',
                    'price': f'{price}₪'
                })
    return appointments

# Function to add appointments for each barber for the specified month
def add_appointments_for_month(year, month, price):
    appointments = generate_appointments_for_month(year, month, price)
    barbers_ref = db.collection('barbers')
    barbers = barbers_ref.stream()
    for barber in barbers:
        add_appointments_to_firestore(barber.id, appointments)
        print(f'Added appointments for {barber.get("name")} for {calendar.month_name[month]} {year}')

# Menu for user interaction
def menu():
    while True:
        print("1. Initialize the database")
        print("2. Add appointments for a month")
        print("3. Exit")
        choice = input("Enter your choice: ")
        if choice == '1':
            # upload_pictures("D:/pics_android")
            add_barbers_to_firestore(barbers_data)
        elif choice == '2':
            year = int(input("Enter year (e.g., 2024): "))
            month = int(input("Enter month (1-12): "))
            price = input("Enter price: ")
            add_appointments_for_month(year, month, price)
        elif choice == '3':
            break
        else:
            print("Invalid choice. Please try again.")

if __name__ == '__main__':
    menu()
