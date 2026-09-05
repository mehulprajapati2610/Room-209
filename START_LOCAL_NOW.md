# 🚀 Room 209 — START LOCAL NOW (30 minutes)

You have Supabase + Firebase ready. Now get it running on 2 phones using local backend.

---

## ✅ What You Have
- ✅ Supabase database 
- ✅ Firebase project
- ✅ Code on GitHub
- ✅ Your PC: 192.168.0.41
- ✅ GitHub Actions ready to build APK

---

## 🎯 Do This Now (3 steps, 30 min)

### Step 1: Update Backend Config (2 min)

Go to Supabase dashboard:
1. Click **Settings** → **Database**
2. Copy your **Connection string**
3. Save it somewhere

Edit `RUN_LOCAL.ps1` (in project root):

Find these lines:
```powershell
$env:DATABASE_URL = "postgresql://postgres:YOUR_SUPABASE_PASSWORD@db.xxxxx.supabase.co:5432/postgres"
$env:DATABASE_USER = "postgres"
$env:DATABASE_PASSWORD = "YOUR_SUPABASE_PASSWORD"
```

Replace with your actual Supabase details:
```powershell
$env:DATABASE_URL = "postgresql://postgres:your_actual_password@db.xxxxx.supabase.co:5432/postgres"
$env:DATABASE_USER = "postgres"
$env:DATABASE_PASSWORD = "your_actual_password"
```

**Save file.**

---

### Step 2: Start Backend (2 min)

Open PowerShell:

```powershell
cd "e:\Projects\Room 209"
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope Process
.\RUN_LOCAL.ps1
```

Wait for:
```
Started Room209Application in X seconds
```

✅ Backend running at `http://192.168.0.41:8080`

**Keep this PowerShell window open!**

---

### Step 3: Build & Deploy APK (10 min)

Commit your changes:
```powershell
# In NEW PowerShell window (keep backend running in first one!)
cd "e:\Projects\Room 209"
git add .
git commit -m "Configure local backend IP"
git push origin main
```

Create a Git tag to trigger GitHub Actions:
```powershell
git tag v1.0.0
git push origin v1.0.0
```

GitHub Actions will automatically build APK:
1. Go to: https://github.com/mehulprajapati2610/Room-209/actions
2. Wait for green checkmark ✅ (2-3 min)
3. Go to: https://github.com/mehulprajapati2610/Room-209/releases
4. Download `app-prod-release.apk`

---

### Step 4: Install on Phones (10 min)

**Your phone:**
```powershell
# Make sure phone is connected via USB with USB Debugging enabled
adb install -r Downloads\app-prod-release.apk
```

**Roommate's phone:**
1. Send APK via Telegram/Google Drive/etc.
2. Open file on phone
3. Click "Install anyway"
4. Done!

---

### Step 5: Test (5 min)

**IMPORTANT: Both phones must be on SAME WiFi as your PC!**

1. **Your phone**: Open app → Login `marcus@room209.internal` / `pass123`
2. **Roommate's phone**: Open app → Login `alex@room209.internal` / `pass123`
3. **Your phone**: Post something in Feed
4. **Roommate's phone**: Should see it instantly! ✅

---

## 🧪 Test Accounts

```
marcus@room209.internal / pass123
alex@room209.internal / pass123
dev@room209.internal / pass123
sam@room209.internal / pass123
```

---

## ⚠️ Important

### Backend must stay running
- Don't close the PowerShell window with `RUN_LOCAL.ps1`
- When it stops, app disconnects
- If you close it, data resets each time (fresh database on restart)

### Both phones need same WiFi
- Not mobile data
- Must be same network as your PC
- Test: Can phones ping your PC? (ping 192.168.0.41)

### Making it permanent later
When you want backend to always be running:
- Use cheap VPS (Railway, Fly.io, DigitalOcean $3-5/mo)
- Or AWS Free Tier
- For now, local testing is perfect!

---

## 📝 Troubleshooting

### Backend won't start
**Problem**: `Failed to connect to database`
**Solution**: Check DATABASE_URL is correct in `RUN_LOCAL.ps1`

### Phones can't connect
**Problem**: `Connection refused` or timeout
**Solution**: 
- Are phones on same WiFi as PC? 
- Is backend still running?
- Try: `ping 192.168.0.41` from phone

### GitHub Actions fails
**Problem**: Red X on build
**Solution**: Check logs at https://github.com/mehulprajapati2610/Room-209/actions

---

## 🎯 Summary

| Task | Time | Status |
|------|------|--------|
| Update backend config | 2 min | 📝 Do this |
| Start backend | 2 min | ▶️ Do this |
| Push code + tag | 2 min | 📤 Do this |
| GitHub build | 3 min | ⏳ Automatic |
| Download APK | 1 min | 📥 Do this |
| Install phones | 10 min | 📱 Do this |
| Test sync | 5 min | ✅ Verify |
| **Total** | **~30 min** | 🚀 Ready! |

---

## 💡 Next Time You Update Code

1. Make changes locally
2. Commit and push:
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
5. Download new APK

---

## 📊 Cost

- Supabase: $0
- Firebase: $0
- Backend: $0 (runs on your PC)
- GitHub Actions: $0
- **Total: $0/month** ✅

---

Ready? Start with Step 1! 🚀

