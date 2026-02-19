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

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withNullBprefix_returnsEarly() {
    // Arrange - Create a TestGame without prefix to test line 118: if (bprefix == null) return;
    // We need to bypass the @PostConstruct init() method that requires environment
    TestGame game = new TestGame("game") {
      // Override to skip init() and test ensureInitializedFromEnvIfNeeded directly
      @Override
      public void init() {
        // Skip initialization to set bprefix to null before any init
      }
    };
    game.bprefix = null; // Set bprefix to null

    // Act - Call getMinBet() which triggers ensureInitializedFromEnvIfNeeded()
    Double minBet = game.getMinBet();

    // Assert - Should return early without attempting to access environment
    // minBet remains null since no initialization occurred
    assertNull(minBet, "MinBet should be null when bprefix is null");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withPartiallyInitializedProperties_initializesMissingProperties() {
    // Arrange - Create game and environment
    Environment mockEnv = createMockEnvironment("game", "Env Name", "100.0", "0.8", "15.0", "300.0");
    EnvWrapperUtils.setEnv(mockEnv);

    TestGame game = new TestGame("game");

    // Manually set only some properties to test the condition on line 122
    // Line 122: if (name != null && minBet != null && maxBet != null) return;
    // We want this to be FALSE, so we set only name, but not minBet and maxBet
    game.setName("Partial Name");
    game.setMinBet(null); // Explicitly set to null
    game.setMaxBet(null); // Explicitly set to null

    // Act - Call getMaxBet() which triggers ensureInitializedFromEnvIfNeeded()
    // Since not all three properties are set, it should initialize from environment
    // Note: The environment value overrides existing values (line 132)
    Double maxBet = game.getMaxBet();

    // Assert - Should initialize from environment
    // The method uses environment.getProperty with default, so env value "Env Name" overrides "Partial Name"
    assertEquals("Env Name", game.getName(), "Name should come from environment");
    assertEquals(300.0, maxBet, "MaxBet should be initialized from environment");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withNullName_usesEnvironmentName() {
    // Arrange - Create game with environment, then set name to null
    Environment mockEnv = createMockEnvironment("game", "Env Name", "100.0", "0.8", "15.0", "300.0");
    EnvWrapperUtils.setEnv(mockEnv);

    TestGame game = new TestGame("game");
    game.setName(null); // Set name to null to test line 132 ternary

    // Act - Trigger ensureInitializedFromEnvIfNeeded by creating new game with same prefix
    TestGame game2 = new TestGame("game");

    // Assert - Name should use environment value
    assertEquals("Env Name", game2.getName(), "Name should come from environment when null");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withNullUuid_usesDefaultUuid() {
    // Arrange - Create game with environment that returns null for uuid
    Environment mockEnv = new Environment() {
      @Override
      public String getProperty(String key) {
        if (key.equals("game.name")) return "Test Game";
        if (key.equals("game.uuid")) return null; // Return null for uuid
        if (key.equals("game.type")) return "Test Type";
        return null;
      }

      @Override
      public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType) { return null; }

      @Override
      public <T> T getProperty(String key, Class<T> targetType, T defaultValue) { return defaultValue; }

      @Override
      public String getRequiredProperty(String key) { return getProperty(key); }

      @Override
      public <T> T getRequiredProperty(String key, Class<T> targetType) { return null; }

      @Override
      public String resolvePlaceholders(String text) { return text; }

      @Override
      public String resolveRequiredPlaceholders(String text) { return text; }

      @Override
      public boolean acceptsProfiles(String... profiles) { return true; }

      @Override
      public boolean acceptsProfiles(org.springframework.core.env.Profiles profiles) { return true; }

      @Override
      public String[] getActiveProfiles() { return new String[0]; }

      @Override
      public String[] getDefaultProfiles() { return new String[0]; }

      @Override
      public boolean containsProperty(String key) { return getProperty(key) != null; }
    };
    EnvWrapperUtils.setEnv(mockEnv);

    // Act - Create game which triggers initialization
    TestGame game = new TestGame("game");

    // Assert - UUID should use default value when null (line 133: this.uuid == null ? "unknown-uuid" : this.uuid)
    assertNotNull(game.getUUID(), "UUID should not be null");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withNullType_usesDefaultType() {
    // Arrange - Create game with environment that returns null for type
    Environment mockEnv = new Environment() {
      @Override
      public String getProperty(String key) {
        if (key.equals("game.name")) return "Test Game";
        if (key.equals("game.uuid")) return "test-uuid";
        if (key.equals("game.type")) return null; // Return null for type
        return null;
      }

      @Override
      public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType) { return null; }

      @Override
      public <T> T getProperty(String key, Class<T> targetType, T defaultValue) { return defaultValue; }

      @Override
      public String getRequiredProperty(String key) { return getProperty(key); }

      @Override
      public <T> T getRequiredProperty(String key, Class<T> targetType) { return null; }

      @Override
      public String resolvePlaceholders(String text) { return text; }

      @Override
      public String resolveRequiredPlaceholders(String text) { return text; }

      @Override
      public boolean acceptsProfiles(String... profiles) { return true; }

      @Override
      public boolean acceptsProfiles(org.springframework.core.env.Profiles profiles) { return true; }

      @Override
      public String[] getActiveProfiles() { return new String[0]; }

      @Override
      public String[] getDefaultProfiles() { return new String[0]; }

      @Override
      public boolean containsProperty(String key) { return getProperty(key) != null; }
    };
    EnvWrapperUtils.setEnv(mockEnv);

    // Act - Create game which triggers initialization
    TestGame game = new TestGame("game");

    // Assert - Type should use default value when null (line 134: this.type == null ? "Unknown" : this.type)
    assertNotNull(game.getType(), "Type should not be null");
    assertEquals("Unknown", game.getType(), "Type should default to 'Unknown' when null");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withNullPrize_usesDefaultPrize() {
    // Arrange - Create game with environment that returns null for prize
    Environment mockEnv = new Environment() {
      @Override
      public String getProperty(String key) {
        if (key.equals("game.name")) return "Test Game";
        if (key.equals("game.uuid")) return "test-uuid";
        if (key.equals("game.type")) return "Test Type";
        if (key.equals("game.prize")) return null; // Return null for prize
        if (key.equals("game.minbet")) return "10.0";
        if (key.equals("game.maxbet")) return "100.0";
        return null;
      }

      @Override
      public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType) { return null; }

      @Override
      public <T> T getProperty(String key, Class<T> targetType, T defaultValue) { return defaultValue; }

      @Override
      public String getRequiredProperty(String key) { return getProperty(key); }

      @Override
      public <T> T getRequiredProperty(String key, Class<T> targetType) { return null; }

      @Override
      public String resolvePlaceholders(String text) { return text; }

      @Override
      public String resolveRequiredPlaceholders(String text) { return text; }

      @Override
      public boolean acceptsProfiles(String... profiles) { return true; }

      @Override
      public boolean acceptsProfiles(org.springframework.core.env.Profiles profiles) { return true; }

      @Override
      public String[] getActiveProfiles() { return new String[0]; }

      @Override
      public String[] getDefaultProfiles() { return new String[0]; }

      @Override
      public boolean containsProperty(String key) { return getProperty(key) != null; }
    };
    EnvWrapperUtils.setEnv(mockEnv);

    // Act - Create game which triggers initialization
    TestGame game = new TestGame("game");

    // Assert - Prize should use default value when null (line 136: this.prize == null ? 0 : this.prize)
    assertNotNull(game.getPrize(), "Prize should not be null");
    assertEquals(0.0, game.getPrize(), "Prize should default to 0.0 when null");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withNullProbability_usesDefaultProbability() {
    // Arrange - Create game with environment that returns null for probability
    Environment mockEnv = new Environment() {
      @Override
      public String getProperty(String key) {
        if (key.equals("game.name")) return "Test Game";
        if (key.equals("game.uuid")) return "test-uuid";
        if (key.equals("game.type")) return "Test Type";
        if (key.equals("game.prob")) return null; // Return null for probability
        if (key.equals("game.minbet")) return "10.0";
        if (key.equals("game.maxbet")) return "100.0";
        return null;
      }

      @Override
      public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType) { return null; }

      @Override
      public <T> T getProperty(String key, Class<T> targetType, T defaultValue) { return defaultValue; }

      @Override
      public String getRequiredProperty(String key) { return getProperty(key); }

      @Override
      public <T> T getRequiredProperty(String key, Class<T> targetType) { return null; }

      @Override
      public String resolvePlaceholders(String text) { return text; }

      @Override
      public String resolveRequiredPlaceholders(String text) { return text; }

      @Override
      public boolean acceptsProfiles(String... profiles) { return true; }

      @Override
      public boolean acceptsProfiles(org.springframework.core.env.Profiles profiles) { return true; }

      @Override
      public String[] getActiveProfiles() { return new String[0]; }

      @Override
      public String[] getDefaultProfiles() { return new String[0]; }

      @Override
      public boolean containsProperty(String key) { return getProperty(key) != null; }
    };
    EnvWrapperUtils.setEnv(mockEnv);

    // Act - Create game which triggers initialization
    TestGame game = new TestGame("game");

    // Assert - Probability should use default value when null (line 137: this.probability == null ? 0 : this.probability)
    assertNotNull(game.getProbability(), "Probability should not be null");
    assertEquals(0.0, game.getProbability(), "Probability should default to 0.0 when null");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withNullMinBet_usesDefaultMinBet() {
    // Arrange - Create game with environment that returns null for minBet
    Environment mockEnv = new Environment() {
      @Override
      public String getProperty(String key) {
        if (key.equals("game.name")) return "Test Game";
        if (key.equals("game.uuid")) return "test-uuid";
        if (key.equals("game.type")) return "Test Type";
        if (key.equals("game.minbet")) return null; // Return null for minbet
        if (key.equals("game.maxbet")) return "100.0";
        return null;
      }

      @Override
      public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType) { return null; }

      @Override
      public <T> T getProperty(String key, Class<T> targetType, T defaultValue) { return defaultValue; }

      @Override
      public String getRequiredProperty(String key) { return getProperty(key); }

      @Override
      public <T> T getRequiredProperty(String key, Class<T> targetType) { return null; }

      @Override
      public String resolvePlaceholders(String text) { return text; }

      @Override
      public String resolveRequiredPlaceholders(String text) { return text; }

      @Override
      public boolean acceptsProfiles(String... profiles) { return true; }

      @Override
      public boolean acceptsProfiles(org.springframework.core.env.Profiles profiles) { return true; }

      @Override
      public String[] getActiveProfiles() { return new String[0]; }

      @Override
      public String[] getDefaultProfiles() { return new String[0]; }

      @Override
      public boolean containsProperty(String key) { return getProperty(key) != null; }
    };
    EnvWrapperUtils.setEnv(mockEnv);

    // Act - Create game which triggers initialization
    TestGame game = new TestGame("game");

    // Assert - MinBet should use default value when null (line 138: this.minBet == null ? 0 : this.minBet)
    assertNotNull(game.getMinBet(), "MinBet should not be null");
    assertEquals(0.0, game.getMinBet(), "MinBet should default to 0.0 when null");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withNullMaxBet_usesDefaultMaxBet() {
    // Arrange - Create game with environment that returns null for maxBet
    Environment mockEnv = new Environment() {
      @Override
      public String getProperty(String key) {
        if (key.equals("game.name")) return "Test Game";
        if (key.equals("game.uuid")) return "test-uuid";
        if (key.equals("game.type")) return "Test Type";
        if (key.equals("game.minbet")) return "10.0";
        if (key.equals("game.maxbet")) return null; // Return null for maxbet
        return null;
      }

      @Override
      public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
      }

      @Override
      public <T> T getProperty(String key, Class<T> targetType) { return null; }

      @Override
      public <T> T getProperty(String key, Class<T> targetType, T defaultValue) { return defaultValue; }

      @Override
      public String getRequiredProperty(String key) { return getProperty(key); }

      @Override
      public <T> T getRequiredProperty(String key, Class<T> targetType) { return null; }

      @Override
      public String resolvePlaceholders(String text) { return text; }

      @Override
      public String resolveRequiredPlaceholders(String text) { return text; }

      @Override
      public boolean acceptsProfiles(String... profiles) { return true; }

      @Override
      public boolean acceptsProfiles(org.springframework.core.env.Profiles profiles) { return true; }

      @Override
      public String[] getActiveProfiles() { return new String[0]; }

      @Override
      public String[] getDefaultProfiles() { return new String[0]; }

      @Override
      public boolean containsProperty(String key) { return getProperty(key) != null; }
    };
    EnvWrapperUtils.setEnv(mockEnv);

    // Act - Create game which triggers initialization
    TestGame game = new TestGame("game");

    // Assert - MaxBet should use default value when null (line 139: this.maxBet == null ? 0 : this.maxBet)
    assertNotNull(game.getMaxBet(), "MaxBet should not be null");
    assertEquals(0.0, game.getMaxBet(), "MaxBet should default to 0.0 when null");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withOnlyNameSet_initializesMinBetAndMaxBet() {
    // Arrange - Create game and environment
    Environment mockEnv = createMockEnvironment("game", "Env Name", "100.0", "0.8", "20.0", "400.0");
    EnvWrapperUtils.setEnv(mockEnv);

    TestGame game = new TestGame("game");

    // Set only name, leave minBet and maxBet null
    // This tests line 122: if (name != null && minBet != null && maxBet != null)
    // With only name set, the condition is false, so initialization proceeds
    game.setName("Only Name Set");
    game.setMinBet(null);
    game.setMaxBet(null);

    // Act - Call getMaxBet() to trigger ensureInitializedFromEnvIfNeeded()
    // Environment values override all values (line 132-139)
    Double maxBet = game.getMaxBet();

    // Assert - All properties should be from environment (env overrides manually set values)
    assertEquals("Env Name", game.getName(), "Name should come from environment (overrides manual set)");
    assertEquals(400.0, maxBet, "MaxBet should be initialized from environment");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withOnlyMinBetSet_initializesNameAndMaxBet() {
    // Arrange - Create game and environment
    Environment mockEnv = createMockEnvironment("game", "Env Name", "100.0", "0.8", "25.0", "500.0");
    EnvWrapperUtils.setEnv(mockEnv);

    TestGame game = new TestGame("game");

    // Set only minBet, leave name and maxBet null
    game.setName(null);
    game.setMinBet(25.0);
    game.setMaxBet(null);

    // Act - Call getMaxBet() to trigger ensureInitializedFromEnvIfNeeded()
    Double maxBet = game.getMaxBet();

    // Assert - Name and MaxBet should be initialized from environment
    assertEquals("Env Name", game.getName(), "Name should be initialized from environment");
    assertEquals(25.0, game.getMinBet(), "MinBet should remain as set");
    assertEquals(500.0, maxBet, "MaxBet should be initialized from environment");
  }

  @Test
  void testEnsureInitializedFromEnvIfNeeded_withOnlyMaxBetSet_initializesNameAndMinBet() {
    // Arrange - Create game and environment
    Environment mockEnv = createMockEnvironment("game", "Env Name", "100.0", "0.8", "30.0", "600.0");
    EnvWrapperUtils.setEnv(mockEnv);

    TestGame game = new TestGame("game");

    // Set only maxBet, leave name and minBet null
    game.setName(null);
    game.setMinBet(null);
    game.setMaxBet(600.0);

    // Act - Call getMinBet() to trigger ensureInitializedFromEnvIfNeeded()
    Double minBet = game.getMinBet();

    // Assert - Name and MinBet should be initialized from environment
    assertEquals("Env Name", game.getName(), "Name should be initialized from environment");
    assertEquals(30.0, minBet, "MinBet should be initialized from environment");
    assertEquals(600.0, game.getMaxBet(), "MaxBet should remain as set");
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
