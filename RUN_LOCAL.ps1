# Room 209 Local Development Server

Write-Host "================================" -ForegroundColor Cyan
Write-Host "Room 209 Backend - Local Mode" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Your PC IP: 192.168.0.41" -ForegroundColor Yellow
Write-Host "Backend URL: http://192.168.0.41:8080" -ForegroundColor Yellow
Write-Host ""
Write-Host "⚠️  Make sure both phones are on the SAME WiFi network!" -ForegroundColor Yellow
Write-Host ""

# Navigate to backend
Set-Location "$PSScriptRoot\backend"

# Set Supabase connection (using JDBC format with SSL for Supabase)
# REPLACE WITH YOUR ACTUAL SUPABASE CREDENTIALS!
$env:DATABASE_URL = "jdbc:postgresql://db.aokmhgciexlsdlaytlxf.supabase.co:5432/postgres?sslmode=require"
$env:DATABASE_USER = "postgres"
$env:DATABASE_PASSWORD = "Room-209Mehul"
$env:SPRING_PROFILES_ACTIVE = "default"
$env:FIREBASE_ENABLED = "false"

Write-Host "Starting backend server..." -ForegroundColor Green
Write-Host "Press Ctrl+C to stop" -ForegroundColor Gray
Write-Host ""

# Run backend
& ".\gradlew.bat" bootRun

Write-Host ""
Write-Host "Backend stopped." -ForegroundColor Red
