# ✅ Backend is RUNNING! Next Steps (10 minutes)

## 🎉 Status

✅ Backend running at: **http://192.168.0.41:8080**
✅ Supabase database connected
✅ Firebase configured
✅ GitHub Actions ready

---

## 📱 NOW DO THIS:

### Step 1: Build APK via GitHub Actions (5 min)

Keep backend running in PowerShell. Open a NEW PowerShell window:

```powershell
cd "e:\Projects\Room 209"
git tag v1.0.0
git push origin v1.0.0
```

Go to: https://github.com/mehulprajapati2610/Room-209/actions
- Wait for build to finish (green checkmark) - 2-3 minutes
- Go to: https://github.com/mehulprajapati2610/Room-209/releases
- Download `app-prod-release.apk`

### Step 2: Install APK on Your Phone (3 min)

**Make sure:**
- ✅ Backend is still running
- ✅ Your phone is on SAME WiFi as your PC

**Connect phone via USB:**
```powershell
adb install -r Downloads\app-prod-release.apk
```

**Or manually:**
1. Copy APK file to phone
2. Open file manager
3. Tap APK file
4. Click "Install anyway" (unknown source)
5. Done!

### Step 3: Test on Your Phone (2 min)

1. Open Room 209 app
2. Login: `marcus@room209.internal` / `pass123`
3. Should see Home screen ✅
4. If error "Can't connect", check:
   - Is backend still running? (check PowerShell window)
   - Is phone on same WiFi as PC?
   - Can you ping 192.168.0.41 from phone?

### Step 4: Install on Roommate's Phone (1 min)

1. Send APK via Telegram/Drive/etc.
2. Roommate opens APK → "Install anyway"
3. Roommate logs in: `alex@room209.internal` / `pass123`
4. **Both phones on same WiFi!**

### Step 5: Test Real-time Sync

1. Your phone: Post something in Feed
2. Roommate's phone: Should see post instantly ✅

---

## 🧪 Test Accounts

```
marcus@room209.internal / pass123    (Your phone)
alex@room209.internal / pass123      (Roommate's phone)
```

---

## ⚠️ IMPORTANT

**Backend must stay running!**
- Don't close PowerShell window
- Keep it running while using the app
- If you close it, app disconnects

---

## 🎯 Expected Result

✅ Both phones show Room 209 app
✅ Both connected to same backend
✅ Real-time sync (feed, chores, plans)
✅ **$0 cost!**

---

That's it! You're done! 🎉

