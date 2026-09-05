# Room 209 — Local Deployment (No Render, No Studio)

Deploy to 2 phones using local backend + GitHub Actions for APK building.

---

## ✅ What You Have
- ✅ Supabase database ready
- ✅ Firebase configured
- ✓ GitHub repo with code
- ✗ No Render deployment needed
- ✗ No Android Studio needed

---

## 🎯 Setup (30 minutes)

### Phase 1: Update Android App Config (5 min)

Edit `android/app/build.gradle.kts` prod flavor:

**Find this:**
```kotlin
create("prod") {
    dimension = "environment"
    buildConfigField("String", "BACKEND_BASE_URL", "\"https://room209-backend.onrender.com/api/v1/\"")
    buildConfigField("String", "WS_BASE_URL", "\"wss://room209-backend.onrender.com/ws-room\"")
}
```

**Replace with your PC's local IP** (get it from: `ipconfig` in PowerShell, look for `IPv4 Address`):

Example: `192.168.1.100` (yours will be different)

```kotlin
create("prod") {
    dimension = "environment"
    buildConfigField("String", "BACKEND_BASE_URL", "\"http://192.168.1.100:8080/api/v1/\"")
    buildConfigField("String", "WS_BASE_URL", "\"ws://192.168.1.100:8080/ws-room\"")
}
```

---

### Phase 2: Update Backend Config (5 min)

Edit `backend/src/main/resources/application.yml`:

Find:
```yaml
datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/room209}
```

And add your Supabase connection string:

```yaml
datasource:
    url: ${DATABASE_URL:postgresql://postgres:YOUR_PASSWORD@db.xxxxx.supabase.co:5432/postgres}
    username: ${DATABASE_USER:postgres}
    password: ${DATABASE_PASSWORD:YOUR_PASSWORD}
```

---

### Phase 3: Start Backend Locally (5 min)

Open PowerShell:

```powershell
cd "e:\Projects\Room 209\backend"
./gradlew bootRun
```

Wait for:
```
Started Room209Application in X seconds
```

Backend is now running at `http://localhost:8080` ✅

---

### Phase 4: Build APK via GitHub Actions (10 min)

1. Commit and push your changes:
```powershell
cd "e:\Projects\Room 209"
git add .
git commit -m "Configure local backend URLs and GitHub Actions build"
git push origin main
```

2. Create a Git tag to trigger build:
```powershell
git tag v1.0.0
git push origin v1.0.0
```

3. Go to GitHub Actions:
   - URL: https://github.com/mehulprajapati2610/Room-209/actions
   - Wait for build to complete (2-3 min)
   - Should show green checkmark ✅

4. Get APK:
   - Go to **Releases**: https://github.com/mehulprajapati2610/Room-209/releases
   - Download `app-prod-release.apk`
   - Save to your Downloads or Desktop

---

## 📱 Install on Phones

### Your Phone (Connected to PC via USB):
```powershell
adb install -r Downloads\app-prod-release.apk
```

### Roommate's Phone:
1. Send APK via Telegram/Drive/etc.
2. Open file on phone
3. Click "Install anyway" (unknown source)
4. Done!

---

## 🧪 Test

Both phones on same WiFi:

1. **Your phone**: Login with `marcus@room209.internal` / `pass123`
2. **Roommate's phone**: Login with `alex@room209.internal` / `pass123`
3. **Your phone**: Create a post in feed
4. **Roommate's phone**: Should see it appear instantly ✅

---

## ⚠️ Important Notes

### Backend must stay running
- Keep PowerShell window open: `./gradlew bootRun`
- If you close it, app disconnects
- Each time you restart, backend is fresh (all data resets)

### Both phones need WiFi to same network
- Your PC WiFi: Where backend runs
- Phones WiFi: Must be same network as PC
- Not mobile data — same WiFi!

### To make it persistent later
- Run backend on cheap cloud host (AWS Free Tier, DigitalOcean $5/mo, etc.)
- Or use cheap VPS ($3-5/mo)

---

## 🔄 Update APK After Code Changes

1. Change code locally
2. Push to GitHub:
   ```powershell
   git add .
   git commit -m "Your changes"
   git push origin main
   ```
3. Create new tag:
   ```powershell
   git tag v1.0.1
   git push origin v1.0.1
   ```
4. GitHub builds automatically
5. Download new APK from Releases

---

## 💾 Test Accounts

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

## 🎯 Next Steps (When Ready for Persistence)

- Cheap backend hosts:
  - **Railway.app** - $5/mo (better than Render)
  - **Fly.io** - Free tier + pay-as-you-go
  - **AWS LightSail** - $3.50/mo fixed
  - **DigitalOcean App Platform** - $5/mo minimum

- For now, local backend works great for testing!

---

## 📊 Cost Summary

| Component | Cost |
|-----------|------|
| Supabase | $0 (free 500MB) |
| Firebase | $0 (free) |
| Backend | $0 (runs on your PC) |
| GitHub Actions | $0 (free) |
| **Total** | **$0/month** |

---

Easy deployment! No Studio, no Render. Just GitHub Actions. 🚀

