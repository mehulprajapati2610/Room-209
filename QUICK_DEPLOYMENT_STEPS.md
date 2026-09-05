# 🚀 Room 209 — Quick Deployment (60 minutes)

Your code is on GitHub. Now deploy to Render + Supabase + Firebase in 4 phases.

---

## ✅ Phase 1: Supabase Database (10 minutes)

### 1. Create Supabase Project
- Go to [supabase.com](https://supabase.com)
- Click **"New Project"**
- **Project name**: `room-209`
- **Database password**: Create strong password (save it!)
- **Region**: Pick closest to you (e.g., `us-east-1`)
- Click **"Create new project"** (wait 2-3 min)

### 2. Get Connection String
- In Supabase dashboard, go: **Settings** → **Database**
- Copy **Connection string** (looks like):
```
postgresql://postgres:PASSWORD@db.xxxxx.supabase.co:5432/postgres
```
- Replace `PASSWORD` with your actual database password
- **Save this URL** - you'll need it for Render

### 3. Load Database Schema
- In Supabase, go: **SQL Editor** → **New query**
- Open this file: `backend/src/main/resources/schema.sql`
- Copy all content
- Paste into Supabase SQL editor
- Click **Run** (wait for completion)

✅ **Database ready!**

---

## ✅ Phase 2: Firebase Project (15 minutes)

### 1. Create Firebase Project
- Go to [firebase.google.com/console](https://firebase.google.com/console)
- Click **"Add project"**
- **Project name**: `room-209`
- Uncheck "Enable Google Analytics" (to save costs)
- Click **"Create project"** (wait for setup)

### 2. Generate Service Account Key
1. Go: **Project Settings** (gear icon top-right)
2. Click **"Service Accounts"** tab
3. Click **"Generate new private key"**
4. Save file as: `firebase-service-account.json`
5. **KEEP THIS SECRET!** Don't commit to Git.

### 3. Enable Cloud Messaging
1. Go: **Build** → **Cloud Messaging**
2. Click the notification icon to enable FCM
3. Copy **Server API Key** (you may not need it for this setup)

### 4. Android Firebase Config
1. Go: **Project Settings** → **General**
2. Under "Your apps", click **"Add app"** → Choose **Android**
3. **Android package name**: `com.room209.app`
4. Click **"Register app"**
5. Click **"Download google-services.json"**
6. Move downloaded file to: `android/app/google-services.json`
7. Commit to Git:
```powershell
cd "e:\Projects\Room 209"
git add android/app/google-services.json
git commit -m "Add Firebase Google Services config"
git push origin main
```

✅ **Firebase ready!**

---

## ✅ Phase 3: Render Deployment (20 minutes)

### 1. Create Render Account & Connect GitHub
- Go to [render.com](https://render.com)
- Sign up (free tier)
- Click **"Connect GitHub"** and authorize

### 2. Deploy Backend Service
1. In Render dashboard, click **"New"** → **"Web Service"**
2. Select your GitHub repo: `room-209`
3. **Settings**:
   - **Name**: `room209-backend`
   - **Environment**: Docker (auto-detected)
   - **Region**: Pick closest to you
   - **Plan**: Free
4. Click **"Create Web Service"**

### 3. Add Environment Variables
While service is building, go to **Settings** tab and add these variables:

```
DATABASE_URL = postgresql://postgres:PASSWORD@db.xxxxx.supabase.co:5432/postgres
DATABASE_USER = postgres
DATABASE_PASSWORD = YOUR_PASSWORD
SPRING_PROFILES_ACTIVE = prod
FIREBASE_ENABLED = true
JWT_SECRET = your-super-secret-key-12345-change-this-to-32-chars-min
CLOUDINARY_CLOUD_NAME = demo
CLOUDINARY_API_KEY = sample_key
CLOUDINARY_API_SECRET = sample_secret
```

**Replace:**
- `PASSWORD` → your Supabase database password
- `db.xxxxx.supabase.co` → your Supabase host
- `JWT_SECRET` → any random 32+ character string (for security)

### 4. Upload Firebase Credentials
1. On Render service page, go to **Files** (if available)
2. Or use **"Env Group"** to create a shared credentials file
3. Create file: `firebase-service-account.json`
4. Copy content from file you downloaded earlier
5. Save

**⚠️ Alternative if no file upload:**
- Skip Firebase credentials upload for now
- Set `FIREBASE_ENABLED = false` in env vars
- Push notifications will be disabled, but app still works
- You can add Firebase later

### 5. Wait for Deploy
- Render will auto-build from GitHub
- Build takes 3-5 minutes
- Once live, you'll see green "Live" status
- URL will be: `https://room209-backend.onrender.com`

### 6. Verify Backend is Running
- Visit: `https://room209-backend.onrender.com/api/v1/me`
- Should show a login redirect (that's correct!)
- If error: check Render logs for issues

✅ **Backend running on cloud!**

---

## ✅ Phase 4: Build & Deploy Android App (15 minutes)

### 1. Add Firebase to Android Build
Open `android/app/build.gradle.kts` and add Firebase plugin at top:

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("com.google.gms.google-services")  // ADD THIS LINE
}
```

Then in root `android/build.gradle.kts`, add in plugins section:

```kotlin
plugins {
    id("com.google.gms.google-services") version "4.4.1" apply false
}
```

### 2. Update Render URL (if it changed)
In `android/app/build.gradle.kts`, find the prod flavor and update:

```kotlin
create("prod") {
    dimension = "environment"
    buildConfigField("String", "BACKEND_BASE_URL", "\"https://room209-backend.onrender.com/api/v1/\"")
    buildConfigField("String", "WS_BASE_URL", "\"wss://room209-backend.onrender.com/ws-room\"")
}
```

### 3. Build Production APK
1. Open Android Studio
2. Go: **Build** → **Generate Signed Bundle/APK**
3. Choose **APK**
4. **Keystore path**: Click "Choose existing..." → select `android/release.keystore`
5. **Keystore password**: `android` (or whatever was set)
6. **Key alias**: `release` (or whatever was set)
7. **Build variant**: Select `prodRelease` (NOT debug)
8. Click **Finish**

### 4. APK Location
- Once built, APK is at:
```
android/app/build/outputs/apk/prod/release/app-prod-release.apk
```

### 5. Test on Your Phone
1. Connect your Android phone to computer via USB
2. Enable "USB Debugging" on phone (Settings → About → tap Build Number 7 times → go back to Developer Options)
3. In Android Studio, click **Run** (green play button)
4. Select your device
5. App installs and opens
6. **Login with test account**:
   - Email: `marcus@room209.internal`
   - Password: `pass123`
7. Test basic features (posts, chores, etc.)

✅ **App running on your phone!**

---

## ✅ Phase 5: Share APK with Roommate (5 minutes)

### Option 1: Direct File Transfer
1. APK file: `android/app/build/outputs/apk/prod/release/app-prod-release.apk`
2. Send via:
   - **Telegram** (easiest)
   - **Google Drive**
   - **OneDrive**
   - **Bluetooth**
   - **USB cable**

### Option 2: GitHub Releases (Better for future updates)
1. Go to: https://github.com/mehulprajapati2610/Room-209
2. Click **Releases** → **Create a new release**
3. Tag: `v1.0.0`
4. Upload APK file
5. Publish
6. Share release link with roommate

### Roommate Installation Steps:
1. Receive APK file
2. Open file manager on Android phone
3. Tap on APK file
4. Android will warn "Unknown source" → tap **"Install anyway"**
5. Wait for installation
6. App opens
7. Login with test account (same as yours)

✅ **Both phones synced!**

---

## 🧪 Test Accounts (Already Seeded)

```
Account 1 (You):
Email: marcus@room209.internal
Pass: pass123
Bed: 1

Account 2 (Roommate):
Email: alex@room209.internal
Pass: pass123
Bed: 2

Account 3:
Email: dev@room209.internal
Pass: pass123
Bed: 3

Account 4:
Email: sam@room209.internal
Pass: pass123
Bed: 4
```

---

## ✔️ Verification Checklist

- [ ] Supabase database created & schema loaded
- [ ] Firebase project created & credentials saved
- [ ] Firebase Android config downloaded
- [ ] Render backend deployed & running (check: https://room209-backend.onrender.com/api/v1/me)
- [ ] Android Firebase plugin added to build.gradle
- [ ] Prod APK built successfully
- [ ] APK tested on your phone (logged in, can see home screen)
- [ ] APK shared with roommate
- [ ] Roommate installed APK
- [ ] Both phones logged in (different accounts)
- [ ] Both phones can see each other's posts/chores in real-time

---

## 🆘 Troubleshooting

### Backend won't start on Render
**Solution:**
1. Go to Render dashboard → your service
2. Click **Logs** tab
3. Look for errors (usually database connection issue)
4. Check environment variables are exactly correct
5. Most common: `DATABASE_URL` has wrong password

### App can't connect to backend
**Solution:**
1. Check prod flavor URL is correct: `https://room209-backend.onrender.com`
2. Verify Render service is showing "Live" status
3. Test in browser: `https://room209-backend.onrender.com/api/v1/me` (should redirect to login)
4. Check Android logs: `adb logcat | grep "room209"`

### Firebase not working
**Solution:**
- If not critical for MVP, set `FIREBASE_ENABLED = false` on Render
- App still works fine, just no push notifications
- Add Firebase credentials later when you have time

### APK won't install on roommate's phone
**Solution:**
1. Phone settings → **Apps** → enable **"Unknown sources"**
2. Try transferring via different method (Telegram, Drive, etc.)
3. Check APK is using `prodRelease` variant

---

## 📊 Expected Costs (Monthly)

| Service | Free Tier | Cost |
|---------|-----------|------|
| Supabase PostgreSQL | 500MB/month | **$0** |
| Render Backend | 750 dyno-hours | **$0** |
| Firebase | Unlimited messages | **$0** |
| Cloudinary | 10GB + 2GB/month | **$0** |
| **TOTAL** | | **$0/month** |

---

## 🎯 Next Steps After Deployment

1. Both phones syncing → ✅ Done!
2. Test all features (feed posts, chores, polls, plans)
3. Monitor Render logs for issues
4. If push notifications needed → add Firebase credentials later
5. Ready to invite more roommates!

---

Good luck! You're deploying a production-ready hostel app. 🎉

