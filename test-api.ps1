# Test Script for Chart Generation API
# Run this script after starting the Spring Boot application

# Check if the service is running
Write-Host "Testing Chart Generation API..." -ForegroundColor Green
Write-Host "Checking service health..." -ForegroundColor Yellow

try {
    $healthResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/charts/health" -Method GET
    Write-Host "✓ Service is running: $($healthResponse.Content)" -ForegroundColor Green
} catch {
    Write-Host "✗ Service is not running. Please start the application first." -ForegroundColor Red
    Write-Host "Run: mvn spring-boot:run" -ForegroundColor Yellow
    exit 1
}

Write-Host "`nTesting chart generation endpoints..." -ForegroundColor Yellow

# Test 1: Monthly Sales (Bar Chart)
Write-Host "`n1. Generating Monthly Sales Chart..." -ForegroundColor Cyan
try {
    Invoke-WebRequest -Uri "http://localhost:8080/api/charts/generate" `
        -Method POST `
        -ContentType "application/json" `
        -InFile "samples\monthly_sales.json" `
        -OutFile "output\monthly_sales.png"
    Write-Host "✓ Monthly sales chart generated: output\monthly_sales.png" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to generate monthly sales chart: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Browser Share (Pie Chart)
Write-Host "`n2. Generating Browser Share Chart..." -ForegroundColor Cyan
try {
    Invoke-WebRequest -Uri "http://localhost:8080/api/charts/generate" `
        -Method POST `
        -ContentType "application/json" `
        -InFile "samples\browser_share.json" `
        -OutFile "output\browser_share.png"
    Write-Host "✓ Browser share chart generated: output\browser_share.png" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to generate browser share chart: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Financial Performance (Multi-series)
Write-Host "`n3. Generating Financial Performance Chart..." -ForegroundColor Cyan
try {
    Invoke-WebRequest -Uri "http://localhost:8080/api/charts/generate" `
        -Method POST `
        -ContentType "application/json" `
        -InFile "samples\financial_performance.json" `
        -OutFile "output\financial_performance.png"
    Write-Host "✓ Financial performance chart generated: output\financial_performance.png" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to generate financial performance chart: $($_.Exception.Message)" -ForegroundColor Red
}
[
# Test 4: Product Comparison
Write-Host "`n4. Generating Product Comparison Chart..." -ForegroundColor Cyan
try {
    Invoke-WebRequest -Uri "http://localhost:8080/api/charts/generate" `
        -Method POST `
        -ContentType "application/json" `
        -InFile "samples\product_comparison.json" `
        -OutFile "output\product_comparison.png"
    Write-Host "✓ Product comparison chart generated: output\product_comparison.png" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to generate product comparison chart: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Demographics (Pie Chart)
Write-Host "`n5. Generating Demographics Chart..." -ForegroundColor Cyan
try {
    Invoke-WebRequest -Uri "http://localhost:8080/api/charts/generate" `
        -Method POST `
        -ContentType "application/json" `
        -InFile "samples\demographics.json" `
        -OutFile "output\demographics.png"
    Write-Host "✓ Demographics chart generated: output\demographics.png" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to generate demographics chart: $($_.Exception.Message)" -ForegroundColor Red
}

# Test Analysis Endpoint
Write-Host "`n6. Testing Analysis Endpoint..." -ForegroundColor Cyan
try {
    $analysisResult = Invoke-RestMethod -Uri "http://localhost:8080/api/charts/analyze" `
        -Method POST `
        -ContentType "application/json" `
        -InFile "samples\monthly_sales.json"

    Write-Host "✓ Analysis completed:" -ForegroundColor Green
    Write-Host "  Chart Type: $($analysisResult.chartType)" -ForegroundColor White
    Write-Host "  Title: $($analysisResult.title)" -ForegroundColor White
    Write-Host "  Reasoning: $($analysisResult.reasoning)" -ForegroundColor White
} catch {
    Write-Host "✗ Failed to analyze data: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest completed! Check the 'output' folder for generated charts." -ForegroundColor Green
Write-Host "Open the PNG files to view the generated charts." -ForegroundColor Yellow
  {"quarter": "Q1", "revenue": 45000, "expenses": 32000, "profit": 13000},
  {"quarter": "Q2", "revenue": 52000, "expenses": 35000, "profit": 17000},
  {"quarter": "Q3", "revenue": 48000, "expenses": 33000, "profit": 15000},
  {"quarter": "Q4", "revenue": 58000, "expenses": 38000, "profit": 20000}
]

