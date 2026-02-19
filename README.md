# Spring Boot Casino

[![codecov](https://codecov.io/gh/jordimarsal/spring-boot-casino/branch/master/graph/badge.svg)](https://codecov.io/gh/jordimarsal/spring-boot-casino)

A Spring Boot application demonstrating casino game betting logic with PostgreSQL persistence and Domain-Driven Design patterns.

## Features

### Core Functionality
- Player session management with timeout control
- Multi-game betting platform (Video Bingo, Blackjack, Poker, Roulette, Slots)
- Transactional bet processing with audit trail
- RESTful API with comprehensive input validation
- Background job scheduling with JobRunr

### Architecture Highlights
- **PostgreSQL + JPA/Hibernate** - Database persistence with @Transactional guarantees
- **Service Layer Pattern** - Business logic separated from controllers
- **Repository Pattern** - Spring Data JPA for data access
- **Factory Pattern** - Game instantiation through `GameFactory`
- **Global Exception Handler** - Domain exceptions with proper HTTP status codes
- **Comprehensive Testing** - 140 tests with 90%+ branch coverage

## Technologies
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
# With PostgreSQL (default profile)
mvn spring-boot:run

# Run tests
mvn test
```

The application will start on `http://localhost:9095`

## API Endpoints

### Authentication

#### Login Player
```bash
curl -X POST http://localhost:9095/api/casino/logon \
  -H "Content-Type: application/json" \
  -d '{
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
    "gameUUID": "ROULETTE-UUID"
  }'
```

**Validations:**
- Path UUID must match bet.playerUUID
- Bet amount must be positive (> 0)
- Maximum bet: 10,000

### Player Info

#### Get Player (JSON)
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
│  Services   │ Business logic (@Transactional)
└──────┬──────┘
       │
┌──────▼──────┐
│  Handlers   │ Game-specific logic (Jugada)
│  + Factory  │ Game instantiation
└──────┬──────┘
       │
┌──────▼──────┐
│ Repositories│ Spring Data JPA
└──────┬──────┘
       │
┌──────▼──────┐
│  Entities   │ JPA entities (Player, Bet)
└──────┬──────┘
       │
┌──────▼──────┐
│ PostgreSQL  │ Database
└─────────────┘
```

### Package Structure

```
src/main/java/net/jordimp/casino/
├── CasinoApplication.java         # Spring Boot main class
├── config/                        # Configuration
│   └── JpaConfig.java             # JPA auditing
├── controllers/                   # REST endpoints
│   └── RestPlayGameController.java
├── entity/                        # JPA entities
│   ├── Player.java                # Player entity
│   └── UserProvider.java          # Enum
├── exceptions/                    # Domain exceptions
│   ├── ErrorResponse.java         # Error response DTO
│   ├── GlobalExceptionHandler.java # @RestControllerAdvice
│   ├── InsufficientBalanceException.java
│   ├── PlayerNotFoundException.java
│   └── SessionExpiredException.java
├── repositories/                   # Spring Data JPA
│   ├── BetRepository.java
│   └── PlayerRepository.java
├── services/                      # Business logic
│   ├── dto/                       # Data transfer objects
│   │   └── Bet.java               # JPA entity with domain behavior
│   ├── factory/                   # Factory pattern
│   │   ├── AbstractFactory.java
│   │   └── GameFactory.java       # Game instantiation
│   ├── handler/                   # Game betting logic
│   │   ├── GameHandler.java       # Game registry
│   │   └── Jugada.java           # Bet processing logic
│   ├── vo/                        # Value objects (games)
│   │   ├── BaseGame.java          # Abstract game class
│   │   ├── VideoBingo.java
│   │   ├── Blackjack.java
│   │   ├── Poker.java
│   │   ├── Roulette.java
│   │   └── Slot.java
│   ├── GamePlayServiceImpl.java   # Main service
│   └── PlayerServiceImpl.java    # Player management
├── utils/                         # Utilities
│   ├── CasinoLoggerUtils.java    # Logging wrapper
│   ├── EnvWrapperUtils.java      # Environment access
│   └── Utils.java                # String formatting
└── StartUpInit.java              # Startup initialization
```

### Key Components

- **Controllers:** `RestPlayGameController` - REST API endpoints
- **Services:** `GamePlayServiceImpl` (bet processing), `PlayerServiceImpl` (player management)
- **Handlers:** `Jugada` (bet validation + game interaction), `GameHandler` (game registry)
- **Factory:** `GameFactory` (creates game instances by UUID)
- **Entities:** `Player` (JPA entity), `Bet` (JPA entity with domain logic)
- **Value Objects:** `BaseGame` + subclasses (VideoBingo, Blackjack, Poker, Roulette, Slot)

## Games Supported

Each game has configurable parameters (prize, probability, min/max bet):

- **Video Bingo** (`VIDEOBINGO-UUID`) - 30% win probability
- **Blackjack** (`BLACKJACK-UUID`)
- **Poker** (`POKER-UUID`)
- **Roulette** (`ROULETTE-UUID`) - 100:1 payout
- **Slot Machine** (`SLOT-UUID`)

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
roulette.name=Roulette
roulette.uuid=ROULETTE-UUID
roulette.type=ROULETTE
roulette.prize=100.0
roulette.prob=0.5
roulette.minbet=5
roulette.maxbet=500
```

## Testing

### Test Coverage

**Current Metrics:**
- **140 tests** across 22 test classes
- **90% branch coverage** (JaCoCo enforced threshold: 85%)
- **92% complexity coverage**
- **96% instruction coverage**

### Test Types

- **Unit Tests:** Domain models, utilities, handlers
- **Integration Tests:** Repositories, services, controllers
- **E2E Tests:** Complete flows (login → bet → logout)

### Run Tests

```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=BaseGameTests

# With coverage report
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

## Code Quality

### Spotless - Code Formatting

Automatically formats Java code using Google Java Format:

```bash
# Check format
mvn spotless:check

# Apply formatting
mvn spotless:apply
```

### JaCoCo - Code Coverage

Enforces 85% code coverage threshold:

```bash
# Run tests with coverage check
mvn clean verify

# Generate HTML report
mvn jacoco:report

# View report
open target/site/jacoco/index.html
```

### Codecov - Coverage Reporting

Continuous integration coverage reporting:

```bash
# Upload coverage after tests
bash <(curl -s https://codecov.io/bash)
```

Coverage badge and reports: https://codecov.io/gh/jordimarsal/spring-boot-casino

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

# Test profile (H2 in-memory)
mvn spring-boot:run -Dspring.profiles.active=test
```

## Production Readiness

### Completed
- ✅ PostgreSQL persistence with JPA/Hibernate
- ✅ Transaction management with @Transactional
- ✅ Global exception handling
- ✅ Comprehensive test coverage (140 tests, 90%+ branch)
- ✅ Code formatting with Spotless
- ✅ Code coverage enforcement with JaCoCo (85% threshold)
- ✅ Audit trail for all bets (created_at timestamps)
- ✅ Input validation on all endpoints
- ✅ Session timeout management

### Pending
- ⏳ Authentication/authorization
- ⏳ Rate limiting
- ⏳ Performance testing
- ⏳ Security audit

## License

This project is licensed under the MIT License.

---

**By Jordi Marsal - Sabadell / Octubre 2020**
**Database Migration & Java 21 update:** February 2026
**Test coverage improvement:** February 2026
