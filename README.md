<div align="center">

# 🌿 Plant E-Commerce App

### *Your smart plant companion — shop, care, and diagnose, all in one place.*

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Latest-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-Firestore_%7C_Auth-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Hilt](https://img.shields.io/badge/Hilt-DI-2196F3?style=for-the-badge&logo=google&logoColor=white)

<br/>

> A full-featured Android e-commerce and plant health application built entirely with **Jetpack Compose**, following modern Android architecture principles. Browse and purchase plants and gardening services, book appointments, manage your delivery addresses, and use an on-device AI camera to diagnose plant diseases — all wrapped in a polished, animation-rich UI.

</div>

---


## 📸 Screenshots

<div align="center">

| | | |
|:---:|:---:|:---:|
| <img width="200" alt="Screenshot 1" src="https://github.com/user-attachments/assets/926a913c-545e-4092-a76a-e225098cee12" /> | <img width="200" alt="Screenshot 2" src="https://github.com/user-attachments/assets/c27cc78d-c73a-4870-ad0e-e45b9ee7a023" /> | <img width="200" alt="Screenshot 3" src="https://github.com/user-attachments/assets/6c906618-e0d0-42a2-aae2-cd9bd16060e4" /> |
| <img width="200" alt="Screenshot 4" src="https://github.com/user-attachments/assets/73ff3ac6-cdba-4c73-8be0-a3e2fbef1dc7" /> | <img width="200" alt="Screenshot 5" src="https://github.com/user-attachments/assets/d27ada98-0f8d-4b8f-96ac-ba1d29fb480b" /> | <img width="200" alt="Screenshot 6" src="https://github.com/user-attachments/assets/bd2a40b4-abdc-4677-b730-7ea554d394f1" /> |
| <img width="200" alt="Screenshot 7" src="https://github.com/user-attachments/assets/1f3e8e2f-4b77-4bc1-88b3-b0a0da69592e" /> | <img width="200" alt="Screenshot 8" src="https://github.com/user-attachments/assets/9caff974-ed3f-4368-95f1-21c5d5e8d04e" /> | <img width="200" alt="Screenshot 9" src="https://github.com/user-attachments/assets/0830a578-9902-4568-ab6c-233c8f8e4f9e" /> |
| <img width="200" alt="Screenshot 10" src="https://github.com/user-attachments/assets/4d0cc156-d131-46e4-a811-3c256f4957ab" /> | <img width="200" alt="Screenshot 11" src="https://github.com/user-attachments/assets/4c3f6b12-dced-4373-b1cf-decf25743935" /> | <img width="200" alt="Screenshot 12" src="https://github.com/user-attachments/assets/3a588755-5f96-4ddb-aef2-23b06e3c2211" /> |

</div>

---

## ✨ Feature Highlights

| Feature | Description |
|---|---|
| 🛒 **Plant Shop** | Browse a live Firestore-backed catalog of plants with real-time search and category filtering |
| 🔧 **Services Marketplace** | Book gardening services (repotting, pest control, health checkups) with an interactive appointment calendar |
| 🔬 **AI Disease Detection** | Capture a leaf with CameraX and get instant disease diagnosis from a custom ML backend via Ktor |
| 🔐 **Authentication** | Full email/password auth with Firebase — supports both Customer and Partner (vendor) registration |
| 🛍️ **Smart Cart** | Unified cart for plants and services with animated quantity controls and INR pricing |
| 📍 **Location-Aware Addresses** | GPS-powered address auto-fill using FusedLocationProvider + Geocoder, with full Firestore persistence |
| 📦 **Order History** | View past orders fetched from Firestore, ordered chronologically |
| 🎨 **Expressive UI** | Shared element transitions, custom canvas vine animations, per-route nav animations, haptic feedback |

---

## 📸 Tech Stack

```
Language        → Kotlin
UI              → Jetpack Compose (Material 3)
Architecture    → MVVM + Clean Architecture (Repository Pattern)
DI              → Hilt (Dagger)
Backend         → Firebase Auth + Cloud Firestore
Networking      → Ktor CIO Client (for ML backend)
Image Loading   → Coil (with disk + memory caching)
Navigation      → Jetpack Navigation Compose (with SharedTransitionLayout)
Async           → Kotlin Coroutines + StateFlow + SharedFlow
Location        → Google Play Services FusedLocationProviderClient
Camera          → CameraX
Build           → Gradle (Kotlin DSL)
```

---

## 🏛️ Architecture

Built on **MVVM with a clean Repository layer**, scoped with Hilt for dependency injection.

```
┌─────────────────────────────────────────────────────────┐
│                        UI Layer                          │
│   Composable Screens  ←→  ViewModels (StateFlow/Events)  │
└────────────────────────────┬────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────┐
│                    Repository Layer                       │
│  AuthRepository · PlantRepository · CartRepository       │
│  LocationRepository · DiseaseRepository                  │
└────────────────────────────┬────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────┐
│                    Data Sources                           │
│   Firebase Firestore · Firebase Auth · Ktor HTTP         │
│   FusedLocationProvider · Geocoder · CameraX             │
└─────────────────────────────────────────────────────────┘
```

### ViewModel Scoping Strategy

A key architectural decision: most ViewModels are scoped to the **Activity** (`sharedActivityViewModel()`), preserving state across navigation without re-fetching. Only feature-isolated screens like `BookingViewModel` and `CameraViewModel` are screen-scoped via `hiltViewModel()`.

| ViewModel | Scope | Reason |
|---|---|---|
| `AuthViewModel` | Activity | Auth state shared app-wide |
| `HomeViewModel` | Activity | Prevents plant list re-fetch on back navigation |
| `DetailViewModel` | Activity | Passes selected plant/service to detail screens |
| `CartViewModel` | Activity | Cart persists throughout the session |
| `ProfileViewModel` | Activity | User/order data shared with profile sub-screens |
| `BookingViewModel` | Screen | Fresh calendar state per booking flow |
| `CameraViewModel` | Screen | Isolated camera lifecycle |

---

## 🗺️ Navigation

The entire app is managed by a single `NavHost` wrapped in `SharedTransitionLayout` to enable **shared element transitions** between list cards and detail screens.

```
SplashScreen  (animated logo + image preloading)
│
├──▶ AuthScreen  (Login / Sign Up — supports Partner registration)
│
└──▶ HomeScreen  (Shop · Camera · Services tabs)
     │
     ├──▶ PlantDetailScreen     (shared element from card → hero image)
     ├──▶ ServiceDetailScreen   (shared element → BookingScreen)
     │         └──▶ BookingScreen  (calendar + time slot picker)
     ├──▶ CartScreen            (animated quantity, checkout bar)
     └──▶ ProfileScreen
               ├──▶ AddressScreen    (modal bottom sheet editor, GPS fill)
               └──▶ OrderHistoryScreen
```

Every route has a **hand-crafted navigation animation** (see `NavAnimations.kt`), including a custom overshoot-easing bounce on the splash screen exit.

---

## 🔍 Core Modules

### 🌱 Plant Repository & Home Screen

`OfflinePlantRepository` registers Firestore **real-time snapshot listeners** on startup (within the application-scoped coroutine), meaning the plant and service lists update live without user-initiated refresh.

`HomeViewModel` combines three `StateFlow`s — plant list, search query, and selected category — using `combine {}` to derive a filtered `ImmutableList<Plant>` with zero boilerplate.

```kotlin
val filteredPlants: StateFlow<ImmutableList<Plant>> =
    combine(plantUiState, searchQuery, selectedCategory) { state, query, category ->
        if (state is PlantUiState.Success) {
            state.featuredPlants
                .filter { (query.isBlank() || it.name.contains(query, ignoreCase = true)) &&
                          (category.isBlank() || it.category == category) }
                .toImmutableList()
        } else persistentListOf()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), persistentListOf())
```

---

### 🔬 Plant Disease Detection

The camera flow uses **CameraX** to capture a full-resolution image, which is then sent as a multipart form upload to a local Python/FastAPI ML server over the local network via **Ktor CIO**.

```
CameraX Preview → Capture → File → Ktor POST (multipart/form-data)
                                          ↓
                                 ML Model (Python backend)
                                          ↓
                               PlantDiseaseResponse (JSON)
                                          ↓
                               DiseaseResultScreen (Healthy ✅ / Infected ❌)
```

`CameraViewModel` handles all states (`Idle → Loading → Success/Error`) and includes commented-out mock scenarios for offline development and testing.

The `DiseaseResultScreen` renders a **hero image** of the captured leaf with a sliding bottom sheet showing:
- Health status badge (green / red)
- AI confidence progress bar
- Symptoms and treatment cards (only shown when infected)

---

### 📅 Booking System

`BookingViewModel` implements a full **custom calendar** using `java.time` APIs — no third-party calendar library needed.

- Calculates `firstDayOfMonthOffset` using `dayOfWeek.value % 7` for correct Sunday-start grid alignment
- Generates hourly time slots (9am–5pm), auto-filtering past slots when today is selected
- `isPrevMonthEnabled` is derived lazily via `uiState.map {}` and exposed as a `StateFlow<Boolean>`
- Unavailable dates are defined as a list (ready to be swapped for a real backend call)

```kotlin
val isPrevMonthEnabled: StateFlow<Boolean> =
    uiState.map { it.currentDisplayMonth != YearMonth.now() }
           .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
```

On booking confirmation, the selected service is added to the cart and the user returns to the home screen — making the booking flow feel transactional and natural.

---

### 📍 Location & Address Management

`LocationRepository` uses `suspendCancellableCoroutine` to bridge the callback-based `FusedLocationProviderClient` into a clean coroutine suspension, then uses the `Geocoder` API to resolve coordinates into a human-readable `Address` model with street, city, and postal code.

The `AddressScreen` features a **`ModalBottomSheet` editor** with:
- Split fields: House/Flat number (always manual) + Street/Area (GPS-fillable)
- `FilterChip` label selector (Home / Work / Other)
- "Use Current Location" button that auto-fills area/street while intentionally leaving house number blank
- Inline validation with error display

---

### 🛒 Cart

`OfflineCartRepository` is a pure in-memory store backed by `MutableStateFlow<List<CartItem>>`. It uses `update {}` for thread-safe mutations and supports both `PlantItem` and `ServiceItem` sealed subtypes.

`CartViewModel` maps the raw list into a `CartUiState` that pre-computes:
- Separated `plantItems` and `serviceItems` lists (for sectioned display)
- Formatted INR total price (`NumberFormat.getCurrencyInstance`)

The `CartScreen` uses `Modifier.animateItem()` on list items and an `AnimatedContent` quantity counter with slide-up/slide-down transitions.

---

### 🔐 Authentication

`AuthViewModel` uses two separate reactive channels:
- `MutableStateFlow<AuthUiState>` — drives the form UI (fields, loading state, errors)
- `MutableSharedFlow<AuthResult>` — fires one-shot navigation events on success/failure

The sign-up flow supports **Partner registration** which requires a phone number and stores a `role: "partner"` field in the Firestore user document alongside standard customer accounts.

---

## 🎨 UI & Animation Details

### Shared Element Transitions

Plant and service cards animate into their detail screens using Compose's `SharedTransitionLayout` + `sharedBounds()`. Each card, image, and the "Add to Cart" / "Book Appointment" button participate in the transition with individual `zIndexInOverlay` values for correct layering.

### Navigation Animations

Every route has a purpose-built transition in `NavAnimations.kt`:

| Route | Animation |
|---|---|
| Splash → Auth/Home | Custom overshoot-bounce slide-up + fade |
| Home → Detail | Slide in from right + fade |
| Detail → back | Slide out to right + fade |
| Profile | Full-width slide from right |
| Cart | Partial slide from right |
| Booking / Camera | Slide up from bottom |

### Animated Vine

`AnimatedCornerVine` is a fully custom `Canvas` composable that draws animating vines along the top and left edges of the screen. It uses `PathEffect.dashPathEffect` with a phase offset to simulate a growing vine, with leaf circles appearing at predefined positions as the animation progresses.

### Haptic Feedback

`HapticHelper` provides a clean wrapper around the Android vibration APIs, with graceful version handling for `VibratorManager` (API 31+) vs the legacy `Vibrator` service. Three intensity levels are exposed: `light()`, `medium()`, `strong()`.

---

## 📁 Project Structure

```
com.example.app/
│
├── dependency/              # Hilt DI modules
│   ├── AppModule.kt         # PlantRepository, CartRepository, ImageLoader
│   ├── NetworkModule.kt     # Ktor HttpClient (CIO + ContentNegotiation + Logging)
│   ├── CoroutineModule.kt   # Application-scoped CoroutineScope
│   ├── LocationModule.kt    # FusedLocationProviderClient
│   └── DiseaseRepositoryModule.kt
│
├── repository/              # Data layer
│   ├── AuthRepository.kt
│   ├── PlantRepository.kt   # Firestore real-time listeners
│   ├── CartRepository.kt    # In-memory StateFlow cart
│   ├── LocationRepository.kt
│   └── DiseaseRepository.kt # Ktor multipart upload
│
├── model/                   # Data classes
│   ├── Plant.kt · Service.kt · Address.kt · Order.kt
│   └── PlantDiseaseResponse.kt
│
├── screens/
│   ├── authScreen/          # Login + Sign Up
│   ├── Homescreen/          # Plant list, search, categories, bottom nav
│   ├── Detailscreen/        # Plant & Service detail with shared transitions
│   ├── cameraScreen/        # CameraX + Disease detection result
│   ├── cart/                # Cart screen + checkout bar
│   ├── bookingScreen/       # Calendar + time slot picker
│   ├── addressScreen/       # Address list + bottom sheet editor
│   ├── ProfileScreen/       # Profile, order history
│   └── SplashScreen/
│
├── Miscellaneous/           # Reusable UI utilities
│   ├── AnimatedVine.kt      # Custom canvas vine animation
│   ├── HapticHelper.kt      # Vibration abstraction
│   └── PullState.kt
│
└── ui/navhost/              # Navigation
    ├── AppDestinations.kt   # Route enum
    ├── AppNav.kt            # NavHost + SharedTransitionLayout
    ├── NavAnimations.kt     # All custom enter/exit transitions
    └── AppRoot.kt
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- Android SDK 26+
- A Firebase project with **Authentication** (Email/Password) and **Firestore** enabled
- *(Optional)* A running instance of the plant disease detection ML backend

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/plant-ecommerce-app.git
   cd plant-ecommerce-app
   ```

2. **Add Firebase configuration**

   Download your `google-services.json` from the Firebase console and place it in the `app/` directory.

3. **Configure Firestore Collections**

   The app expects the following top-level collections:
   ```
   plants/     → Plant documents
   services/   → Service documents
   users/      → User profiles (auto-created on sign-up)
     └── {uid}/addresses/   → Saved addresses
     └── {uid}/orders/      → Order history
   ```

4. **Configure the ML Backend** *(optional)*

   In `DiseaseRepository.kt`, update the endpoint URL to point to your running server:
   ```kotlin
   client.post("http://<YOUR_LOCAL_IP>:8081/api/v1/detect-disease")
   ```
   To test without a server, uncomment the mock scenario block in `CameraViewModel.kt`.

5. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```

---

## 🔮 Roadmap

- [ ] Checkout flow with Razorpay/UPI payment integration
- [ ] Push notifications for order status updates
- [ ] Partner-side dashboard for managing bookings
- [ ] Plant care reminders and watering schedule tracker
- [ ] Offline-first cart persistence with Room
- [ ] Admin CMS for plant and service catalog management

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome. Please open an issue first to discuss what you'd like to change.

---

<div align="center">

Built with ❤️ and a lot of ☕ by **Mayur**

*If you found this project interesting, please consider giving it a ⭐*

</div>
