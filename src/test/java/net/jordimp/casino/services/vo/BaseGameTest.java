package net.jordimp.casino.services.vo;

import static org.junit.jupiter.api.Assertions.*;

import net.jordimp.casino.utils.EnvWrapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

/**
 * Test class for BaseGame focusing on environment initialization coverage.
 * Tests the ensureInitializedFromEnvIfNeeded() method which has low coverage.
 */
public class BaseGameTest {

  @BeforeEach
  void setUp() {
    // Clear the static environment before each test
    EnvWrapperUtils.setEnv(null);
  }

  /**
   * Test Game class for testing BaseGame functionality.
   * Allows us to control the bprefix for different test scenarios.
   */
  private static class TestGame extends BaseGame {
    public TestGame(String prefix) {
      super(prefix);
    }
  }

  @Test
  void testGameInitialization_withValidPrefix_initializesFromEnvironment() {
    // Arrange - Setup environment with game properties
    Environment mockEnv = createMockEnvironment("testgame", "Test Game", "10.0", "0.5", "5.0", "100.0");
    EnvWrapperUtils.setEnv(mockEnv);

    // Act - Create game with valid prefix and get name
    TestGame game = new TestGame("testgame");
    String name = game.getName();

    // Assert - Game should be properly initialized from environment
    assertNotNull(game, "Game instance should not be null");
    assertNotNull(name, "Name should not be null when prefix is set and env is available");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withAlreadyInitializedProperties_returnsEarly() {
    // Arrange - Create TestGame with "game" prefix and setup environment
    Environment mockEnv = createMockEnvironment("game", "Env Game Name", "50.0", "0.7", "10.0", "200.0");
    EnvWrapperUtils.setEnv(mockEnv);

    TestGame game = new TestGame("game");

    // Act - Manually set properties to different values
    game.setName("Manual Name");
    game.setMinBet(25.0);
    game.setMaxBet(150.0);

    // Call getMinBet() which triggers ensureInitializedFromEnvIfNeeded()
    // Since properties are already set, it returns early without overwriting
    Double minBet = game.getMinBet();
    Double maxBet = game.getMaxBet();

    // Assert - Values should remain as manually set (not overwritten by environment)
    assertEquals("Manual Name", game.getName(), "Name should remain as manually set");
    assertEquals(25.0, minBet, "MinBet should remain as manually set");
    assertEquals(150.0, maxBet, "MaxBet should remain as manually set");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withNullEnvironment_returnsEarly() {
    // Arrange - Setup environment first, create game, then clear environment
    Environment mockEnv = createMockEnvironment("game", "Test Game", "10.0", "0.5", "5.0", "100.0");
    EnvWrapperUtils.setEnv(mockEnv);

    TestGame game = new TestGame("game");

    // Clear environment to test ensureInitializedFromEnvIfNeeded with null env
    EnvWrapperUtils.setEnv(null);

    // Act - Call getMinBet() or getMaxBet() which triggers ensureInitializedFromEnvIfNeeded()
    // Since properties are already initialized, it should return early without accessing env
    Double minBet = game.getMinBet();
    Double maxBet = game.getMaxBet();

    // Assert - Should return without throwing exception
    // Values remain from previous initialization
    assertNotNull(minBet, "MinBet should return previously initialized value");
    assertNotNull(maxBet, "MaxBet should return previously initialized value");
  }

  @Test
  void testParseSafely_withNullValue_returnsDefault() {
    // Arrange - Create TestGame
    Environment mockEnv = createMockEnvironmentWithNulls();
    EnvWrapperUtils.setEnv(mockEnv);

    TestGame game = new TestGame("game");

    // Act - getPrize() triggers initialization which uses parseSafely
    Double prize = game.getPrize();

    // Assert - Prize should not be null (uses default value when null is parsed)
    assertNotNull(prize, "Prize should not be null when parseSafely uses default");
    assertEquals(0.0, prize, "Prize should default to 0.0 when value is null");
  }

  @Test
  void testGetMinBetAndGetMaxBet_triggerEnsureInitializedFromEnvIfNeeded() {
    // Arrange - Create environment and game
    Environment mockEnv = createMockEnvironment("game", "Env Name", "100.0", "0.8", "15.0", "300.0");
    EnvWrapperUtils.setEnv(mockEnv);

    TestGame game = new TestGame("game");

    // Act - Call getMinBet and getMaxBet which trigger ensureInitializedFromEnvIfNeeded
    // Since properties are already set by constructor, the method should return early
    Double minBet = game.getMinBet();
    Double maxBet = game.getMaxBet();

    // Assert - Values should be from environment
    assertNotNull(minBet, "MinBet should not be null");
    assertNotNull(maxBet, "MaxBet should not be null");
    assertEquals(15.0, minBet, "MinBet should match environment value");
    assertEquals(300.0, maxBet, "MaxBet should match environment value");
  }

  /**
   * Creates a mock environment with configurable property values.
   *
   * @param prefix the game property prefix
   * @param name the game name
   * @param prize the prize value
   * @param prob the probability value
   * @param minBet the minimum bet value
   * @param maxBet the maximum bet value
   * @return a mock Environment instance
   */
  private Environment createMockEnvironment(String prefix, String name, String prize, String prob, String minBet, String maxBet) {
    return new Environment() {
      @Override
      public String getProperty(String key) {
        if (key.equals(prefix + ".name")) return name;
        if (key.equals(prefix + ".uuid")) return "test-uuid-" + prefix;
        if (key.equals(prefix + ".type")) return "Test Type";
        if (key.equals(prefix + ".prize")) return prize;
        if (key.equals(prefix + ".prob")) return prob;
        if (key.equals(prefix + ".minbet")) return minBet;
        if (key.equals(prefix + ".maxbet")) return maxBet;
        return null;
      }

      @Override
      public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
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
        return getProperty(key) != null;
      }
    };
  }

  /**
   * Creates a mock environment that returns null for numeric properties.
   * Used to test parseSafely with null values.
   *
   * @return a mock Environment instance with null numeric values
   */
  private Environment createMockEnvironmentWithNulls() {
    return createMockEnvironment("game", "Test Game", null, null, null, null);
  }
}
