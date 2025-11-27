@echo off
echo Testing Chart Generation API
echo ============================

REM Check if service is running
echo Checking if service is running...
curl -f http://localhost:8080/api/charts/health > nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Service is not running. Please start the application first:
    echo mvn spring-boot:run
    pause
    exit /b 1
)
echo Service is running!
echo.

REM Create output directory
if not exist "generated-charts" mkdir "generated-charts"

echo Generating charts...
echo.

REM Test 1: Monthly Sales Chart
echo 1. Generating Monthly Sales Chart...
curl -X POST http://localhost:8080/api/charts/generate ^
  -H "Content-Type: application/json" ^
  -d @sample-requests\monthly-sales.json ^
  --output "generated-charts\monthly-sales.png" ^
  --silent --show-error
if %errorlevel% equ 0 (
    echo ✓ Monthly Sales chart saved to: generated-charts\monthly-sales.png
) else (
    echo ✗ Failed to generate Monthly Sales chart
)

REM Test 2: Market Share Chart
echo 2. Generating Market Share Chart...
curl -X POST http://localhost:8080/api/charts/generate ^
  -H "Content-Type: application/json" ^
  -d @sample-requests\market-share.json ^
  --output "generated-charts\market-share.png" ^
  --silent --show-error
if %errorlevel% equ 0 (
    echo ✓ Market Share chart saved to: generated-charts\market-share.png
) else (
    echo ✗ Failed to generate Market Share chart
)

REM Test 3: Product Sales Chart
echo 3. Generating Product Sales Chart...
curl -X POST http://localhost:8080/api/charts/generate ^
  -H "Content-Type: application/json" ^
  -d @sample-requests\product-sales.json ^
  --output "generated-charts\product-sales.png" ^
  --silent --show-error
if %errorlevel% equ 0 (
    echo ✓ Product Sales chart saved to: generated-charts\product-sales.png
) else (
    echo ✗ Failed to generate Product Sales chart
)

REM Test 4: Analysis Only (no chart generation)
echo 4. Testing Analysis Endpoint...
curl -X POST http://localhost:8080/api/charts/analyze ^
  -H "Content-Type: application/json" ^
  -d @sample-requests\monthly-sales.json ^
  --silent | jq . > generated-charts\analysis-result.json 2>nul
if %errorlevel% equ 0 (
    echo ✓ Analysis result saved to: generated-charts\analysis-result.json
) else (
    echo ✗ Failed to get analysis result
)

echo.
echo Testing completed!
echo Check the 'generated-charts' folder for results.
echo Open the PNG files to view the generated charts.
echo.
pause
