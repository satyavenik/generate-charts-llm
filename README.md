# Chart Generation with LLM Integration

A Spring Boot REST API that accepts JSON data, uses an LLM (Language Learning Model) to analyze the data and recommend the best chart type, then generates and returns the chart as a PNG image.

## Features

- **JSON Data Input**: Accepts any JSON data structure
- **LLM-Powered Analysis**: Uses OpenAI GPT to analyze data and recommend appropriate chart types
- **Multiple Chart Types**: Supports BAR, LINE, PIE, and SCATTER charts
- **Automatic Chart Generation**: Uses JFreeChart to generate high-quality chart images
- **REST API**: Easy-to-use REST endpoints with OpenAPI/Swagger documentation

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- OpenAI API Key (or compatible LLM API)

## Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd generate-charts-llm
   ```

2. **Configure API Key**
   
   Set your OpenAI API key as an environment variable:
   ```bash
   # Windows
   set OPENAI_API_KEY=your-api-key-here
   
   # Linux/Mac
   export OPENAI_API_KEY=your-api-key-here
   ```
   
   Or edit `src/main/resources/application.properties`:
   ```properties
   openai.api.key=your-api-key-here
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### 1. Generate Chart (POST /api/charts/generate)

Accepts JSON data, analyzes it using LLM, and returns a PNG chart image.

**Request:**
```bash
curl -X POST http://localhost:8080/api/charts/generate \
  -H "Content-Type: application/json" \
  -d '{
    "data": {
      "January": 100,
      "February": 150,
      "March": 200,
      "April": 175
    },
    "title": "Monthly Sales",
    "description": "Sales data for Q1"
  }' \
  --output chart.png
```

**Request Body:**
```json
{
  "data": {
    "category1": value1,
    "category2": value2,
    ...
  },
  "title": "Optional chart title",
  "description": "Optional description to help LLM understand context"
}
```

**Response:** PNG image (binary data)

### 2. Analyze Data (POST /api/charts/analyze)

Analyzes JSON data and returns the LLM's recommendation without generating the chart.

**Request:**
```bash
curl -X POST http://localhost:8080/api/charts/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "data": {
      "Product A": 45,
      "Product B": 30,
      "Product C": 25
    },
    "title": "Market Share"
  }'
```

**Response:**
```json
{
  "chartType": "PIE",
  "title": "Market Share Distribution",
  "xAxisLabel": "Products",
  "yAxisLabel": "Percentage",
  "categories": ["Product A", "Product B", "Product C"],
  "series": {
    "Market Share": [45, 30, 25]
  },
  "reasoning": "A pie chart is ideal for showing proportions and market share distribution among products."
}
```

### 3. Health Check (GET /api/charts/health)

Check if the service is running.

**Request:**
```bash
curl http://localhost:8080/api/charts/health
```

**Response:**
```
Chart Generation Service is running
```

## API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Example Use Cases

### 1. Sales Data Visualization
```json
{
  "data": {
    "Q1": 25000,
    "Q2": 32000,
    "Q3": 28000,
    "Q4": 35000
  },
  "title": "Quarterly Revenue",
  "description": "Company revenue by quarter"
}
```

### 2. Multi-Series Data
```json
{
  "data": [
    {"month": "Jan", "sales": 100, "expenses": 80},
    {"month": "Feb", "sales": 150, "expenses": 90},
    {"month": "Mar", "sales": 200, "expenses": 110}
  ],
  "title": "Financial Overview",
  "description": "Compare sales vs expenses"
}
```

### 3. Distribution Data
```json
{
  "data": {
    "Chrome": 65,
    "Firefox": 15,
    "Safari": 12,
    "Edge": 8
  },
  "title": "Browser Market Share",
  "description": "Percentage distribution"
}
```

## How It Works

1. **Data Submission**: Client sends JSON data via REST API
2. **LLM Analysis**: The service calls OpenAI GPT to analyze the data structure and recommend:
   - Best chart type (BAR, LINE, PIE, SCATTER)
   - Appropriate labels and titles
   - Data organization strategy
3. **Chart Generation**: JFreeChart generates a PNG image based on LLM recommendations
4. **Response**: PNG chart image is returned to the client

## Configuration

Edit `src/main/resources/application.properties` to customize:

```properties
# Server port
server.port=8080

# OpenAI Configuration
openai.api.key=${OPENAI_API_KEY}
openai.api.url=https://api.openai.com/v1/chat/completions
openai.model=gpt-4

# Logging
logging.level.com.example.generatechartsllm=DEBUG
```

## Supported Chart Types

- **BAR**: Best for comparing values across categories
- **LINE**: Ideal for showing trends over time
- **PIE**: Perfect for showing proportions and percentages
- **SCATTER**: Great for showing correlations between variables

## Error Handling

The service includes fallback mechanisms:
- If LLM service is unavailable, a default chart analysis is used
- Invalid data structures are handled gracefully
- Comprehensive error logging for debugging

## Technologies Used

- **Spring Boot 3.2.0**: Application framework
- **Spring WebFlux**: Reactive HTTP client for LLM API calls
- **JFreeChart**: Chart generation library
- **OpenAI GPT-4**: Data analysis and chart recommendation
- **Springdoc OpenAPI**: API documentation
- **Lombok**: Reduce boilerplate code

## License

MIT License

## Support

For issues and questions, please open an issue on the repository.

