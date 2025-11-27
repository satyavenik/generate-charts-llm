# Sample Requests for Chart Generation API

This file contains various sample requests you can use to test the Chart Generation API endpoints.

## 1. Simple Bar Chart - Monthly Sales Data

**Endpoint:** `POST /api/charts/generate`

```json
{
  "data": {
    "January": 15000,
    "February": 18000,
    "March": 22000,
    "April": 19000,
    "May": 25000,
    "June": 28000
  },
  "title": "Monthly Sales Performance",
  "description": "Sales revenue for the first half of 2024"
}
```

**cURL Command (Windows CMD):**
```cmd
curl -X POST http://localhost:8080/api/charts/generate ^
  -H "Content-Type: application/json" ^
  -d "{\"data\":{\"January\":15000,\"February\":18000,\"March\":22000,\"April\":19000,\"May\":25000,\"June\":28000},\"title\":\"Monthly Sales Performance\",\"description\":\"Sales revenue for the first half of 2024\"}" ^
  --output monthly-sales.png
```

**PowerShell Command:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/charts/generate" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"data":{"January":15000,"February":18000,"March":22000,"April":19000,"May":25000,"June":28000},"title":"Monthly Sales Performance","description":"Sales revenue for the first half of 2024"}' `
  -OutFile "monthly-sales.png"
```

---

## 2. Pie Chart - Market Share Distribution

**Endpoint:** `POST /api/charts/generate`

```json
{
  "data": {
    "iOS": 27.5,
    "Android": 71.2,
    "Others": 1.3
  },
  "title": "Mobile OS Market Share",
  "description": "Global mobile operating system market share percentage"
}
```

**cURL Command:**
```cmd
curl -X POST http://localhost:8080/api/charts/generate ^
  -H "Content-Type: application/json" ^
  -d "{\"data\":{\"iOS\":27.5,\"Android\":71.2,\"Others\":1.3},\"title\":\"Mobile OS Market Share\",\"description\":\"Global mobile operating system market share percentage\"}" ^
  --output market-share.png
```

---

## 3. Line Chart - Time Series Data

**Endpoint:** `POST /api/charts/generate`

```json
{
  "data": {
    "2019": 1200,
    "2020": 1450,
    "2021": 1680,
    "2022": 1950,
    "2023": 2300,
    "2024": 2750
  },
  "title": "Company Revenue Growth",
  "description": "Annual revenue trend over 6 years showing steady growth"
}
```

**cURL Command:**
```cmd
curl -X POST http://localhost:8080/api/charts/generate ^
  -H "Content-Type: application/json" ^
  -d "{\"data\":{\"2019\":1200,\"2020\":1450,\"2021\":1680,\"2022\":1950,\"2023\":2300,\"2024\":2750},\"title\":\"Company Revenue Growth\",\"description\":\"Annual revenue trend over 6 years showing steady growth\"}" ^
  --output revenue-growth.png
```

---

## 4. Multi-Series Data - Comparative Analysis

**Endpoint:** `POST /api/charts/generate`

```json
{
  "data": [
    {"quarter": "Q1", "revenue": 45000, "expenses": 32000, "profit": 13000},
    {"quarter": "Q2", "revenue": 52000, "expenses": 35000, "profit": 17000},
    {"quarter": "Q3", "revenue": 48000, "expenses": 33000, "profit": 15000},
    {"quarter": "Q4", "revenue": 58000, "expenses": 38000, "profit": 20000}
  ],
  "title": "Quarterly Financial Performance",
  "description": "Comparative analysis of revenue, expenses, and profit by quarter"
}
```

**cURL Command:**
```cmd
curl -X POST http://localhost:8080/api/charts/generate ^
  -H "Content-Type: application/json" ^
  -d "{\"data\":[{\"quarter\":\"Q1\",\"revenue\":45000,\"expenses\":32000,\"profit\":13000},{\"quarter\":\"Q2\",\"revenue\":52000,\"expenses\":35000,\"profit\":17000},{\"quarter\":\"Q3\",\"revenue\":48000,\"expenses\":33000,\"profit\":15000},{\"quarter\":\"Q4\",\"revenue\":58000,\"expenses\":38000,\"profit\":20000}],\"title\":\"Quarterly Financial Performance\",\"description\":\"Comparative analysis of revenue, expenses, and profit by quarter\"}" ^
  --output financial-performance.png
