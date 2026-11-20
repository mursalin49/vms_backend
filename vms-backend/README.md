# VMS Backend — Spring Boot 3 · Java 17 · MySQL 8 · Docker

## Run with Docker (backend + MySQL)
```
cp .env.example .env
docker compose up --build
```
- API:      http://localhost:8080
- Swagger:  http://localhost:8080/swagger-ui.html  (Authorize with the login token)
- Health:   http://localhost:8080/actuator/health
- MySQL:    localhost:3307  (user/password from .env)

Stop: `docker compose down`   |   Stop + delete DB data: `docker compose down -v`

## Run backend locally, MySQL in Docker
```
docker compose up -d mysql
mvn spring-boot:run        # defaults connect to localhost:3307 (user vms / vms_password)
```

## Authentication (no public sign-up)
First start creates the authorized user: admin@starkgroup.com / StrongPassword123 (override ADMIN_EMAIL / ADMIN_PASSWORD). The password is stored as a BCrypt hash; public sign-up is disabled.
Set SEED_SAMPLE_DATA=false to skip the 5 sample vehicles.

## API
POST   /api/auth/login      {email,password} -> {token,email} | 400 | 401
GET    /api/vehicles
GET    /api/vehicles/{id}
POST   /api/vehicles        -> 201 | 400 (fieldErrors) | 409 duplicate vehicleNumber
PUT    /api/vehicles/{id}   -> 200 | 404 | 409
DELETE /api/vehicles/{id}   -> 204 | 404
All except login need  Authorization: Bearer <token>.

## Architecture
controller (HTTP) -> service (business rules, transactions) -> repository (Spring Data JPA) -> MySQL
- package-by-layer: controller/, service/, repository/, entity/, payload/, security/, config/, and exception/
- DTO records (VehicleRequest/Response); entities are never returned from controllers
- Schema owned by Flyway (db/migration); Hibernate only validates (ddl-auto=validate)
- Duplicate Vehicle Number: service check + UNIQUE constraint (409 on both paths)
- Stateless JWT, BCrypt passwords, CORS from env, 12-factor config via env vars
- Docker: multi-stage build, non-root user, MySQL healthcheck gates backend start

## Tests
`mvn test`  (VehicleServiceTest: duplicate rule, normalization, not-found)

## Troubleshooting
- "Schema-validation: wrong column type": set spring.jpa.hibernate.ddl-auto=none (Flyway still creates the schema).
- Port 3306/3307 busy: change MYSQL_PORT in .env.
- CORS error from Flutter Web: add your exact origin to CORS_ORIGINS.
