# Banking Entities CRUD Microservice

REST microservice that exposes a complete CRUD on banking entities (Bank), with in-memory H2 database.

## Requirements

- Java 17+
- Maven (or use `./mvnw`)

## Execution

```bash
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080` (default port).

## REST API

Base path: **`/api/banks`**

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/banks` | List all banks |
| GET | `/api/banks/{id}` | Get bank by id |
| POST | `/api/banks` | Create bank |
| PUT | `/api/banks/{id}` | Update bank |
| DELETE | `/api/banks/{id}` | Delete bank |
| GET | `/api/banks/internal-query` | **Internal query**: makes HTTP call to GET `/api/banks` endpoint of the same microservice and returns the list |

### POST/PUT Body Example

```json
{
  "code": "B001",
  "name": "Example Bank",
  "country": "Spain",
  "active": true
}
```

- **code**: required, unique (prevents duplicates).
- **name**: required.
- **country**: optional.
- **active**: optional, defaults to `true`.

## Design and Patterns

- **Layered architecture**: Controller → Service → Repository (JPA).
- **Patterns**: Repository (Spring Data JPA), Service (business logic), DTOs for input/output, domain exceptions (`BancoNotFoundException`, `DuplicateBancoException`).
- **Duplicate handling**: in POST and PUT validates that no other bank exists with the same `code`; if it exists, responds **409 Conflict**.
- **Exception handling**: `@RestControllerAdvice` (`GlobalExceptionHandler`) centralizes responses for:
  - 404 if bank doesn't exist.
  - 409 if code is duplicated.
  - 400 if validation fails (Bean Validation).
  - 500 for uncontrolled errors.

## Tests

```bash
./mvnw test
```

- **BancoServiceTest**: unit tests of the service (create, duplicate, findById, update, delete).
- **BancoControllerIntegrationTest**: CRUD integration tests (create, list, get, delete, validation, duplicate 409).
- **ConsultaInternaIntegrationTest**: verifies that `/api/banks/internal-query` makes the HTTP call to the same microservice and returns the list.

## H2 Console

In development, the H2 console is at: `http://localhost:8080/h2-console`  
JDBC URL: `jdbc:h2:mem:bancosdb`, user: `sa`, password empty.
