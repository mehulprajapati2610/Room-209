# Room 209 — Cloud Deployment Guide

Deploy your hostel app to run on two phones with free cloud infrastructure.

---

## Phase 1: GitHub Setup (5 minutes)

### Step 1A: Create GitHub Repository
1. Go to [github.com/new](https://github.com/new)
2. Repository name: `room-209`
3. **Do NOT** initialize with README (we already have one)
4. Click "Create repository"
5. Copy the repository URL (looks like `https://github.com/YOUR_USERNAME/room-209.git`)

### Step 1B: Push Local Code to GitHub
In PowerShell, from workspace root:
```powershell
cd "e:\Projects\Room 209"
git remote add origin https://github.com/YOUR_USERNAME/room-209.git
git branch -M main
git push -u origin main
```

**Result**: Your entire codebase is now on GitHub ✓

---

## Phase 2: Supabase Database (10 minutes)

### Step 2A: Create Supabase Project
1. Go to [supabase.com](https://supabase.com)
2. Sign up (free tier)
3. Click "New Project"
4. **Project name**: `room-209`
5. **Database password**: Save this securely!
6. **Region**: Choose closest to you (e.g., `us-east-1` for US)
7. Click "Create new project" (wait 2-3 min for provisioning)

### Step 2B: Get Connection URL
1. In Supabase dashboard, go to **Settings** → **Database**
2. Copy the **Connection string** (URI format)
3. Replace `[YOUR-PASSWORD]` with your actual database password
4. Should look like: `postgresql://postgres:YOUR_PASSWORD@db.xxxxx.supabase.co:5432/postgres`

### Step 2C: Initialize Database Schema
1. Go to **SQL Editor** in Supabase
2. Create a new query
3. Copy entire content from `backend/src/main/resources/schema.sql`
4. Paste into SQL editor and click **Run**

**Result**: PostgreSQL database ready with Room 209 schema ✓

---

## Phase 3: Firebase Project (15 minutes)

### Step 3A: Create Firebase Project
1. Go to [firebase.google.com](https://firebase.google.com)
2. Click "Go to console"
3. Click "Add project"
4. **Project name**: `room-209`
5. Uncheck "Enable Google Analytics" (to stay free)
6. Click "Create project" (wait for setup)

### Step 3B: Enable Cloud Messaging
1. In Firebase console, go to **Build** → **Cloud Messaging**
2. Click the notification icon to enable
3. Copy **Server API Key** (for backend)

### Step 3C: Generate Service Account Key
1. Go to **Project Settings** (gear icon, top right)
2. Click **Service Accounts** tab
3. Click "Generate new private key"
4. Save file as `firebase-service-account.json`
5. **IMPORTANT**: Keep this secret!

### Step 3D: Android Firebase Config
1. Go to **Project Settings** → **General** tab
2. Under "Your apps", click "Add app" → Select **Android**
3. **Android package name**: `com.room209.app`
4. Register app
5. Download `google-services.json`
6. Move to: `android/app/google-services.json`

**Result**: Firebase configured for push notifications ✓

---

## Phase 4: Render Deployment (20 minutes)

### Step 4A: Create Render Account
1. Go to [render.com](https://render.com)
2. Sign up (free tier)
3. Connect your GitHub account

### Step 4B: Create Backend Web Service
1. In Render dashboard, click **New** → **Web Service**
2. Select your GitHub repository (`room-209`)
3. Choose `backend` directory
4. **Name**: `room209-backend`
5. **Environment**: Docker (auto-detected)
6. **Plan**: Free tier

### Step 4C: Set Environment Variables
In Render dashboard, go to your service's **Environment** tab and add:

```
DATABASE_URL=postgresql://postgres:YOUR_PASSWORD@db.xxxxx.supabase.co:5432/postgres
DATABASE_USER=postgres
DATABASE_PASSWORD=YOUR_PASSWORD
SPRING_PROFILES_ACTIVE=prod
FIREBASE_ENABLED=true
JWT_SECRET=your-super-secret-jwt-key-change-this
CLOUDINARY_CLOUD_NAME=demo
CLOUDINARY_API_KEY=sample_key
CLOUDINARY_API_SECRET=sample_secret
```

Replace:
- `YOUR_PASSWORD` with Supabase database password
- `db.xxxxx.supabase.co` with your Supabase host
- `your-super-secret-jwt-key-change-this` with a random 32-char string

### Step 4D: Upload Firebase Credentials
1. Copy content of `firebase-service-account.json`
2. In Render, go to **Files** tab (if available)
3. Or use "Build & Deploy" hooks to inject credentials
4. Alternative: Use Render's **Deploy Hook** to set file content

**Note**: For MVP, you can temporarily disable Firebase (`FIREBASE_ENABLED=false`) if credentials upload is complex.

### Step 4E: Deploy
1. Click **Deploy** button
2. Wait for build to complete (3-5 min)
3. Once live, Render will show URL: `https://room209-backend.onrender.com`

**Result**: Backend running on cloud ✓

---

## Phase 5: Android App Build (10 minutes)

### Step 5A: Update Backend URL (if changed from default)
Edit `android/app/build.gradle.kts`, update prod flavor URL:
```kotlin
buildConfigField("String", "BACKEND_BASE_URL", "\"https://YOUR_RENDER_URL/api/v1/\"")
buildConfigField("String", "WS_BASE_URL", "\"wss://YOUR_RENDER_URL/ws-room\"")
```

### Step 5B: Add Firebase Config
1. Ensure `android/app/google-services.json` exists (from Step 3D)
2. In `android/app/build.gradle.kts`, add Firebase plugin:
```kotlin
id("com.google.gms.google-services")
```

3. In project-level `build.gradle.kts`, add:
```kotlin
plugins {
    id("com.google.gms.google-services") version "4.4.1" apply false
}
```

### Step 5C: Build Production APK
In Android Studio:
1. **Build** → **Generate Signed Bundle/APK**
2. Choose **APK**
3. Select keystore: `android/release.keystore`
4. **Build variant**: `prodRelease` (production flavor)
5. Build complete → APK at `android/app/build/outputs/apk/prod/release/app-prod-release.apk`

### Step 5D: Share APK with Roommate
1. Send `app-prod-release.apk` via file transfer (AirDrop, Telegram, Drive, etc.)
2. Roommate opens on Android phone
3. Phone may warn "Unknown source" → Click "Install anyway"
4. App opens → Login with any of 4 test accounts (see README)

**Result**: Both phones running same app, same backend ✓

---

## Test Accounts (Seeded)

```
Email: marcus@room209.internal
Pass: pass123

Email: alex@room209.internal
Pass: pass123

Email: dev@room209.internal
Pass: pass123

Email: sam@room209.internal
Pass: pass123
```

---

## Verification Checklist

- [ ] Code pushed to GitHub
- [ ] Supabase database created & schema loaded
- [ ] Firebase project created with service account key
- [ ] Android Firebase config added
- [ ] Render backend deployed & running
- [ ] Backend health check: `https://room209-backend.onrender.com/api/v1/me` (redirects to login)
- [ ] APK built with prod flavor
- [ ] APK tested on your phone
- [ ] APK installed on roommate's phone
- [ ] Both phones logged in and syncing

---

## Troubleshooting

### Backend won't start on Render
- Check Environment variables (typos in DATABASE_URL?)
- View Render logs: Dashboard → Service → Logs
- Verify Supabase connection string format

### App can't connect to backend
- Check prod flavor URLs in `build.gradle.kts`
- Verify Render URL is correct: `https://room209-backend.onrender.com`
- Check Android app logs: `adb logcat | grep Room209`

### Push notifications not working
- Verify Firebase is enabled in Render env: `FIREBASE_ENABLED=true`
- Check Firebase service account key is mounted correctly
- Verify Android device has Firebase token: App Settings → Check logs

### Database migrations not running
- In Render, set `HIBERNATE_DDL_AUTO=update` (or is in prod profile)
- Check Supabase SQL logs for errors

---

## Cost Summary (Monthly)

| Service | Free Tier | Cost |
|---------|-----------|------|
| Supabase PostgreSQL | 500MB storage | $0 |
| Render Web Service | 750 dyno-hours | $0 |
| Firebase Cloud Messaging | Unlimited messages | $0 |
| Cloudinary (images) | 10GB + 2GB/month | $0 |
| **Total** | | **$0** |

---

## Next Steps

1. Follow Phase 1-5 above
2. Test with both phones
3. Monitor backend logs on Render for issues
4. Share feedback or bugs

Good luck! 🚀

