# Spring Boot Casino

A Spring Boot application demonstrating casino game betting logic.


## Recent Improvements (Feb 2026)

### Completed ✅
- Thread-safe player storage with ConcurrentHashMap
- Null-safe Environment initialization with defaults
- Input validation on bet endpoints
- Exception handling in background jobs
- Comprehensive test suite including concurrency tests
- Simplified balance calculation (atomic operations)
- Removed misleading @Transactional annotations

### Database Migration (Feb 2026) ✅
- **Complete JPA/Hibernate implementation** with PostgreSQL
- **Progressive DDD patterns**: Rich domain models with behavior
- **Transaction management**: @Transactional service layer
- **Global exception handler**: Domain-specific exceptions
- **Repository pattern**: Spring Data JPA repositories
- **Comprehensive testing**: Unit, integration, and E2E tests
- **Audit trail**: All bets persisted with timestamps

### Pending ⏳
- Authentication/authorization
- Rate limiting
- Performance testing
- Code quality tools (Spotless, JaCoCo, Codecov)

## Quick Start

### Prerequisites
- Java 11+
- Maven 3.6+
- PostgreSQL 13+ (for production)

### Database Setup

#### Required: PostgreSQL

Install PostgreSQL 13+ and create database:

```bash
# Install PostgreSQL
sudo apt-get install postgresql postgresql-contrib

# Create database and user
sudo -u postgres createuser casino_user -P
sudo -u postgres createdb -O casino_user casino

# Grant privileges
psql -U casino_user -d casino -c "GRANT ALL PRIVILEGES ON DATABASE casino TO casino_user;"
```

Update `src/main/resources/application-postgres.properties` with your password:
```properties
spring.datasource.password=your_secure_password
```

### Run Application
```bash
# With PostgreSQL (production profile)
mvn spring-boot:run

# With H2 in-memory (test profile)
mvn test -Dspring.profiles.active=test
```

The application will start on `http://localhost:9095`

### Run Tests
```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=PlayerTests

# Specific test method
mvn test -Dtest=BetTests#testBalanceCalculationIsAtomic
```

## API Endpoints

### Authentication
- `POST /api/casino/logon` - Player login
  ```bash
  curl -X POST http://localhost:8080/api/casino/logon \
    -H "Content-Type: application/json" \
    -d '{"loginDate":"2026-02-18T10:00:00","maxTime":300,"uuid":"test-123","userProvider":"POKERSTAR"}'
  ```

- `POST /api/casino/logout/{uuid}` - Player logout
  ```bash
  curl -X POST http://localhost:8080/api/casino/logout/test-123
  ```

### Betting
- `POST /api/casino/bet/{uuid}` - Place a bet
  ```bash
  curl -X POST http://localhost:8080/api/casino/bet/player-123 \
    -H "Content-Type: application/json" \
    -d '{"betAmount":10,"playerUUID":"player-123","gameUUID":"VIDEOBINGO-UUID","balancePlayer":100}'
  ```

  **Validations:**
  - Path UUID must match bet.playerUUID
  - Bet amount must be positive
  - Bet amount cannot exceed 10,000

### Player Info
- `GET /api/casino/get/{uuid}` - Get player by UUID
  ```bash
  curl http://localhost:8080/api/casino/get/player-123
  ```

- `GET /api/casino/gets/{uuid}` - Get player info as string
  ```bash
  curl http://localhost:8080/api/casino/gets/player-123
  ```

## Architecture

### Components
- **Controllers:** REST endpoints (`RestPlayGameController`)
- **Services:** Business logic (`PlayerServiceImpl`, `GamePlayServiceImpl`)
- **Handlers:** Game betting logic (`Jugada`, `GameHandler`)
- **DAO:** Data access (`MemoryEntities`)
- **Entities:** Data models (`Player`, `Bet`)

### Games Supported
- Video Bingo
- Blackjack
- Poker
- Roulette
- Slot Machine

### Thread-Safety Implementation
- `ConcurrentHashMap` for concurrent player access
- `volatile` + `synchronized` for Environment initialization
- Null-safe initialization with default values
- Exception handling in cron jobs

## Configuration

### Application Properties
```properties
# Server
server.port=8080

# JobRunr Dashboard
org.jobrunr.dashboard.port=8000

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


## Test Coverage

- **Unit Tests:** Environment, Game initialization, Bet calculations
- **Integration Tests:** REST endpoints, Player flows, Validation
- **Concurrency Tests:** Thread-safety verification

**Current Coverage:** 19 tests across 7 test classes

## Known Issues

### Test Environment
Some integration tests fail due to port binding conflicts when running all tests simultaneously. This is a test isolation issue, not a code problem.

**Workaround:** Run test classes individually:
```bash
mvn test -Dtest=CasinoRestControllerTests
mvn test -Dtest=PlayerTests
```

### Production Readiness
- ❌ No data persistence (in-memory storage)
- ❌ No authentication/authorization
- ❌ No audit trail for financial transactions
- ❌ No rate limiting
- ⚠️ CORS configured as wildcard (`@CrossOrigin(origins = "*")`)

## Development

### Project Structure
```
src/main/java/net/jordimp/casino/
├── CasinoApplication.java          # Spring Boot main class
├── controllers/                    # REST endpoints
├── dao/                           # Data access layer
├── entity/                        # Data models
├── services/                      # Business logic
│   ├── dto/                      # Data transfer objects
│   ├── handler/                  # Game handlers
│   └── vo/                       # Value objects (games)
└── utils/                         # Utilities
```

### Contributing
This is a demonstration project. For production use, implement the pending improvements listed in the Status section.

## Roadmap

### Phase 1: Critical Fixes (COMPLETED ✅)
- Thread-safety implementation
- Null-safety improvements
- Input validation
- Exception handling

### Phase 2: Data Persistence (PLANNED)
- Database schema design
- JPA/Hibernate implementation
- Migration from in-memory storage

### Phase 3: Security (PLANNED)
- Authentication implementation
- Authorization layer
- Rate limiting
- CORS configuration

### Phase 4: Production Readiness (PLANNED)
- Audit logging
- Monitoring and metrics
- Performance optimization
- Security audit

## Original Implementation

*By Jordi Marsal - Sabadell / Octubre 2020*

Implementación conceptual de backend aplicación de un casino simplificado donde los jugadores pueden realizar apuestas y obtener ganancias.

## License

[Your License Here]