```

---

## 5. Product Performance Data

**Endpoint:** `POST /api/charts/generate`

```json
{
  "data": {
    "Product A": 145,
    "Product B": 89,
    "Product C": 234,
    "Product D": 67,
    "Product E": 198,
    "Product F": 123
  },
  "title": "Product Sales Volume",
  "description": "Units sold per product in the last quarter"
}
```

---

## 6. Website Analytics Data

**Endpoint:** `POST /api/charts/generate`

```json
{
  "data": {
    "Monday": 2400,
    "Tuesday": 2100,
    "Wednesday": 2800,
    "Thursday": 2600,
    "Friday": 3200,
    "Saturday": 1800,
    "Sunday": 1500
  },
  "title": "Weekly Website Traffic",
  "description": "Daily unique visitors for the past week"
}
```

---

## 7. Temperature Data (Scatter Plot Suitable)

**Endpoint:** `POST /api/charts/generate`

```json
{
  "data": [
    {"day": 1, "temperature": 22.5, "humidity": 45},
    {"day": 2, "temperature": 25.1, "humidity": 52},
    {"day": 3, "temperature": 23.8, "humidity": 48},
    {"day": 4, "temperature": 26.2, "humidity": 55},
    {"day": 5, "temperature": 24.9, "humidity": 50},
    {"day": 6, "temperature": 27.3, "humidity": 58},
    {"day": 7, "temperature": 25.6, "humidity": 53}
  ],
  "title": "Weather Patterns",
  "description": "Daily temperature and humidity correlation"
}
```

---

## 8. Budget Allocation

**Endpoint:** `POST /api/charts/generate`

```json
{
  "data": {
    "Marketing": 35000,
    "Development": 85000,
    "Operations": 45000,
    "HR": 25000,
    "Admin": 15000,
    "Research": 30000
  },
  "title": "Annual Budget Allocation",
  "description": "Department-wise budget distribution for 2024"
}
```

---

## 9. Customer Satisfaction Scores

**Endpoint:** `POST /api/charts/generate`

```json
{
  "data": {
    "Excellent": 142,
    "Good": 89,
    "Average": 34,
    "Poor": 12,
    "Very Poor": 3
  },
  "title": "Customer Satisfaction Survey Results",
  "description": "Distribution of customer satisfaction ratings from recent survey"
}
```

---

## 10. Stock Performance

**Endpoint:** `POST /api/charts/generate`

```json
{
  "data": {
    "Week 1": 125.50,
    "Week 2": 128.75,
    "Week 3": 132.20,
    "Week 4": 129.80,
    "Week 5": 135.40,
    "Week 6": 142.15,
    "Week 7": 138.90,
    "Week 8": 145.25
  },
  "title": "Stock Price Movement",
  "description": "Weekly closing prices showing upward trend with some volatility"
}
```

---

## Analysis Only Requests (No Chart Generation)

**Endpoint:** `POST /api/charts/analyze`

### Sample Analysis Request:
```json
{
  "data": {
    "Chrome": 64.2,
    "Safari": 19.4,
    "Edge": 4.8,
    "Firefox": 3.2,
    "Opera": 2.4,
    "Others": 6.0
  },
  "title": "Browser Usage Statistics",
  "description": "Market share of different web browsers"
}
```

**cURL Command for Analysis:**
```cmd
curl -X POST http://localhost:8080/api/charts/analyze ^
  -H "Content-Type: application/json" ^
  -d "{\"data\":{\"Chrome\":64.2,\"Safari\":19.4,\"Edge\":4.8,\"Firefox\":3.2,\"Opera\":2.4,\"Others\":6.0},\"title\":\"Browser Usage Statistics\",\"description\":\"Market share of different web browsers\"}"
```

**Expected Analysis Response:**
```json
{
  "chartType": "PIE",
  "title": "Browser Usage Distribution",
  "xAxisLabel": "Browsers",
  "yAxisLabel": "Market Share (%)",
  "categories": ["Chrome", "Safari", "Edge", "Firefox", "Opera", "Others"],
  "series": {
    "Market Share": [64.2, 19.4, 4.8, 3.2, 2.4, 6.0]
  },
  "reasoning": "A pie chart is ideal for displaying market share percentages as it clearly shows the proportion of each browser relative to the total."
}
```

---

## Testing with Different Data Structures

### Array of Numbers:
```json
{
  "data": [10, 25, 30, 45, 20, 35, 40],
  "title": "Sample Numeric Array",
  "description": "Testing with simple array of numbers"
}
```

### Nested Object Structure:
```json
{
  "data": {
    "regions": {
      "North": {"sales": 15000, "target": 18000},
      "South": {"sales": 22000, "target": 20000},
      "East": {"sales": 18000, "target": 19000},
      "West": {"sales": 25000, "target": 23000}
    }
  },
  "title": "Regional Sales vs Targets",
  "description": "Performance comparison across different regions"
}
```

---

## Health Check

**Endpoint:** `GET /api/charts/health`

**cURL Command:**
```cmd
curl http://localhost:8080/api/charts/health
```

**Expected Response:**
```
Chart Generation Service is running
```

---

## Important Notes:

1. **Set OpenAI API Key**: Before testing, make sure to set your OpenAI API key:
   ```cmd
   set OPENAI_API_KEY=your-actual-api-key-here
   ```

2. **Start the Application**: 
   ```cmd
   mvn spring-boot:run
   ```

3. **View Generated Charts**: The PNG files will be saved to your current directory

4. **API Documentation**: Visit `http://localhost:8080/swagger-ui.html` for interactive testing

5. **Response Format**: Chart generation returns PNG binary data, while analysis returns JSON

6. **Error Handling**: If LLM service is unavailable, the system provides fallback chart generation
