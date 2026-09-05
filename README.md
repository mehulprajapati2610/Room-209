# Room 209 — Hostel Communal Living Application

> **Warm Minimalist Living** · Private Hostel Residence Suite 209  
> Designed 1:1 to match the Stitch UI design system (`projects/5385559759393937784`).

---

## 🏛️ Architecture & Tech Stack

- **Frontend**: Android Native with **Kotlin** & **Jetpack Compose (Material 3)**
  - **Typography**: Manrope typescale (Display, Headlines, Body, `labelCaps` tracked uppercase)
  - **Color Tokens**: Warm Minimalist palette (Canvas `#FAF8F5`, Terracotta Umber `#8C4A2F`, Forest Bronze `#384C38`, Hairline `#EAE6DF`)
  - **Linear Iconography**: Strict No-Emoji Standard with custom 1.5dp stroke geometric vector icons
  - **Networking**: Retrofit 2 + Moshi + OkHttp 3
  - **Realtime Client**: OkHttp STOMP over WebSocket
  - **Image Pipeline**: Coil Compose with Cloudinary CDN integration
  - **Push**: Firebase Cloud Messaging (FCM)

- **Backend**: **Java 17** + **Spring Boot 3.3**
  - **Security**: Stateless Spring Security with JWT Bearer tokens
  - **Database**: **PostgreSQL** with Spring Data JPA & Hibernate
  - **Realtime Broker**: Spring WebSocket (`@EnableWebSocketMessageBroker`) with STOMP sub-protocol (`/ws-room`, `/topic/room.{id}.*`)
  - **Media Engine**: Cloudinary Java SDK with automated folder routing & signature generation
  - **Notifications**: Firebase Admin SDK (`FCMService`) for background alerts

---

## 📱 Stitch UI Screens Implemented

1. **Home Screen** (`816efda5c0f34924abdbe7ad54932ee8`)
   - Header with Room 209 identifier & profile trigger
   - **Residents Bar**: Horizontal scroll of roommates with live status beads (`IN ROOM`, `AWAY`, `QUIET`, `DND`)
   - **Quiet Hours Banner**: Minimalist dial indicator showing active status & schedule (`11:00 PM – 7:00 AM`)
   - **Announcements**: Pinned notices & alerts with empty state
   - **Chores & Duties**: Live checklist with one-tap status toggling

2. **Communal Feed Screen** (`1c822d1896a5444f84d9372921c07e41`)
   - Filter chips: `ALL`, `ANNOUNCEMENT`, `MAINTENANCE`, `CHIT-CHAT`
   - Editorial post stream with author initials, bed tag, Cloudinary photo attachments, like counter, and replies

3. **Share to Feed Screen** (`4e4f54d160ab49f683ef3e5ccd2d42d6`)
   - Minimalist writing canvas, category selector, image upload, and terracotta publish CTA

4. **Create Action Sheet** (`ae9f2e1f6b3f45349aa99beb215616df`)
   - 20dp top rounded bottom sheet with 4 action triggers:
     - `POST TO FEED`
     - `ASSIGN CHORE`
     - `SCHEDULE PLAN`
     - `CREATE POLL`

5. **Plans Screen** (`232b9a3d61d747028ecb1bc52d778890`)
   - Group outings, study sessions, and room events with RSVP tracking (`ATTENDING`, `DECLINED`)

6. **Lounge & Fun Screen** (`2f2a150e87e84bc79c9d732a7f336002`)
   - Live roommate poll card with interactive voting percentages and room protocol guidelines

7. **Resident Profile Screen** (`c61e2ddddc044f5a9a0f906861167265`)
   - User identity, bed assignment, live presence status switcher, and notification toggles

---

## 🚀 Running the Backend (Spring Boot 3)

### 1. Database Setup
Create the PostgreSQL database:
```sql
CREATE DATABASE room209;
```
*(Or use Docker: `docker run --name postgres-room209 -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=room209 -p 5432:5432 -d postgres:16`)*

### 2. Launch Server
Navigate to `backend/` and run:
```bash
cd backend
gradlew bootRun
```
The server will start on port `8080`.
On first run, `DatabaseSeeder` will initialize **Room 209** and create 4 resident test accounts with feeds, chores, plans, and polls remaining clean and dynamic.

### Seeded Test Accounts:
- `marcus@room209.internal` / `pass123` (Marcus Reed · Lead · Bed 1)
- `alex@room209.internal` / `pass123` (Alex Chen · Resident · Bed 2)
- `dev@room209.internal` / `pass123` (Dev Patel · Resident · Bed 3)
- `sam@room209.internal` / `pass123` (Sam Taylor · Resident · Bed 4)

---

## 📱 Running the Android App (Android Studio)

1. Open **Android Studio**.
2. Select **Open** and select the folder: `e:\Projects\Room 209\android`.
3. Allow Gradle to sync.
4. Run on an Android Emulator or physical device (Target SDK 34, Android 14).
   - *Note: Android Studio emulator automatically points `http://10.0.2.2:8080/api/v1/` to your host machine.*

---

## 🔑 Generating Signed Release APK

The project includes a pre-generated production keystore (`release.keystore`) and `signing.properties` configured in `app/build.gradle.kts`.

To build the signed release APK, run:
```bash
cd android
gradlew assembleRelease
```
The signed APK will be output at:
```text
android/app/build/outputs/apk/release/app-release.apk
```
