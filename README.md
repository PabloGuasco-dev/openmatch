# OpenMatch - Full Stack Banking System

A comprehensive full-stack application featuring a banking CRUD microservice with modern web technologies.

## Project Structure

```
openmatch/
├── backend/          # Spring Boot REST API
├── frontend/         # React web application  
├── api-gateway/      # Node.js API gateway
└── README.md         # This file
```

## Features

### Backend (Spring Boot)
- **REST API** for banking entities management
- **Complete CRUD operations** with validation
- **H2 in-memory database** with console access
- **Internal query endpoint** demonstrating self HTTP calls
- **Comprehensive testing** (unit + integration)
- **Global exception handling** with proper HTTP status codes
- **Security configuration** for API and H2 console

### Frontend (React)
- Modern React application with Vite
- Responsive design with TailwindCSS
- Real-time banking data management

### API Gateway (Node.js)
- Centralized API routing
- Request/response handling

## Requirements

- **Java 17+**
- **Node.js 16+**
- **Maven** (or use included `./mvnw`)

## Quick Start

### Backend
```bash
cd backend
./mvnw spring-boot:run
```
The API will be available at `http://localhost:8080`

### Frontend
```bash
cd frontend
npm install
npm run dev
```
The web app will be available at `http://localhost:5173`

### API Gateway
```bash
cd api-gateway
npm install
npm start
```
The gateway will be available at `http://localhost:3000`

## API Endpoints

### Banking API (`/api/banks`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/banks` | List all banks |
| `GET` | `/api/banks/{id}` | Get bank by ID |
| `POST` | `/api/banks` | Create new bank |
| `PUT` | `/api/banks/{id}` | Update existing bank |
| `DELETE` | `/api/banks/{id}` | Delete bank |
| `GET` | `/api/banks/internal-query` | Internal HTTP call demo |

### Request/Response Format

**Create/Update Bank:**
```json
{
  "code": "B001",
  "name": "Example Bank",
  "country": "United States",
  "active": true
}
```

**Bank Response:**
```json
{
  "id": 1,
  "code": "B001",
  "name": "Example Bank",
  "country": "United States",
  "active": true,
  "creationDate": "2024-01-01T00:00:00Z"
}
```

## Testing

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## Database

### H2 Console
Access the H2 database console at: `http://localhost:8080/h2-console`

**Connection Details:**
- **JDBC URL:** `jdbc:h2:mem:bancosdb`
- **Username:** `sa`
- **Password:** (empty)

## Technology Stack

### Backend
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Security**
- **H2 Database**
- **Bean Validation**
- **JUnit 5**
- **MockMvc**

### Frontend
- **React 18**
- **Vite**
- **TailwindCSS**
- **Lucide Icons**

### API Gateway
- **Node.js**
- **Express.js**

## Security

- API endpoints configured for public access (demo purposes)
- H2 console access enabled for development
- Token-based authentication ready for production

## Error Handling

The API provides consistent error responses:

- **400 Bad Request** - Validation errors
- **404 Not Found** - Resource not found
- **409 Conflict** - Duplicate bank code
- **500 Internal Server Error** - Unexpected errors

## Development Workflow

1. **Backend Development**: Start with the Spring Boot API
2. **Frontend Integration**: Connect React app to REST endpoints
3. **Gateway Configuration**: Set up routing through API gateway
4. **Testing**: Ensure comprehensive test coverage
5. **Documentation**: Keep API docs updated

## Architecture Patterns

- **Layered Architecture**: Controller → Service → Repository
- **Repository Pattern**: Spring Data JPA
- **DTO Pattern**: Request/Response objects
- **Service Layer**: Business logic separation
- **Exception Handling**: Global error management
- **Dependency Injection**: Spring IoC container
