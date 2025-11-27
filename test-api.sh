#!/bin/bash

# Test Script for Chart Generation API
# Run this script after starting the Spring Boot application

echo "Testing Chart Generation API..."
echo "Checking service health..."

# Check if the service is running
if curl -s -f "http://localhost:8080/api/charts/health" > /dev/null; then
    echo "✓ Service is running"
else
    echo "✗ Service is not running. Please start the application first."
    echo "Run: mvn spring-boot:run"
    exit 1
fi

echo ""
echo "Testing chart generation endpoints..."

# Create output directory
mkdir -p output

# Test 1: Monthly Sales (Bar Chart)
echo ""
echo "1. Generating Monthly Sales Chart..."
if curl -X POST "http://localhost:8080/api/charts/generate" \
    -H "Content-Type: application/json" \
    -d @samples/monthly_sales.json \
    --output output/monthly_sales.png \
    --silent --fail; then
    echo "✓ Monthly sales chart generated: output/monthly_sales.png"
else
    echo "✗ Failed to generate monthly sales chart"
fi

# Test 2: Browser Share (Pie Chart)
echo ""
echo "2. Generating Browser Share Chart..."
if curl -X POST "http://localhost:8080/api/charts/generate" \
    -H "Content-Type: application/json" \
    -d @samples/browser_share.json \
    --output output/browser_share.png \
    --silent --fail; then
    echo "✓ Browser share chart generated: output/browser_share.png"
else
    echo "✗ Failed to generate browser share chart"
fi

# Test 3: Financial Performance (Multi-series)
echo ""
echo "3. Generating Financial Performance Chart..."
if curl -X POST "http://localhost:8080/api/charts/generate" \
    -H "Content-Type: application/json" \
    -d @samples/financial_performance.json \
    --output output/financial_performance.png \
    --silent --fail; then
    echo "✓ Financial performance chart generated: output/financial_performance.png"
else
    echo "✗ Failed to generate financial performance chart"
fi

# Test 4: Product Comparison
echo ""
echo "4. Generating Product Comparison Chart..."
if curl -X POST "http://localhost:8080/api/charts/generate" \
    -H "Content-Type: application/json" \
    -d @samples/product_comparison.json \
    --output output/product_comparison.png \
    --silent --fail; then
    echo "✓ Product comparison chart generated: output/product_comparison.png"
else
    echo "✗ Failed to generate product comparison chart"
fi

# Test 5: Demographics (Pie Chart)
echo ""
echo "5. Generating Demographics Chart..."
if curl -X POST "http://localhost:8080/api/charts/generate" \
    -H "Content-Type: application/json" \
    -d @samples/demographics.json \
    --output output/demographics.png \
    --silent --fail; then
    echo "✓ Demographics chart generated: output/demographics.png"
else
    echo "✗ Failed to generate demographics chart"
fi

# Test Analysis Endpoint
echo ""
echo "6. Testing Analysis Endpoint..."
analysis_result=$(curl -X POST "http://localhost:8080/api/charts/analyze" \
    -H "Content-Type: application/json" \
    -d @samples/monthly_sales.json \
    --silent)

if [ $? -eq 0 ]; then
    echo "✓ Analysis completed:"
    echo "$analysis_result" | jq '.'
else
    echo "✗ Failed to analyze data"
fi

echo ""
echo "Test completed! Check the 'output' folder for generated charts."
echo "Open the PNG files to view the generated charts."
