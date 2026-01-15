# ACA-Py Integration Service

A Spring Boot 3 (Java 17) integration/facade service that provides clean REST endpoints for interacting with ACA-Py (Aries Cloud Agent - Python) Admin API.

## Features

- **Clean REST API**: Well-structured endpoints under `/v1` prefix
- **Full Swagger/OpenAPI Documentation**: Interactive API documentation at `/swagger-ui.html`
- **Correlation ID Support**: Automatic correlation ID tracking across requests
- **Request/Response Logging**: Comprehensive logging with correlation IDs in MDC
- **Error Handling**: Standardized error responses with `ApiError` model
- **Orchestration Flows**: Higher-level endpoints that combine multiple ACA-Py calls

## Tech Stack

- Java 17
- Spring Boot 3.2.1
- Spring WebClient for HTTP calls
- springdoc-openapi for Swagger UI
- Lombok for cleaner code
- JUnit 5 for testing

## Configuration

Configure the ACA-Py connection in `src/main/resources/application.yml`:

```yaml
acapy:
  base-url: http://localhost:8021  # ACA-Py Admin API URL
  connect-timeout: 5000
  read-timeout: 30000
```

## Running the Application

### Build
```bash
mvn clean package
```

### Run
```bash
mvn spring-boot:run
```

Or run the JAR:
```bash
java -jar target/indy-integration-service-1.0.0-SNAPSHOT.jar
```

The service will start on port 8080 by default.

## API Documentation

Once running, access the Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON specification:
```
http://localhost:8080/api-docs
```

## API Endpoints

### Status
- `GET /v1/status` - Get ACA-Py agent status

### Out-of-Band (OOB)
- `POST /v1/oob/invitations` - Create OOB invitation
- `POST /v1/oob/invitations/receive` - Receive OOB invitation
- `DELETE /v1/oob/invitations/{invi_msg_id}` - Delete OOB invitation

### Connections
- `GET /v1/connections` - List all connections
- `GET /v1/connections/{conn_id}` - Get specific connection
- `POST /v1/connections/{conn_id}/message` - Send basic message

### Credentials (Issue Credential 2.0)
- `POST /v1/credentials/v2/offers` - Create credential offer
- `POST /v1/credentials/v2/send` - Send credential
- `GET /v1/credentials/v2/records` - List credential exchange records
- `GET /v1/credentials/v2/records/{cred_ex_id}` - Get credential exchange record

### Proofs (Present Proof 2.0)
- `POST /v1/proofs/v2/requests` - Create proof request
- `POST /v1/proofs/v2/send-request` - Send proof request
- `GET /v1/proofs/v2/records` - List presentation exchange records
- `GET /v1/proofs/v2/records/{pres_ex_id}` - Get presentation exchange record
- `POST /v1/proofs/v2/records/{pres_ex_id}/verify` - Verify presentation

### Orchestration Flows
- `POST /v1/flows/oob-proof-invitation` - Create OOB invitation with embedded proof request
- `POST /v1/flows/oob-credential-offer-invitation` - Create OOB invitation with embedded credential offer

## Correlation ID

The service automatically handles correlation IDs for request tracing:

- Reads `X-Correlation-Id` header from incoming requests
- Generates a UUID if not provided
- Includes correlation ID in all logs via MDC
- Forwards `X-Correlation-Id` on outbound ACA-Py calls
- Returns `X-Correlation-Id` in response headers

## Logging

Logs include correlation IDs for easy tracing:

```
2026-01-15 21:00:00 [http-nio-8080-exec-1] INFO  c.t.i.filter.RequestResponseLoggingFilter [abc-123-def] - Inbound request: method=GET, path=/v1/status, status=200, duration=45ms
2026-01-15 21:00:00 [http-nio-8080-exec-1] INFO  c.t.i.config.WebClientConfig [abc-123-def] - Outbound ACA-Py request: method=GET, url=http://localhost:8021/status
```

## Error Handling

All errors return a standardized `ApiError` response:

```json
{
  "timestamp": "2026-01-15T21:00:00.000",
  "status": 500,
  "error": "Internal Server Error",
  "message": "ACA-Py API error: Connection refused",
  "path": "/v1/status",
  "correlationId": "abc-123-def"
}
```

## Testing

Run unit tests:
```bash
mvn test
```

Run with coverage:
```bash
mvn verify
```

## ACA-Py Field Naming

The service preserves ACA-Py's field naming conventions:
- `conn_id` (connection ID)
- `pres_ex_id` (presentation exchange ID)
- `cred_ex_id` (credential exchange ID)
- `oob_id` (out-of-band record ID)
- `invi_msg_id` (invitation message ID)

## Development

### Project Structure
```
src/
├── main/
│   ├── java/com/tahaky/indyintegration/
│   │   ├── config/          # WebClient and app configuration
│   │   ├── controller/      # REST controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── exception/       # Global exception handler
│   │   ├── filter/          # Correlation ID and logging filters
│   │   ├── model/           # Domain models
│   │   └── service/         # ACA-Py client service
│   └── resources/
│       └── application.yml  # Configuration
└── test/
    └── java/com/tahaky/indyintegration/
        └── controller/      # Controller tests
```
