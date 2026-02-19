# Spring Boot Casino

[![Codecov](https://codecov.io/gh/jordimarsal/spring-boot-casino/branch/main/graph/badge.svg)](https://codecov.io/gh/jordimarsal/spring-boot-casino)

A Spring Boot application demonstrating casino game betting logic with PostgreSQL persistence and Domain-Driven Design patterns.

## Features

### Core Functionality
- Player authentication and session management
- Real-time betting with multiple game types (Video Bingo, Blackjack, Poker, Roulette, Slots)
- Transactional bet processing with audit trail
- RESTful API with comprehensive validation

### Architecture Highlights
- **PostgreSQL + JPA/Hibernate** - Full database persistence with transactional guarantees
- **Progressive DDD** - Rich domain models with behavior (Player.canPlaceBet, Bet.calculateWin)
- **Spring Data JPA** - Repository pattern with custom queries
- **Global Exception Handler** - Domain-specific exceptions with proper HTTP status codes
- **Comprehensive Testing** - 49 tests (unit, integration, E2E) all passing

## Quick Start

### Prerequisites
- Java 21
- Spring Boot 3.3.2
- Maven 3.6+
- PostgreSQL 13+
- JobRunr 7.5.3
- JaCoCo 0.8.10
- Spotless 3.2.1

### Database Setup

Install PostgreSQL 13+ and create database:

```bash
# Create database and user
sudo -u postgres createuser casino_user -P
sudo -u postgres createdb -O casino_user casino

# Grant privileges
psql -U casino_user -d casino -c "GRANT ALL PRIVILEGES ON DATABASE casino TO casino_user;"
```

Update password in `src/main/resources/application-postgres.properties`:
```properties
spring.datasource.password=your_secure_password
```

### Run Application

```bash
# With PostgreSQL (production profile)
mvn spring-boot:run

# Run tests
mvn test
```

The application will start on `http://localhost:9095`

### Code Quality Tools

#### Spotless (Code Formatting)

Format code automatically:
```bash
mvn spotless:apply
```

Check code format:
```bash
mvn spotless:check
```

#### JaCoCo (Code Coverage)

Generate coverage report:
```bash
mvn test
mvn jacoco:report
```

View report: `target/site/jacoco/index.html`

Coverage threshold: **85%** enforced by build.

#### Codecov (Coverage Reporting)

Upload coverage to Codecov (requires GitHub Actions):
```bash
# After running tests
bash <(curl -s https://codecov.io/bash)
```

## API Endpoints

### Authentication

#### Login Player
```bash
curl -X POST http://localhost:9095/api/casino/logon \
  -H "Content-Type: application/json" \
  -d '{
    "loginDate":"2026-02-18T10:00:00",
    "maxTime":300000,
    "uuid":"test-123",
    "userProvider":"POKERSTAR"
  }'
```

#### Logout Player
```bash
curl -X POST http://localhost:9095/api/casino/logout/test-123
```

### Betting

#### Place Bet
```bash
curl -X POST http://localhost:9095/api/casino/bet/player-123 \
  -H "Content-Type: application/json" \
  -d '{
    "betAmount": 10,
    "playerUUID": "player-123",
    "gameUUID": "VIDEOBINGO-UUID"
  }'
```

**Validations:**
- Path UUID must match bet.playerUUID
- Bet amount must be positive (> 0)
- Maximum bet: 10,000

### Player Info

#### Get Player
```bash
curl http://localhost:9095/api/casino/get/player-123
```

#### Get Player Info (String)
```bash
curl http://localhost:9095/api/casino/gets/player-123
```

## Architecture

### Layered Architecture

```
┌─────────────┐
│ Controllers │ REST endpoints
└──────┬──────┘
       │
┌──────▼──────┐
│  Services   │ @Transactional business logic
└──────┬──────┘
       │
┌──────▼──────┐
│ Repositories│ Spring Data JPA
└──────┬──────┘
       │
┌──────▼──────┐
│  Entities   │ Rich domain models
└──────┬──────┘
       │
┌──────▼──────┐
│ PostgreSQL  │ Database
└─────────────┘
```

### Components

- **Controllers:** REST endpoints (`RestPlayGameController`, `CasinoRestController`)
- **Services:** Business logic with @Transactional (`PlayerServiceImpl`, `GamePlayServiceImpl`)
- **Repositories:** Data access (`PlayerRepository`, `BetRepository`)
- **Entities:** Rich domain models (`Player`, `Bet`) with domain behavior
- **Exceptions:** Domain-specific exceptions (`PlayerNotFoundException`, `InsufficientBalanceException`)
- **Handlers:** Game betting logic (`Jugada`, `GameHandler`)

### Games Supported

- Video Bingo
- Blackjack
- Poker
- Roulette
- Slot Machine

### Thread-Safety & Transactions

- **@Transactional** - ACID guarantees on service layer
- **JPA Locking** - Optimistic locking with versioning
- **Database Constraints** - Foreign keys, not null constraints
- **Exception Rollback** - Automatic rollback on errors

## Project Structure

```
src/main/java/net/jordimp/casino/
├── CasinoApplication.java         # Spring Boot main class
├── config/                        # Configuration classes
│   └── JpaConfig.java             # JPA auditing configuration
├── controllers/                   # REST endpoints
├── entity/                        # JPA entities with domain behavior
│   ├── Player.java                # Rich domain model
│   └── UserProvider.java          # Enum
├── exceptions/                    # Domain exceptions
│   ├── ErrorResponse.java         # Error response DTO
│   ├── GlobalExceptionHandler.java # @RestControllerAdvice
│   ├── InsufficientBalanceException.java
│   ├── PlayerNotFoundException.java
│   └── SessionExpiredException.java
├── repositories/                   # Spring Data JPA repositories
│   ├── BetRepository.java
│   └── PlayerRepository.java
├── services/                      # Business logic
│   ├── dto/                       # Data transfer objects
│   │   └── Bet.java               # JPA entity with domain behavior
│   ├── handler/                   # Game handlers
│   └── vo/                        # Value objects (games)
└── utils/                         # Utilities
```

## Configuration

### Application Properties

```properties
# Server
server.port=9095

# JobRunr Dashboard
org.jobrunr.dashboard.port=8000
org.jobrunr.background-job-server.enabled=true

# Database (postgres profile)
spring.datasource.url=jdbc:postgresql://localhost:5432/casino
spring.datasource.username=casino_user
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false

# Logging
logging.level.net.jordimp.casino=DEBUG
```

### Game Configuration

Games are configured via `conf.properties`:

```properties
videobingo.name=Video Bingo
videobingo.uuid=VIDEOBINGO-UUID
videobingo.type=BINGO
videobingo.prize=50.0
videobingo.prob=0.3
videobingo.minbet=1
videobingo.maxbet=10
```

## Testing

### Test Coverage

- **Unit Tests:** Domain model behavior (Player.canPlaceBet, Bet.calculateWin)
- **Integration Tests:** Repositories, Services, Exception Handler
- **E2E Tests:** Complete API flows (login → bet → logout)

**Current Coverage:** 49 tests across 12 test classes

### Run Tests

```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=PlayerEntityTests

# Specific test method
mvn test -Dtest=BetTests#testBalanceCalculationIsAtomic

# With coverage report
mvn test jacoco:report
```

### Test Profiles

- **Default:** Uses H2 in-memory database (fast)
- **Test:** Uses H2 in-memory database with `create-drop`

## Code Quality

### Spotless - Code Formatting

Automatically formats Java code using Google Java Format:

```bash
# Check format
mvn spotless:check

# Apply formatting
mvn spotless:apply
```

Configuration: Palantir Java Format 2.38.0

### JaCoCo - Code Coverage

Enforces 85% code coverage threshold:

```bash
# Run tests with coverage
mvn test

# Generate HTML report
mvn jacoco:report

# View report
open target/site/jacoco/index.html
```

### Codecov - Coverage Reporting

Continuous integration coverage reporting:

```bash
# Upload coverage after tests
curl -s https://codecov.io/bash
```

Coverage badge and reports available at: https://codecov.io/gh/jordimarsal/spring-boot-casino

## Development

### Build

```bash
# Clean build
mvn clean package

# Skip tests during build
mvn clean package -DskipTests
```

### Run Application

```bash
# Default (postgres profile)
mvn spring-boot:run

# Test profile
mvn spring-boot:run -Dspring.profiles.active=test
```

## Production Readiness

### Completed
- PostgreSQL persistence with JPA/Hibernate
- Transaction management with @Transactional
- Global exception handling
- Comprehensive test coverage
- Code formatting with Spotless
- Code coverage enforcement with JaCoCo
- Audit trail for all bets (created_at timestamps)
- Input validation on all endpoints
- Thread-safe concurrent access

### Pending
- Authentication/authorization
- Rate limiting
- Performance testing
- Security audit

## Contributing

1. Format code: `mvn spotless:apply`
2. Run tests: `mvn test`
3. Check coverage: `mvn jacoco:report`
4. Ensure coverage ≥ 85%

## License

[Your License Here]

---

**By Jordi Marsal - Sabadell / Octubre 2020**

**Database Migration:** February 2026
