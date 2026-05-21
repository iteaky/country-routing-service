# Country Routing Service

A simple Spring Boot REST service that calculates a possible land route from one country to another by using country border data from:

```text
https://raw.githubusercontent.com/mledoze/countries/master/countries.json
```

Countries are identified by the `cca3` field.

## Tech stack

- Java 17
- Spring Boot
- Maven
- REST API
- Swagger UI / OpenAPI documentation
- BFS graph search

## How it works

The application loads country data on startup and builds an in-memory graph:

- country `cca3` code = graph node
- country `borders` values = graph edges

The route is calculated with BFS, which is efficient for an unweighted graph and returns the shortest route by number of border crossings.

Complexity: `O(V + E)`, where:

- `V` = number of countries
- `E` = number of borders

## Build

```bash
mvn clean package
```

## Run

```bash
mvn spring-boot:run
```

or after building:

```bash
java -jar target/country-routing-service-0.0.1-SNAPSHOT.jar
```

## Swagger UI / OpenAPI

After starting the application, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```

The raw OpenAPI JSON specification is available at:

```text
http://localhost:8080/v3/api-docs
```

## API

### Get land route

```http
GET /routing/{origin}/{destination}
```

Example:

```bash
curl http://localhost:8080/routing/CZE/ITA
```

Response:

```json
{
  "route": ["CZE", "AUT", "ITA"]
}
```

## Error cases

If there is no possible land route, or one of the country codes is unknown, the service returns:

```http
400 Bad Request
```

Example:

```bash
curl -i http://localhost:8080/routing/CZE/USA
```

## Run tests

```bash
mvn test
```

## Notes

- Input country codes are normalized to uppercase.
- If origin and destination are the same, the service returns a single-country route.
- Countries without borders are supported.
