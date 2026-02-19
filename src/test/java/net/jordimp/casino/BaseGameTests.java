package net.jordimp.casino;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.Optional;
import net.jordimp.casino.entity.Player;
import net.jordimp.casino.entity.UserProvider;
import net.jordimp.casino.services.dto.Bet;
import net.jordimp.casino.services.handler.Jugada;
import net.jordimp.casino.services.vo.Roulette;
import net.jordimp.casino.utils.EnvWrapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

public class BaseGameTests {

  // Constants for test values matching createMockEnvironment()
  private static final double MOCK_MIN_BET = 5.0;
  private static final double MOCK_MAX_BET = 500.0;
  private static final double MOCK_PRIZE = 100.0;

  @BeforeEach
  void setUp() {
    // Clear the static environment before each test
    EnvWrapperUtils.setEnv(null);
  }

  /**
   * Helper method to create a mock environment with specific probability.
   * This ensures deterministic testing for win/loss scenarios.
   *
   * @param probability the game probability (0.0 = always lose, 1.0 = always win)
   * @return a configured Environment mock
   */
  private Environment createMockEnvironmentWithProbability(double probability) {
    return new Environment() {
      @Override
      public String getProperty(String key) {
        if (key.endsWith(".name")) return "Test Game";
        if (key.endsWith(".uuid")) return "test-uuid";
        if (key.endsWith(".type")) return "Test Type";
        if (key.endsWith(".prize")) return String.valueOf(MOCK_PRIZE);
        if (key.endsWith(".prob")) return String.valueOf(probability);
        if (key.endsWith(".minbet")) return String.valueOf(MOCK_MIN_BET);
        if (key.endsWith(".maxbet")) return String.valueOf(MOCK_MAX_BET);
        return "test";
      }

      @Override
      public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return "test".equals(value) ? defaultValue : value;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType) {
        return null;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return defaultValue;
      }

      @Override
      public String getRequiredProperty(String key) {
        return getProperty(key);
      }

      @Override
      public <T> T getRequiredProperty(String key, Class<T> targetType) {
        return null;
      }

      @Override
      public String resolvePlaceholders(String text) {
        return text;
      }

      @Override
      public String resolveRequiredPlaceholders(String text) {
        return text;
      }

      @Override
      public boolean acceptsProfiles(String... profiles) {
        return true;
      }

      @Override
      public boolean acceptsProfiles(org.springframework.core.env.Profiles profiles) {
        return true;
      }

      @Override
      public String[] getActiveProfiles() {
        return new String[0];
      }

      @Override
      public String[] getDefaultProfiles() {
        return new String[0];
      }

      @Override
      public boolean containsProperty(String key) {
        return true;
      }
    };
  }

  @Test
  void testInitWithNullEnvironmentThrowsException() {
    // Create a game instance that will fail during construction
    // since env is not autowired in unit test and EnvWrapperUtils.getEnv() returns null
    Exception exception =
        assertThrows(
            IllegalStateException.class,
            () -> {
              new Roulette();
            });

    assertTrue(exception.getMessage().contains("Environment not initialized"));
  }

  @Test
  void testGettersAndSetters() {
    // Setup environment with required properties
    Environment mockEnv = createMockEnvironment();
    EnvWrapperUtils.setEnv(mockEnv);

    Roulette game = new Roulette();
    game.setName("Test Roulette");
    game.setUUID("test-uuid-123");
    game.setType("Table Game");
    game.setPrize(100.0);
    game.setProbability(0.5);
    game.setMinBet(5.0);
    game.setMaxBet(500.0);

    assertEquals("Test Roulette", game.getName());
    assertEquals("test-uuid-123", game.getUUID());
    assertEquals("Table Game", game.getType());
    assertEquals(100.0, game.getPrize());
    assertEquals(0.5, game.getProbability());
    assertEquals(5.0, game.getMinBet());
    assertEquals(500.0, game.getMaxBet());
  }

  @Test
  void testToString() {
    // Setup environment
    Environment mockEnv = createMockEnvironment();
    EnvWrapperUtils.setEnv(mockEnv);

    Roulette game = new Roulette();
    game.setName("Test Roulette");
    game.setUUID("test-uuid-123");
    game.setType("Table Game");
    game.setPrize(100.0);
    game.setProbability(0.5);
    game.setMinBet(5.0);
    game.setMaxBet(500.0);

    String result = game.toString();
    assertTrue(result.contains("Test Roulette"));
    assertTrue(result.contains("test-uuid-123"));
    assertTrue(result.contains("Table Game"));
    assertTrue(result.contains("100.0"));
    assertTrue(result.contains("0.5"));
    assertTrue(result.contains("5.0"));
    assertTrue(result.contains("500.0"));
  }

  @Test
  void testGetMinBetInitializesFromEnv() {
    // Setup environment
    Environment mockEnv = createMockEnvironment();
    EnvWrapperUtils.setEnv(mockEnv);

    // Test that getMinBet triggers initialization
    Roulette game = new Roulette();
    // Even without env, should return a value (possibly null or default)
    Double minBet = game.getMinBet();
    assertNotNull(minBet);
  }

  @Test
  void testGetMaxBetInitializesFromEnv() {
    // Setup environment
    Environment mockEnv = createMockEnvironment();
    EnvWrapperUtils.setEnv(mockEnv);

    // Test that getMaxBet triggers initialization
    Roulette game = new Roulette();
    // Even without env, should return a value (possibly null or default)
    Double maxBet = game.getMaxBet();
    assertNotNull(maxBet);
  }

  // Helper method to create a mock environment
  private Environment createMockEnvironment() {
    return new Environment() {
      @Override
      public String getProperty(String key) {
        // Return default values for known properties
        if (key.endsWith(".name")) return "Test Game";
        if (key.endsWith(".uuid")) return "test-uuid";
        if (key.endsWith(".type")) return "Test Type";
        if (key.endsWith(".prize")) return "100.0";
        if (key.endsWith(".prob")) return "0.5";
        if (key.endsWith(".minbet")) return "5.0";
        if (key.endsWith(".maxbet")) return "500.0";
        return "test";
      }

      @Override
      public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return "test".equals(value) ? defaultValue : value;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType) {
        return null;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return defaultValue;
      }

      @Override
      public String getRequiredProperty(String key) {
        return getProperty(key);
      }

      @Override
      public <T> T getRequiredProperty(String key, Class<T> targetType) {
        return null;
      }

      @Override
      public String resolvePlaceholders(String text) {
        return text;
      }

      @Override
      public String resolveRequiredPlaceholders(String text) {
        return text;
      }

      @Override
      public boolean acceptsProfiles(String... profiles) {
        return true;
      }

      @Override
      public boolean acceptsProfiles(org.springframework.core.env.Profiles profiles) {
        return true;
      }

      @Override
      public String[] getActiveProfiles() {
        return new String[0];
      }

      @Override
      public String[] getDefaultProfiles() {
        return new String[0];
      }

      @Override
      public boolean containsProperty(String key) {
        return true;
      }
    };
  }

  @Test
  void testInitWithInvalidNumericValuesUsesDefaults() {
    // Create environment with invalid numeric values
    Environment mockEnv = new Environment() {
      @Override
      public String getProperty(String key) {
        if (key.endsWith(".name")) return "Game With Invalid Numbers";
        if (key.endsWith(".uuid")) return "uuid-123";
        if (key.endsWith(".type")) return "Type";
        if (key.endsWith(".prize")) return "invalid_number";
        if (key.endsWith(".prob")) return "";
        if (key.endsWith(".minbet")) return null;
        if (key.endsWith(".maxbet")) return "abc";
        return "test";
      }

      @Override
      public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return "test".equals(value) ? defaultValue : value;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType) {
        return null;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return defaultValue;
      }

      @Override
      public String getRequiredProperty(String key) {
        return getProperty(key);
      }

      @Override
      public <T> T getRequiredProperty(String key, Class<T> targetType) {
        return null;
      }

      @Override
      public String resolvePlaceholders(String text) {
        return text;
      }

      @Override
      public String resolveRequiredPlaceholders(String text) {
        return text;
      }

      @Override
      public boolean acceptsProfiles(String... profiles) {
        return true;
      }

      @Override
      public boolean acceptsProfiles(org.springframework.core.env.Profiles profiles) {
        return true;
      }

      @Override
      public String[] getActiveProfiles() {
        return new String[0];
      }

      @Override
      public String[] getDefaultProfiles() {
        return new String[0];
      }

      @Override
      public boolean containsProperty(String key) {
        return true;
      }
    };

    EnvWrapperUtils.setEnv(mockEnv);

    Roulette game = new Roulette();

    // Should use defaults when parsing fails
    assertEquals(0.0, game.getPrize());
    assertEquals(0.0, game.getProbability());
    assertEquals(0.0, game.getMinBet());
    assertEquals(0.0, game.getMaxBet());
  }

  @Test
  void testGettersAfterInitWithValidEnvironment() {
    Environment mockEnv = createMockEnvironment();
    EnvWrapperUtils.setEnv(mockEnv);

    Roulette game = new Roulette();

    // Verify all getters return values from environment
    assertNotNull(game.getName());
    assertNotNull(game.getUUID());
    assertNotNull(game.getType());
    assertNotNull(game.getPrize());
    assertNotNull(game.getProbability());
  }

  // ==================== Jugada Logic Tests ====================

  @Test
  void testBet_withNullPlayerUUID_returnsBadBet() {
    // Arrange
    Bet bet = TestUtils.createValidBet();
    bet.setPlayerUUID(null);
    Player player = TestUtils.createValidPlayer();

    // Act
    Bet result = Jugada.bet(bet, Optional.of(player));

    // Assert
    assertTrue(result.isBad());
    assertEquals(Bet.E_NULL, result.getWarning());
    assertEquals(0.0, result.getPrizeAmount());
  }

  @Test
  void testBet_withNullGameUUID_returnsBadBet() {
    // Arrange
    Bet bet = TestUtils.createValidBet();
    bet.setGameUUID(null);
    Player player = TestUtils.createValidPlayer();

    // Act
    Bet result = Jugada.bet(bet, Optional.of(player));

    // Assert
    assertTrue(result.isBad());
    assertEquals(Bet.E_NULL, result.getWarning());
    assertEquals(0.0, result.getPrizeAmount());
  }

  @Test
  void testBet_withExhaustedBalance_returnsBadBet() {
    // Arrange
    Bet bet = TestUtils.createValidBet();
    bet.setBetAmount(10.0);
    bet.setBalancePlayer(5.0); // Balance less than bet amount
    Player player = TestUtils.createValidPlayer();

    // Act
    Bet result = Jugada.bet(bet, Optional.of(player));

    // Assert
    assertTrue(result.isBad());
    assertEquals(Bet.E_NO_FUNDS, result.getWarning());
    assertEquals(0.0, result.getPrizeAmount());
  }

  @Test
  void testBet_withExpiredSession_returnsBadBet() {
    // Arrange
    Bet bet = TestUtils.createValidBet();
    Player player = TestUtils.createValidPlayer();
    // Set login date to 2 hours ago (beyond the 1 hour maxTime)
    Date pastDate = new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000));
    player.setLoginDate(pastDate);
    player.setMaxTime(3600L); // 1 hour max session time

    // Act
    Bet result = Jugada.bet(bet, Optional.of(player));

    // Assert
    assertTrue(result.isBad());
    assertEquals(Bet.W_NO_TIME, result.getWarning());
    assertEquals(0.0, result.getPrizeAmount());
  }

  @Test
  void testBet_withNonExistentGame_returnsBadBet() {
    // Arrange
    Bet bet = TestUtils.createValidBet();
    bet.setGameUUID("non-existent-uuid");
    Player player = TestUtils.createValidPlayer();

    // Act
    Bet result = Jugada.bet(bet, Optional.of(player));

    // Assert
    assertTrue(result.isBad());
    assertEquals(Bet.W_NO_GAME, result.getWarning());
    assertEquals(0.0, result.getPrizeAmount());
  }

  @Test
  void testBet_withAmountBelowMinBet_returnsBadBet() {
    // Arrange
    Environment mockEnv = createMockEnvironment();
    EnvWrapperUtils.setEnv(mockEnv);

    Bet bet = TestUtils.createValidBet();
    bet.setBetAmount(1.0); // Below the MOCK_MIN_BET
    Player player = TestUtils.createValidPlayer();
    double initialBalance = bet.getBalancePlayer();

    // Act
    Bet result = Jugada.bet(bet, Optional.of(player));

    // Assert
    assertTrue(result.isBad());
    assertEquals(Bet.W_LIMIT_BET, result.getWarning());
    assertEquals(0.0, result.getPrizeAmount());
    assertEquals(initialBalance, result.getBalancePlayer()); // Balance unchanged for bad bet
  }

  @Test
  void testBet_withAmountAboveMaxBet_returnsBadBet() {
    // Arrange
    Environment mockEnv = createMockEnvironment();
    EnvWrapperUtils.setEnv(mockEnv);

    Bet bet = TestUtils.createValidBet();
    bet.setBetAmount(10000.0); // Above the MOCK_MAX_BET
    bet.setBalancePlayer(20000.0); // Sufficient balance to avoid E_NO_FUNDS
    Player player = TestUtils.createValidPlayer();
    double initialBalance = bet.getBalancePlayer();

    // Act
    Bet result = Jugada.bet(bet, Optional.of(player));

    // Assert
    assertTrue(result.isBad());
    assertEquals(Bet.W_LIMIT_BET, result.getWarning());
    assertEquals(0.0, result.getPrizeAmount());
    assertEquals(initialBalance, result.getBalancePlayer()); // Balance unchanged for bad bet
  }

  @Test
  void testBet_withEmptyPlayerOptional_returnsBadBet() {
    // Arrange
    Bet bet = TestUtils.createValidBet();

    // Act
    Bet result = Jugada.bet(bet, Optional.empty());

    // Assert
    assertTrue(result.isBad());
    assertEquals(Bet.W_NO_TIME, result.getWarning());
    assertEquals(0.0, result.getPrizeAmount());
  }

  @Test
  void testBet_withAmountExactlyMinBet_succeeds() {
    // Arrange
    Environment mockEnv = createMockEnvironment();
    EnvWrapperUtils.setEnv(mockEnv);

    // Use unique game UUID to avoid caching issues
    Bet bet = TestUtils.createValidBetWithGameUUID("ROULETTE-MIN-UUID");
    bet.setBetAmount(MOCK_MIN_BET); // Exactly at the minimum
    Player player = TestUtils.createValidPlayer();
    double initialBalance = bet.getBalancePlayer();

    // Act
    Bet result = Jugada.bet(bet, Optional.of(player));

    // Assert
    assertFalse(result.isBad(), "Bet should not be bad when amount equals minBet");
    assertNotNull(result.getComment());
    assertNotNull(result.getBalancePlayer());
    // Balance should be updated (decreased by bet amount, possibly increased by prize)
    assertTrue(result.getBalancePlayer() >= 0.0, "Balance should be non-negative");
  }

  @Test
  void testBet_withAmountExactlyMaxBet_succeeds() {
    // Arrange
    Environment mockEnv = createMockEnvironment();
    EnvWrapperUtils.setEnv(mockEnv);

    // Use unique game UUID to avoid caching issues
    Bet bet = TestUtils.createValidBetWithGameUUID("ROULETTE-MAX-UUID");
    bet.setBetAmount(MOCK_MAX_BET); // Exactly at the maximum
    bet.setBalancePlayer(1000.0); // Sufficient balance
    Player player = TestUtils.createValidPlayer();
    double initialBalance = bet.getBalancePlayer();

    // Act
    Bet result = Jugada.bet(bet, Optional.of(player));

    // Assert
    assertFalse(result.isBad(), "Bet should not be bad when amount equals maxBet");
    assertNotNull(result.getComment());
    assertNotNull(result.getBalancePlayer());
    // Balance should be updated (decreased by bet amount, possibly increased by prize)
    assertTrue(result.getBalancePlayer() >= 0.0, "Balance should be non-negative");
  }

  @Test
  void testBet_withValidParameters_loss_returnsBadBet() {
    // Arrange - Create environment with probability=0 to guarantee loss
    Environment mockEnv = createMockEnvironmentWithProbability(0.0);
    EnvWrapperUtils.setEnv(mockEnv);

    // Use unique game UUID to avoid caching issues
    Bet bet = TestUtils.createValidBetWithGameUUID("ROULETTE-LOSS-UUID");
    bet.setBetAmount(10.0);
    bet.setBalancePlayer(100.0);
    Player player = TestUtils.createValidPlayer();
    double initialBalance = bet.getBalancePlayer();

    // Act
    Bet result = Jugada.bet(bet, Optional.of(player));

    // Assert
    assertFalse(result.isBad());
    assertEquals(0.0, result.getPrizeAmount(), 0.001); // No prize for losing bet
    assertEquals(initialBalance - bet.getBetAmount(), result.getBalancePlayer(), 0.001); // Balance decreased by bet amount
    assertEquals("Bet done", result.getComment());
  }

  @Test
  void testBet_withValidParameters_win_returnsBadBet() {
    // Arrange - Create environment with probability=1 to guarantee win
    Environment mockEnv = createMockEnvironmentWithProbability(1.0);
    EnvWrapperUtils.setEnv(mockEnv);

    // Use unique game UUID to avoid caching issues
    Bet bet = TestUtils.createValidBetWithGameUUID("ROULETTE-WIN-UUID");
    bet.setBetAmount(10.0);
    bet.setBalancePlayer(100.0);
    Player player = TestUtils.createValidPlayer();
    double initialBalance = bet.getBalancePlayer();

    // Act
    Bet result = Jugada.bet(bet, Optional.of(player));

    // Assert
    assertFalse(result.isBad());
    assertEquals(MOCK_PRIZE, result.getPrizeAmount(), 0.001); // Won the prize
    assertEquals(initialBalance - bet.getBetAmount() + MOCK_PRIZE, result.getBalancePlayer(), 0.001); // Balance: -bet + prize
    assertTrue(result.getComment().contains("WIN"));
  }
}
