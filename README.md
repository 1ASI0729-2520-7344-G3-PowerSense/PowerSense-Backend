# PowerSense Backend

PowerSense is a backend application built with **Java (Spring Boot)** following **Domain-Driven Design (DDD)** principles. It uses **MySQL** as the database (hosted on Railway) and is deployed on **Render**. The frontend is hosted on **GitHub Pages**.

---

## Architecture

The project is organized using a classic DDD structure:

- **Domain**: Business logic and entities.
- **Application**: Command and query services.
- **Infrastructure**: Persistence, adapters, configuration.
- **Interfaces/REST**: Controllers and resources.

### Main Modules
- **analytics**: Alerts, dashboard, reports
- **auth**: Authentication and user management
- **inventory**: Devices, rooms, scheduling
- **shared**: Common domain and infrastructure components

---

## Technologies
- Java 17
- Spring Boot
- Maven
- MySQL (Railway)
- Docker
- Render (backend deployment)
- GitHub Pages (frontend)

---

## Setup

### Clone the repository
```bash
git clone https://github.com/1ASI0729-2520-7344-G3-PowerSense/PowerSense-Backend.git
cd PowerSense-Backend
```

### Configure database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://<host>:<port>/<database>
spring.datasource.username=<username>
spring.datasource.password=<password>
spring.jpa.hibernate.ddl-auto=update
```
Credentials are provided by Railway.

---

## Deployment

- **Backend**: Deploy on Render using the included `Dockerfile`.
- **Database**: MySQL instance on Railway.
- **Frontend**: GitHub Pages.

---

## Build and Run

Run locally:
```bash
./mvnw clean install
./mvnw spring-boot:run
```

Build Docker image:
```bash
docker build -t powersense-backend .
docker run -p 8080:8080 powersense-backend
```

---

## API Endpoints

### Authentication
Base path: `/api/v1/auth`
- POST `/register`
- POST `/login`
- GET `/me`
- PUT `/profile`
- POST `/logout`

### Alerts
Base path: `/api/v1/analytics/alerts`
- GET `/recent`
- POST `/`
- GET `/`
- GET `/{id}`
- PATCH `/{id}/acknowledge`

### Dashboard
Base path: `/api/v1/analytics/dashboard`
- GET `/kpis`
- GET `/alerts`
- GET `/tips`

### Reports
Base path: `/api/v1/analytics/reports`
- GET `/kpis`
- GET `/monthly-comparison`
- GET `/departments`
- GET `/history`
- GET `/realtime-consumption`

### Devices
Base path: `/api/v1/inventory/devices`
- GET `/`
- GET `/{id}`
- POST `/`
- PATCH `/{id}`
- DELETE `/{id}`
- PATCH `/{id}/status`
- PATCH `/status/all`
- POST `/import`
- GET `/export`

### Rooms
Base path: `/api/v1/inventory/rooms`
- GET `/`
- GET `/{id}`
- GET `/{id}/devices`
- POST `/`
- DELETE `/{id}`
- PATCH `/{id}`
- PATCH `/{roomId}/devices/status`

### Schedules
Base path: `/api/v1/inventory/schedules`
- POST `/`
- GET `/`
- GET `/{id}`
- PATCH `/{id}`
- DELETE `/{id}`
- PATCH `/{id}/toggle`

---

## Project Status
Backend implemented  
Database configured  
Frontend deployed  
