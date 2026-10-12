# =============================================
#  ACMS - Build & Run Script
# =============================================

$SRC_DIR    = ".\src\main\java"
$OUT_DIR    = ".\out"
$MAIN_CLASS = "cli.MainCLI"

# Step 1: Clean previous compiled classes
Write-Host "Cleaning previous build..." -ForegroundColor Yellow
if (Test-Path $OUT_DIR) {
    Remove-Item -Path $OUT_DIR -Recurse -Force
    Write-Host "  Removed '$OUT_DIR'" -ForegroundColor DarkGray
}
New-Item -ItemType Directory -Path $OUT_DIR | Out-Null
Write-Host "  Created fresh '$OUT_DIR'" -ForegroundColor DarkGray

# Step 2: Collect all .java source files
Write-Host "Collecting source files..." -ForegroundColor Yellow
$files = Get-ChildItem -Path $SRC_DIR -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
Write-Host "  Found $($files.Count) .java files" -ForegroundColor DarkGray

# Step 3: Compile
Write-Host "Compiling..." -ForegroundColor Yellow
javac -d $OUT_DIR -sourcepath $SRC_DIR $files

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "BUILD FAILED. Fix the errors above and try again." -ForegroundColor Red
    exit 1
}

Write-Host "Build successful!" -ForegroundColor Green
Write-Host ""

# Step 4: Run
Write-Host "Starting ACMS..." -ForegroundColor Cyan
Write-Host "=============================================" -ForegroundColor DarkGray
java -cp $OUT_DIR $MAIN_CLASS
