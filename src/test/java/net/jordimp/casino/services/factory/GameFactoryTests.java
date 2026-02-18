package net.jordimp.casino.services.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import net.jordimp.casino.services.vo.Blackjack;
import net.jordimp.casino.services.vo.Game;
import net.jordimp.casino.services.vo.Poker;
import net.jordimp.casino.services.vo.Roulette;
import net.jordimp.casino.services.vo.Slot;
import net.jordimp.casino.services.vo.VideoBingo;
import net.jordimp.casino.utils.EnvWrapperUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

class GameFactoryTests {

  private GameFactory factory;

  @BeforeEach
  void setUp() {
    factory = new GameFactory();
    // Setup environment for all game types
    EnvWrapperUtils.setEnv(createMockEnvironment());
  }

  @AfterEach
  void tearDown() {
    EnvWrapperUtils.setEnv(null);
  }

  @Test
  void create_withVideoBingoUUID_returnsVideoBingo() {
    Game game = factory.create("VIDEOBINGO-UUID");

    assertNotNull(game);
    assertEquals(VideoBingo.class, game.getClass());
  }

  @Test
  void create_withBlackjackUUID_returnsBlackjack() {
    Game game = factory.create("BLACKJACK-UUID");

    assertNotNull(game);
    assertEquals(Blackjack.class, game.getClass());
  }

  @Test
  void create_withPokerUUID_returnsPoker() {
    Game game = factory.create("POKER-UUID");

    assertNotNull(game);
    assertEquals(Poker.class, game.getClass());
  }

  @Test
  void create_withRouletteUUID_returnsRoulette() {
    Game game = factory.create("ROULETTE-UUID");

    assertNotNull(game);
    assertEquals(Roulette.class, game.getClass());
  }

  @Test
  void create_withSlotUUID_returnsSlot() {
    Game game = factory.create("SLOT-UUID");

    assertNotNull(game);
    assertEquals(Slot.class, game.getClass());
  }

  @Test
  void create_withUnknownUUID_returnsNull() {
    Game game = factory.create("UNKNOWN-GAME-UUID");

    assertNull(game);
  }

  @Test
  void create_withNullUUID_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> {
      factory.create(null);
    });
  }

  @Test
  void create_withEmptyString_returnsNull() {
    Game game = factory.create("");

    assertNull(game);
  }

  // Helper method to create a mock environment with all game properties
  private Environment createMockEnvironment() {
    return new Environment() {
      @Override
      public String getProperty(String key) {
        // Return default values for all game properties
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
}
