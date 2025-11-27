# Generate Charts LLM

Spring Boot REST API with LLM Integration that generates charts for given JSON data.

## Features

- REST API endpoint that accepts JSON data and returns chart images
- LLM-powered analysis to recommend optimal chart types and configurations
- Swagger/OpenAPI documentation
- Support for multiple chart types: bar, pie, and line charts
- JFreeChart for chart rendering

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- OpenAI API key

## Configuration

Set your OpenAI API key as an environment variable:

```bash
export OPENAI_API_KEY=your-api-key-here
```

Or configure it in `application.properties`:

```properties
openai.api.key=your-api-key-here
```

## Building and Running

```bash
# Build the project
mvn clean package

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Generate Chart

**POST** `/api/charts/generate`

Accepts JSON data and returns a PNG chart image.

**Request Body:**

```json
{
  "chartType": "bar",
  "title": "Sales by Month",
  "data": {
    "January": 100,
    "February": 150,
    "March": 200
  }
}
```

Or with labels and values:

```json
{
  "chartType": "pie",
  "title": "Market Share",
  "labels": ["Product A", "Product B", "Product C"],
  "values": [40, 35, 25]
}
```

**Response:** PNG image

### Analyze Data

**POST** `/api/charts/analyze`

Analyzes the data using LLM and returns chart recommendations without generating an image.

**Request Body:** Same as above

**Response:**

```json
{
  "recommendedChartType": "bar",
  "suggestedTitle": "Monthly Sales Overview",
  "xAxisLabel": "Month",
  "yAxisLabel": "Sales Amount",
  "insights": "The data shows an increasing trend in sales over the months."
}
```

## Swagger UI

Access the Swagger UI at: `http://localhost:8080/swagger-ui.html`

API documentation is available at: `http://localhost:8080/api-docs`

## Technology Stack

- Spring Boot 3.2.0
- Spring WebFlux (for WebClient)
- JFreeChart 1.5.4
- SpringDoc OpenAPI 2.3.0
- OpenAI GPT-4o-mini

## Project Structure

```
src/main/java/com/example/generatechartsllm/
├── GenerateChartsLlmApplication.java    # Main application class
├── config/
│   └── OpenApiConfig.java               # Swagger/OpenAPI configuration
├── controller/
│   └── ChartController.java             # REST controller
├── model/
│   ├── ChartRequest.java                # Request model
│   └── ChartAnalysis.java               # Analysis response model
└── service/
    ├── ChartService.java                # Chart generation service
    └── ChatClientService.java           # LLM client service
```

