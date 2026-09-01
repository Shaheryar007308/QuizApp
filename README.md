# 🧠 Modern History Quiz App (Kotlin & Jetpack Compose)

A production-grade, full-featured Android application built with **Kotlin**, **Jetpack Compose**, **MVVM Architecture**, and **Clean Code principles**. 

The app features user authentication via **Firebase Auth**, dynamic question fetching from the **Open Trivia Database (OpenTDB) API**, local persistence with **Room Database**, and a custom **Glassmorphism UI** layered over dynamic background vector graphics.

---

## ✨ Features

- 🔐 **Firebase Authentication**: 
  - User Registration with Full Name, Email, Password, and Confirm Password validation.
  - Strict Email Verification enforcement (`user.isEmailVerified` required to log in).
  - Password visibility toggle (Eye icon) on all auth screens.
- 🎨 **Glassmorphic UI Design**:
  - Full-screen custom background support (`R.drawable.quiz_bg`).
  - Translucent Material 3 cards and buttons with custom border styling.
  - Dynamic option coloring during gameplay (Green for Correct, Red for Incorrect).
- ❓ **Anti-Duplicate Quiz Engine**:
  - OpenTDB Session Token integration ensuring users never see duplicate questions across game sessions.
  - Sequentially handles OpenTDB's strict 5-second per-IP rate limits to prevent API freezes.
  - Combines Multiple Choice and True/False questions (15 total per session) with HTML entity decoding (`&quot;`, `&#039;`, `&amp;`).
- 📊 **Score History & Local Persistence**:
  - Persists completed quiz results locally using **Room Database**.
  - Query filtering strictly ties quiz history records to the authenticated Firebase User ID (`userId`).
  - Supports individual history record deletion.
- 🧭 **Type-Safe Navigation**:
  - Navigation Compose leveraging Kotlin `@Serializable` routes.

---

## 🏗️ Architecture & Tech Stack

This project follows **Modern Android Development (MAD)** standards and **Clean MVVM Architecture**:

- **UI Layer**: Jetpack Compose, Material 3, StateFlow, ViewModel
- **Architecture**: MVVM + Repository Pattern + Clean Architecture
- **Asynchronous Work**: Kotlin Coroutines & Flow
- **Network**: Retrofit 2 + Gson Converter
- **Local Database**: Room Database (DAO, Entities, SQLite)
- **Backend**: Firebase Authentication SDK
- **Navigation**: Type-Safe Navigation Compose (`@Serializable`)

### 📂 Directory Structure


com.example.quizapp/
├── data/
│   ├── local/          # Room Entity, DAO, and Database configuration
│   ├── model/          # OpenTDB DTOs, Token Models, and UI Mappers
│   ├── network/        # Retrofit Interface & API Client Configuration
│   └── repository/     # AuthRepository & QuizRepository (Data Sources)
├── domain/             # Network Result Wrappers & Business Models
├── presentation/
│   ├── auth/           # Login & Register Screen Composables & AuthViewModel
│   ├── history/        # History Screen Composable & HistoryViewModel
│   ├── home/           # Dashboard Screen Composable
│   ├── quiz/           # Quiz Gameplay Screen Composable & QuizViewModel
│   └── result/         # Quiz Result Screen Composable
└── navigation/         # Type-Safe Route Definitions & NavGraph Setup

###  Clone 
git clone [https://github.com/YOUR_USERNAME/YOUR_REPOSITORY_NAME.git](https://github.com/YOUR_USERNAME/YOUR_REPOSITORY_NAME.git)
cd YOUR_REPOSITORY_NAME

## 👨‍💻 Developer & Author

**Shaheryar Mukhtar**
- **Role**: Junior Android Developer & Computer Science Undergraduate
- **Tech Stack**: Kotlin, Jetpack Compose, Android SDK, Firebase, Clean Architecture
- **GitHub**: [github.com/YOUR_GITHUB_USERNAME](https://github.com/Shaheryar007308)

---

*Built with Kotlin & Jetpack Compose.*
