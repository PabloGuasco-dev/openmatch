# Microservicio CRUD Entidades Bancarias

Microservicio REST que expone un CRUD completo sobre entidades bancarias (Banco), con base de datos H2 en memoria.

## Requisitos

- Java 17+
- Maven (o usar `./mvnw`)

## Ejecución

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080` (puerto por defecto).

## API REST

Base path: **`/api/bancos`**

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/bancos` | Listar todos los bancos |
| GET | `/api/bancos/{id}` | Obtener banco por id |
| POST | `/api/bancos` | Crear banco (alta) |
| PUT | `/api/bancos/{id}` | Actualizar banco (modificación) |
| DELETE | `/api/bancos/{id}` | Eliminar banco (baja) |
| GET | `/api/bancos/consulta-interna` | **Consulta interna**: llama por HTTP al endpoint GET `/api/bancos` del mismo microservicio y devuelve el listado |

### Ejemplo de cuerpo para POST/PUT

```json
{
  "codigo": "B001",
  "nombre": "Banco Ejemplo",
  "pais": "España",
  "activo": true
}
```

- **codigo**: obligatorio, único (evita duplicados).
- **nombre**: obligatorio.
- **pais**: opcional.
- **activo**: opcional, por defecto `true`.

## Diseño y patrones

- **Arquitectura en capas**: Controller → Service → Repository (JPA).
- **Patrones**: Repository (Spring Data JPA), Service (lógica de negocio), DTOs para entrada/salida, excepciones de dominio (`BancoNotFoundException`, `DuplicateBancoException`).
- **Manejo de duplicados**: en POST y PUT se valida que no exista otro banco con el mismo `codigo`; si existe, se responde **409 Conflict**.
- **Manejo de excepciones**: `@RestControllerAdvice` (`GlobalExceptionHandler`) centraliza respuestas para:
  - 404 si el banco no existe.
  - 409 si el código está duplicado.
  - 400 si falla la validación (Bean Validation).
  - 500 para errores no controlados.

## Tests

```bash
./mvnw test
```

- **BancoServiceTest**: tests unitarios del servicio (crear, duplicado, findById, update, delete).
- **BancoControllerIntegrationTest**: tests de integración del CRUD (crear, listar, obtener, eliminar, validación, duplicado 409).
- **ConsultaInternaIntegrationTest**: comprueba que `/api/bancos/consulta-interna` realiza la llamada HTTP al propio microservicio y devuelve el listado.

## H2 Console

En desarrollo, la consola H2 está en: `http://localhost:8080/h2-console`  
JDBC URL: `jdbc:h2:mem:bancosdb`, usuario: `sa`, contraseña vacía.
