package net.jordimp.casino;

import static org.junit.jupiter.api.Assertions.*;

import net.jordimp.casino.controllers.RestPlayGameController;
import net.jordimp.casino.services.dto.Bet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RestPlayGameControllerValidationTests {

  @Autowired private RestPlayGameController controller;

  @Test
  void testBetRejectsMismatchedUUIDs() {
    Bet bet = new Bet(10.0, "player-123", "game-456", 100.0);
    bet.setPlayerUUID("different-player-uuid");

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          controller.bet("path-uuid-123", bet);
        });
  }

  @Test
  void testBetRejectsNegativeAmount() {
    Bet bet = new Bet(-10.0, "player-123", "game-456", 100.0);
    bet.setPlayerUUID("player-123");

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          controller.bet("player-123", bet);
        });
  }

  @Test
  void testBetRejectsZeroAmount() {
    Bet bet = new Bet(0.0, "player-123", "game-456", 100.0);
    bet.setPlayerUUID("player-123");

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          controller.bet("player-123", bet);
        });
  }

  @Test
  void testBetRejectsExcessiveAmount() {
    Bet bet = new Bet(99999.0, "player-123", "game-456", 100.0);
    bet.setPlayerUUID("player-123");

    assertThrows(
        IllegalArgumentException.class,
        () -> {
          controller.bet("player-123", bet);
        });
  }
}
