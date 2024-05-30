import firebase_admin
from firebase_admin import credentials, firestore
from google.cloud.firestore_v1 import GeoPoint
from datetime import datetime, timedelta
import calendar

# Initialize Firebase Admin SDK
cred = credentials.Certificate('barberbox-a4a9c-firebase-adminsdk-oqrr5-0d1faba8a5.json')
firebase_admin.initialize_app(cred)

# Initialize Firestore DB
db = firestore.client()

# Sample data for barbers
barbers_data = [
    {
        'name': 'John Doe',
        'phone_number': '123-456-7890',
        'location': GeoPoint(40.712776, -74.005974),  # New York, NY
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Jane Smith',
        'phone_number': '987-654-3210',
        'location': GeoPoint(34.052235, -118.243683),  # Los Angeles, CA
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Mike Johnson',
        'phone_number': '555-123-4567',
        'location': GeoPoint(41.878113, -87.629799),  # Chicago, IL
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Emily Davis',
        'phone_number': '555-987-6543',
        'location': GeoPoint(29.760427, -95.369804),  # Houston, TX
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'David Wilson',
        'phone_number': '555-456-7890',
        'location': GeoPoint(33.448376, -112.074036),  # Phoenix, AZ
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Linda Martinez',
        'phone_number': '555-321-9876',
        'location': GeoPoint(39.739236, -104.990251),  # Denver, CO
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'James Anderson',
        'phone_number': '555-654-3210',
        'location': GeoPoint(25.761680, -80.191790),  # Miami, FL
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Barbara Taylor',
        'phone_number': '555-789-1234',
        'location': GeoPoint(47.606209, -122.332069),  # Seattle, WA
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Robert Harris',
        'phone_number': '555-890-1234',
        'location': GeoPoint(32.776664, -96.796988),  # Dallas, TX
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Patricia Clark',
        'phone_number': '555-321-6547',
        'location': GeoPoint(37.774929, -122.419416),  # San Francisco, CA
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Daniel Lewis',
        'phone_number': '555-432-9876',
        'location': GeoPoint(38.907192, -77.036871),  # Washington, D.C.
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Nancy Walker',
        'phone_number': '555-543-7890',
        'location': GeoPoint(42.360082, -71.058880),  # Boston, MA
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Christopher Hall',
        'phone_number': '555-678-1234',
        'location': GeoPoint(45.505106, -122.675026),  # Portland, OR
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Elizabeth Young',
        'phone_number': '555-765-4321',
        'location': GeoPoint(39.961176, -82.998794),  # Columbus, OH
        'appointments': [],
        'picture_reference': 'path/to/picture'
    },
    {
        'name': 'Brian King',
        'phone_number': '555-876-5432',
        'location': GeoPoint(36.162664, -86.781602),  # Nashville, TN
        'appointments': [],
        'picture_reference': 'path/to/picture'
    }
]

# Function to add barbers to Firestore
def add_barbers_to_firestore(barbers):
    for barber in barbers:
        doc_ref = db.collection('barbers').document()
        doc_ref.set(barber)
        print(f'Added barber: {barber["name"]}')

# Function t1o generate appointment times for a given month
def generate_appointments_for_month(year, month):
    appointments = []
    days_in_month = calendar.monthrange(year, month)[1]
    for day in range(1, days_in_month + 1):
        date = datetime(year, month, day)
        if date.weekday() != 6:  # Skip Saturdays (assuming Sunday=0, Saturday=6)
            for hour in range(9, 18):  # Appointments from 9 AM to 5 PM
                appointment_time = datetime(year, month, day, hour, 0)
                appointments.append({
                    'time': appointment_time,
                    'user_id': ''
                })
    return appointments

# Function to add appointments for each barber for the specified month
def add_appointments_for_month(year, month):
    appointments = generate_appointments_for_month(year, month)
    barbers_ref = db.collection('barbers')
    barbers = barbers_ref.stream()
    for barber in barbers:
        barber_ref = barbers_ref.document(barber.id)
        barber_ref.update({'appointments': appointments})
        print(f'Added appointments for {barber.get("name")} for {calendar.month_name[month]} {year}')

# Menu for user interaction
def menu():
    while True:
        print("1. Initialize the database")
        print("2. Add appointments for a month")
        print("3. Exit")
        choice = input("Enter your choice: ")
        if choice == '1':
            add_barbers_to_firestore(barbers_data)
        elif choice == '2':
            year = int(input("Enter year (e.g., 2024): "))
            month = int(input("Enter month (1-12): "))
            add_appointments_for_month(year, month)
        elif choice == '3':
            break
        else:
            print("Invalid choice. Please try again.")

if __name__ == '__main__':
    menu()
