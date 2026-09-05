@echo off
REM Room 209 Local Development Server

echo.
echo ================================
echo Room 209 Backend - Local Mode
echo ================================
echo.
echo Your IP: 192.168.0.41
echo Backend: http://192.168.0.41:8080
echo.
echo Make sure phones are on SAME WiFi network!
echo.

REM Navigate to backend
cd /d "%~dp0\backend"

REM Set Supabase connection string
REM REPLACE WITH YOUR ACTUAL SUPABASE CONNECTION STRING!
set DATABASE_URL=postgresql://postgres:YOUR_SUPABASE_PASSWORD@db.xxxxx.supabase.co:5432/postgres
set DATABASE_USER=postgres
set DATABASE_PASSWORD=YOUR_SUPABASE_PASSWORD
set SPRING_PROFILES_ACTIVE=default
set FIREBASE_ENABLED=false

echo Starting backend server...
echo Press Ctrl+C to stop

call gradlew bootRun

pause
