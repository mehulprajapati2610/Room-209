# 🚀 Room 209 — START HERE

Your app is ready to deploy to production. Here's what's done and what you need to do.

---

## ✅ What's Already Done (By Me)

1. **Code is on GitHub**
   - Repository: https://github.com/mehulprajapati2610/Room-209
   - 143 files committed (backend, Android, configs, guides)

2. **Backend Configured for Cloud**
   - Environment variables ready for Supabase/Render
   - Docker setup complete
   - Firebase FCM integration ready
   - Production profile (`application-prod.yml`) ready

3. **Android App Ready**
   - Product flavors (dev/prod) configured
   - Prod flavor points to Render cloud URLs
   - Firebase plugin ready to add
   - Signed APK setup ready

4. **Documentation**
   - `QUICK_DEPLOYMENT_STEPS.md` - Step-by-step instructions
   - `DEPLOYMENT_GUIDE.md` - Full deployment guide
   - `DEPLOYMENT_CHECKLIST.md` - Checklist with quick links

---

## ⚡ What YOU Need to Do (60 minutes)

### Phase 1: Supabase Database (10 min)
**Read**: `QUICK_DEPLOYMENT_STEPS.md` → **Phase 1**

Quick summary:
1. Go to https://supabase.com
2. Create project named `room-209`
3. Copy connection URL
4. Run schema from: `backend/src/main/resources/schema.sql`

**Save the connection URL** - you need it for Render!

---

### Phase 2: Firebase (15 min)
**Read**: `QUICK_DEPLOYMENT_STEPS.md` → **Phase 2**

Quick summary:
1. Go to https://firebase.google.com/console
2. Create project named `room-209`
3. Download `firebase-service-account.json`
4. Create Android app in Firebase
5. Download `google-services.json`
6. Move to: `android/app/google-services.json`
7. Commit and push to GitHub

---

### Phase 3: Render Backend (20 min)
**Read**: `QUICK_DEPLOYMENT_STEPS.md` → **Phase 3**

Quick summary:
1. Go to https://render.com
2. Create web service from your GitHub repo
3. Add environment variables (from Supabase connection URL)
4. Deploy (Render auto-builds from Docker)
5. Save Render URL: `https://room209-backend.onrender.com`

---

### Phase 4: Android APK Build (10 min)
**Read**: `QUICK_DEPLOYMENT_STEPS.md` → **Phase 4**

Quick summary:
1. Add Firebase plugin to `android/app/build.gradle.kts`
2. Verify prod flavor has correct Render URL
3. Build → Generate Signed APK (prodRelease variant)
4. APK location: `android/app/build/outputs/apk/prod/release/app-prod-release.apk`

---

### Phase 5: Test & Share (5 min)
**Read**: `QUICK_DEPLOYMENT_STEPS.md` → **Phase 5**

Quick summary:
1. Test APK on your phone (login with `marcus@room209.internal` / `pass123`)
2. Send APK to roommate (Telegram, Drive, etc.)
3. Roommate installs and logs in with `alex@room209.internal` / `pass123`
4. Done! ✅

---

## 📱 Test Accounts (Already in Database)

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

## 🎯 Expected Result After 60 Minutes

✅ Backend running on Render cloud (https://room209-backend.onrender.com)
✅ Database on Supabase (free 500MB tier)
✅ Your phone has working app (logged in)
✅ Roommate's phone has same app (logged in)
✅ Both phones see each other's posts, chores, plans in real-time
✅ **Cost: $0/month** (all free tiers)

---

## 📚 Documentation Files

Read in this order:
1. **This file** - Overview
2. **QUICK_DEPLOYMENT_STEPS.md** - Follow these steps exactly
3. **DEPLOYMENT_CHECKLIST.md** - Check off each step
4. **DEPLOYMENT_GUIDE.md** - Detailed reference guide

---

## 🐛 If Something Goes Wrong

**Check these first:**
1. Supabase connection URL — Is password correct?
2. Render environment variables — Any typos?
3. Firebase Android config — Is `google-services.json` in right place?
4. Render logs — Check for build/runtime errors
5. Android logs — `adb logcat | Select-String "room209"`

**Get help:**
- Render logs: https://render.com (go to service → Logs)
- Supabase logs: https://supabase.com (check SQL errors)
- Firebase logs: https://firebase.google.com/console

---

## 🚀 After Deployment

**Your app is production-ready!**

Features working:
- ✅ Real-time feed (posts, comments)
- ✅ Chore management with live status
- ✅ Group plans with RSVP
- ✅ Polls with voting
- ✅ Presence status (IN ROOM, AWAY, QUIET, DND)
- ✅ Image uploads to Cloudinary
- ✅ WebSocket real-time sync
- ✅ JWT authentication
- ⚠️ Push notifications (Firebase optional, can add later)

---

## 💡 Pro Tips

1. **If Render free tier sleeps** — App will take 30s to wake up on first request. That's normal.

2. **More roommates** — Create new accounts:
   - You: marcus@room209.internal
   - Roommate: alex@room209.internal
   - Or make more test accounts in database

3. **Backup your files**:
   ```
   firebase-service-account.json  ⚠️ KEEP SECURE
   release.keystore               ⚠️ KEEP SECURE
   Google Services JSON           ✅ In Git (safe)
   ```

4. **Future updates** — Push changes to GitHub, Render auto-redeploys

5. **Monitor costs** — Should always be $0/month with free tiers

---

## ❓ Questions?

- Full deployment guide: `DEPLOYMENT_GUIDE.md`
- Step-by-step checklist: `DEPLOYMENT_CHECKLIST.md`
- GitHub issues: https://github.com/mehulprajapati2610/Room-209/issues
- Code: https://github.com/mehulprajapati2610/Room-209

---

## ⏱️ Time Estimate

- Supabase: 10 min
- Firebase: 15 min
- Render: 20 min
- Android build: 10 min
- Test & share: 5 min
- **Total: 60 minutes**

---

## ✨ You're Ready!

Everything is set up. Follow `QUICK_DEPLOYMENT_STEPS.md` and you'll have your app running on two phones in 1 hour.

Let's go! 🚀

