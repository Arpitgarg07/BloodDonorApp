<div align="center">

<img src="https://img.shields.io/badge/Platform-Android-red?style=for-the-badge&logo=android&logoColor=white" />
<img src="https://img.shields.io/badge/Firebase-Firestore-orange?style=for-the-badge&logo=firebase&logoColor=white" />
<img src="https://img.shields.io/badge/Flutter-Dart-blue?style=for-the-badge&logo=flutter&logoColor=white" />
<img src="https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge" />

<br/><br/>

# 🩸 Blood Donor Finder

### _Connecting donors to those in need — fast, free, and life-saving._

**[📱 Download App](https://i.apponthego.com/a78b0)** • **[⭐ Star this Repo](https://github.com/Arpitgarg07/BloodDonorApp)**

</div>

---

## 📖 About

**Blood Donor Finder** is a mobile application that bridges the critical gap between blood donors and recipients. In emergency situations, finding the right blood group quickly can be the difference between life and death. This app enables anyone to find verified blood donors nearby by blood group and city, and also lets willing donors register themselves to help others.

Built as a **Third Year Mobile Application Lab Project** at Poornima Institute of Engineering & Technology, Jaipur (Academic Year 2025–26).

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔍 **Smart Search** | Search donors by blood group (A+, A−, B+, B−, O+, O−, AB+, AB−) |
| 🏙️ **City Filter** | Filter donors by city for nearby results |
| 📋 **Donor Profiles** | View donor details — name, age, blood group, city |
| 📞 **One-Tap Call** | Call a donor directly from the app |
| 💬 **WhatsApp Message** | Message donors via WhatsApp instantly |
| ✍️ **Donor Registration** | Register as a donor with name, age, phone, city & blood group |
| ✅ **Availability Status** | Donors can mark themselves as available to donate |
| 🔥 **Firebase Backend** | Real-time data sync powered by Firebase Firestore |
| 📊 **Donor Stats** | See total donors, cities listed, and available donors |

---

## 📱 Screenshots

<div align="center">

| Splash Screen | Donor List | Donor Detail | Contact |
|---|---|---|---|
| App launches with branding | Browse & filter donors | Full donor info | Call or WhatsApp |

> _Screenshots show the live app with real donor data from Jaipur._

</div>

---

## 🗺️ User Flow

```
App Opens
    │
    ├── PATH 1: Find a Donor
    │       └── Home Screen (select blood group + city)
    │               └── Apply Filters
    │                       └── Donor List Screen
    │                               └── Select Donor
    │                                       └── Donor Detail Screen
    │                                               ├── 📞 Call Button
    │                                               └── 💬 WhatsApp Button
    │
    └── PATH 2: Register as Donor
            └── Register Form Screen (Name, Age, Phone, City, Blood Group)
                        └── Submit → Validation
                                    ├── Invalid Data → Re-enter
                                    └── Valid Data → Save to Firebase → ✅ Success
```

---

## 🛠️ Tech Stack

- **Frontend:** Flutter (Dart)
- **Backend / Database:** Firebase Firestore
- **Authentication:** Firebase Auth
- **Communication:** Android Intent API (Call & WhatsApp)
- **Platform:** Android

---

## 🚀 Getting Started

### Prerequisites

- Flutter SDK (≥ 3.0)
- Android Studio / VS Code
- A Firebase project with Firestore enabled

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/Arpitgarg07/BloodDonorApp.git

# 2. Navigate to project directory
cd BloodDonorApp

# 3. Install dependencies
flutter pub get

# 4. Connect your Firebase project
# Add your google-services.json in android/app/

# 5. Run the app
flutter run
```

---

## 🔥 Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project
3. Enable **Cloud Firestore** in your project
4. Download `google-services.json` and place it inside `android/app/`
5. Set Firestore rules to allow read/write during development:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

> ⚠️ Update rules before production deployment.

---

## 📂 Project Structure

```
BloodDonorApp/
├── lib/
│   ├── main.dart              # Entry point
│   ├── screens/
│   │   ├── home_screen.dart       # Donor search & filter
│   │   ├── donor_list_screen.dart # List of matching donors
│   │   ├── donor_detail_screen.dart # Donor profile & contact
│   │   └── register_screen.dart   # Donor registration form
│   ├── models/
│   │   └── donor.dart         # Donor data model
│   └── services/
│       └── firebase_service.dart  # Firestore operations
├── android/
│   └── app/
│       └── google-services.json  # Firebase config
└── pubspec.yaml
```

---

## 🧩 Data Model

Each donor document in Firestore has the following structure:

```json
{
  "name": "Arpit Garg",
  "age": 19,
  "phone": "8377181470",
  "city": "Jaipur",
  "bloodGroup": "B+",
  "availableToDonate": true
}
```

---

## 🗺️ Roadmap

- [x] Donor registration with Firebase
- [x] Search & filter by blood group and city
- [x] Call & WhatsApp integration
- [x] Donor availability toggle
- [ ] Push notifications for urgent blood requests
- [ ] Location-based donor discovery (GPS)
- [ ] Donor history & donation tracking
- [ ] iOS support

---

## 👨‍💻 Author

**Arpit Garg**
- 🎓 PIET23CS025 — Department of Computer Engineering
- 🏫 Poornima Institute of Engineering & Technology, Jaipur
- 📧 Submitted to: Ms. Bersha Kumari (Assistant Professor)

---

## 📄 License

This project is developed for academic purposes under the Mobile Application Lab curriculum (Academic Year 2025–26) at Poornima Institute of Engineering & Technology.

---

<div align="center">

Made with ❤️ and 🩸 in Jaipur

**[📱 Try the App](https://i.apponthego.com/a78b0)** • **[🐛 Report an Issue](https://github.com/Arpitgarg07/BloodDonorApp/issues)**

</div>
