# Room 209 Deployment Checklist

## Quick Links
- **GitHub**: https://github.com/mehulprajapati2610/Room-209
- **Supabase**: https://supabase.com
- **Firebase**: https://firebase.google.com/console
- **Render**: https://render.com

---

## Phase 1: Supabase ⬜
- [ ] Sign up / Login to Supabase
- [ ] Create new project named `room-209`
- [ ] Save database password securely
- [ ] Wait for database to provision
- [ ] Copy connection URL (with password replaced)
- [ ] Open SQL Editor and run `backend/src/main/resources/schema.sql`
- [ ] Verify schema created (check tables in "Explore" tab)

**Connection URL Template:**
```
postgresql://postgres:YOUR_PASSWORD@db.xxxxx.supabase.co:5432/postgres
```

**Save this URL!** → You need it for Render env vars

---

## Phase 2: Firebase ⬜
- [ ] Sign up / Login to Firebase
- [ ] Create new project named `room-209`
- [ ] Uncheck "Enable Google Analytics"
- [ ] Wait for Firebase project to initialize
- [ ] Go to **Project Settings** → **Service Accounts**
- [ ] Click **"Generate new private key"**
- [ ] Save downloaded JSON file securely
- [ ] Go to **Build** → **Cloud Messaging** → Enable
- [ ] Go to **Project Settings** → **General**
- [ ] Under "Your apps", click **"Add app"** → **Android**
- [ ] Package name: `com.room209.app`
- [ ] Click **"Register app"**
- [ ] Download `google-services.json`
- [ ] Move to: `android/app/google-services.json`
- [ ] Commit to Git and push:
  ```powershell
  cd "e:\Projects\Room 209"
  git add android/app/google-services.json
  git commit -m "Add Firebase config"
  git push origin main
  ```

**Keep secure:**
- ✓ `firebase-service-account.json` (for Render)
- ✓ `android/app/google-services.json` (already in Git)

---

## Phase 3: Render Backend Deployment ⬜
- [ ] Sign up / Login to Render
- [ ] Connect GitHub account
- [ ] Click **"New"** → **"Web Service"**
- [ ] Select repo: `room-209`
- [ ] Service name: `room209-backend`
- [ ] Environment: Docker (auto)
- [ ] Click **"Create Web Service"**
- [ ] While building, click **"Settings"** tab
- [ ] Add these **Environment Variables**:

```
DATABASE_URL = postgresql://postgres:PASSWORD@db.xxxxx.supabase.co:5432/postgres
DATABASE_USER = postgres
DATABASE_PASSWORD = YOUR_PASSWORD
SPRING_PROFILES_ACTIVE = prod
FIREBASE_ENABLED = false
JWT_SECRET = your-super-secret-key-at-least-32-chars
CLOUDINARY_CLOUD_NAME = demo
CLOUDINARY_API_KEY = sample_key
CLOUDINARY_API_SECRET = sample_secret
```

- [ ] Wait for build to complete (3-5 min)
- [ ] Check status is green **"Live"**
- [ ] Note your Render URL: `https://room209-backend.onrender.com`
- [ ] Test in browser: `https://room209-backend.onrender.com/api/v1/me`
- [ ] Should redirect to login (that's correct!)

---

## Phase 4: Android App Build ⬜
- [ ] Open Android Studio
- [ ] Open `android/app/build.gradle.kts`
- [ ] Add Firebase plugin to plugins section:
  ```kotlin
  id("com.google.gms.google-services")
  ```
- [ ] Update prod flavor URLs (if Render URL changed):
  ```kotlin
  buildConfigField("String", "BACKEND_BASE_URL", "\"https://room209-backend.onrender.com/api/v1/\"")
  buildConfigField("String", "WS_BASE_URL", "\"wss://room209-backend.onrender.com/ws-room\"")
  ```
- [ ] Build → **Generate Signed Bundle/APK**
- [ ] Choose **APK**
- [ ] Select keystore: `android/release.keystore`
- [ ] Build variant: `prodRelease`
- [ ] Click Finish
- [ ] APK location: `android/app/build/outputs/apk/prod/release/app-prod-release.apk`

---

## Phase 5: Test & Share ⬜
- [ ] Connect Android phone via USB
- [ ] Enable USB Debugging on phone
- [ ] Click Run (green play button)
- [ ] App installs on your phone
- [ ] Login with: `marcus@room209.internal` / `pass123`
- [ ] Test features:
  - [ ] See Home screen
  - [ ] See Feed screen
  - [ ] Post to feed (text + optional image)
  - [ ] Toggle chores
  - [ ] Check presence status (IN ROOM, AWAY, etc.)
- [ ] Send APK to roommate via:
  - [ ] Telegram
  - [ ] Google Drive
  - [ ] WhatsApp
  - [ ] Bluetooth
  - [ ] USB Transfer
- [ ] Roommate installs APK:
  - [ ] Open file
  - [ ] Click "Install anyway" (unknown source warning)
  - [ ] Wait for installation
- [ ] Roommate logs in with: `alex@room209.internal` / `pass123`
- [ ] Both phones visible in Feed posts
- [ ] Both can see each other's real-time updates

---

## Test Accounts Available

| Email | Password | Bed | Role |
|-------|----------|-----|------|
| marcus@room209.internal | pass123 | 1 | Lead |
| alex@room209.internal | pass123 | 2 | Resident |
| dev@room209.internal | pass123 | 3 | Resident |
| sam@room209.internal | pass123 | 4 | Resident |

---

## Environment Variables Summary

**Render needs these (copy from Supabase):**
- `DATABASE_URL` - Connection string
- `DATABASE_USER` - postgres
- `DATABASE_PASSWORD` - Your Supabase password

**Render needs these (random values are fine for MVP):**
- `JWT_SECRET` - Any random 32+ char string
- `SPRING_PROFILES_ACTIVE` - prod
- `FIREBASE_ENABLED` - false (for MVP, can be true later)

**Optional (free tier defaults):**
- `CLOUDINARY_*` - Use demo credentials

---

## After Deployment

✅ **What's working:**
- Backend running on Render (free tier)
- Database on Supabase (500MB free)
- Both phones syncing in real-time
- All features functional (posts, chores, plans, polls)
- Notifications disabled (Firebase optional)

📊 **Cost:** $0/month

🚀 **Next steps:**
1. Both phones use app for your room
2. Add more roommates by creating new accounts
3. Monitor Render logs for issues
4. Add Firebase credentials when needed (for push notifications)

---

## Critical Files to Keep Secure

⚠️ **DO NOT COMMIT THESE:**
- `firebase-service-account.json` (keep in Render env only)
- `signing.properties` (already in .gitignore)
- `release.keystore` (already in .gitignore)
- `.env` files (if created)

✅ **Safe to commit:**
- `android/app/google-services.json` (Firebase Android config)
- Source code
- Build configs

---

## Helpful Commands

**Check Git status:**
```powershell
cd "e:\Projects\Room 209"
git status
```

**See Render logs in browser:**
- Go to https://render.com
- Click your service
- Click **Logs** tab

**Test backend from browser:**
```
https://room209-backend.onrender.com/api/v1/me
(should redirect to login)
```

**Check Android logs while running:**
```powershell
adb logcat | Select-String "room209"
```

---

## Support

- 📖 Full guide: `DEPLOYMENT_GUIDE.md`
- ⚡ Quick steps: `QUICK_DEPLOYMENT_STEPS.md`
- 📋 This checklist: `DEPLOYMENT_CHECKLIST.md`
- 🐛 Code: `https://github.com/mehulprajapati2610/Room-209`

