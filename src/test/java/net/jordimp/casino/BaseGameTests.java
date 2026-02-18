package net.jordimp.casino;

import static org.junit.jupiter.api.Assertions.*;

import net.jordimp.casino.services.vo.Roulette;
import net.jordimp.casino.utils.EnvWrapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

public class BaseGameTests {

  @BeforeEach
  void setUp() {
    // Clear the static environment before each test
    EnvWrapperUtils.setEnv(null);
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
}
